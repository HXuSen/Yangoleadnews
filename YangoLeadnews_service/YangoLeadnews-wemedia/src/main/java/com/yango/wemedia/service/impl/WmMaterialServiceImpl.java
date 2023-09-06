package com.yango.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yango.common.constants.WemediaConstants;
import com.yango.file.service.FileStorageService;
import com.yango.model.common.dtos.PageResponseResult;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.wemedia.dtos.WmMaterialDto;
import com.yango.model.wemedia.pojos.WmMaterial;
import com.yango.model.wemedia.pojos.WmNewsMaterial;
import com.yango.utils.thread.WmThreadLocalUtil;
import com.yango.wemedia.mapper.WmMaterialMapper;
import com.yango.wemedia.mapper.WmNewsMaterialMapper;
import com.yango.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * ClassName: WmMaterialServiceImpl
 * Package: com.yango.wemedia.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-10:18
 */
@Service
@Transactional
@Slf4j
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * 图片上传
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //1.检查参数
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.上传图片到minio
        String filename = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String path = null;
        try {
            path = fileStorageService.uploadImgFile("", filename + suffix, multipartFile.getInputStream());
            log.info("上传图片至Minio:{}",path);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传文件失败");
        }
        //3.保存路径到数据库
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(path);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setType((short) 0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);
        //4.返回结果
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(WmMaterialDto dto) {
        //1.检查参数
        dto.checkParam();
        //2.分页查询
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmMaterial> queryWrapper = new LambdaQueryWrapper();
        //2.1 是否收藏
        if (dto.getIsCollection() != null && dto.getIsCollection() == 1){
            queryWrapper.eq(WmMaterial::getIsCollection,dto.getIsCollection());
        }
        //2.2 按用户id
        queryWrapper.eq(WmMaterial::getUserId,WmThreadLocalUtil.getUser().getId());
        //2.3按时间倒序
        queryWrapper.orderByDesc(WmMaterial::getCreatedTime);

        page(page,queryWrapper);
        //3.结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult delPicById(Integer id) {
        if (id == null)
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        WmMaterial material = getById(id);
        if (material == null)
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        List<WmNewsMaterial> wmNewsMaterials = wmNewsMaterialMapper.selectList(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getMaterialId, id));
        if (wmNewsMaterials.size() != 0)
            return ResponseResult.errorResult(AppHttpCodeEnum.MATERIAL_IS_EXIST);
        boolean flag = removeById(id);
        if (!flag)
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"文件删除失败");
        else
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult collectPicById(Integer id) {
        if (id == null)
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        update(Wrappers.<WmMaterial>lambdaUpdate().eq(WmMaterial::getId,id).set(WmMaterial::getIsCollection, WemediaConstants.COLLECT_MATERIAL));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult cancelCollectPicById(Integer id) {
        if (id == null)
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        update(Wrappers.<WmMaterial>lambdaUpdate().eq(WmMaterial::getId,id).set(WmMaterial::getIsCollection, WemediaConstants.CANCEL_COLLECT_MATERIAL));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
