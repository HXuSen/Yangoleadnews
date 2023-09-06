package com.yango.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.WmMaterialDto;
import com.yango.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName: WmMaterialService
 * Package: com.yango.wemedia.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-10:18
 */
public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * 图片上传
     * @param multipartFile
     * @return
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 素材列表查询
     * @param dto
     * @return
     */
    ResponseResult findList(WmMaterialDto dto);

    ResponseResult delPicById(Integer id);

    ResponseResult collectPicById(Integer id);

    ResponseResult cancelCollectPicById(Integer id);
}
