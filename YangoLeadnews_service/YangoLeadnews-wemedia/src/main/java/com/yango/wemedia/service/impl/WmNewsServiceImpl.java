package com.yango.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yango.common.constants.WemediaConstants;
import com.yango.common.constants.WmNewsMessageConstants;
import com.yango.common.exception.CustomException;
import com.yango.model.common.dtos.PageResponseResult;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.wemedia.dtos.NewsAuthDto;
import com.yango.model.wemedia.dtos.WmNewDto;
import com.yango.model.wemedia.dtos.WmNewsDto;
import com.yango.model.wemedia.dtos.WmNewsPageReqDto;
import com.yango.model.wemedia.pojos.WmMaterial;
import com.yango.model.wemedia.pojos.WmNews;
import com.yango.model.wemedia.pojos.WmNewsMaterial;
import com.yango.model.wemedia.pojos.WmUser;
import com.yango.model.wemedia.vo.WmNewsVo;
import com.yango.utils.thread.WmThreadLocalUtil;
import com.yango.wemedia.mapper.WmMaterialMapper;
import com.yango.wemedia.mapper.WmNewsMapper;
import com.yango.wemedia.mapper.WmNewsMaterialMapper;
import com.yango.wemedia.mapper.WmUserMapper;
import com.yango.wemedia.service.WmNewsAutoScanService;
import com.yango.wemedia.service.WmNewsService;
import com.yango.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * ClassName: WmNewsServiceImpl
 * Package: com.yango.wemedia.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-11:51
 */
@Service
@Transactional
@Slf4j
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;
    @Autowired
    private WmMaterialMapper wmMaterialMapper;
    @Autowired
    private WmNewsTaskService wmNewsTaskService;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Override
    public ResponseResult findList(WmNewsPageReqDto dto) {
        //1.检查参数
        dto.checkParam();
        //2.条件查询
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(dto.getStatus() != null,WmNews::getStatus,dto.getStatus());
        queryWrapper.eq(dto.getChannelId() != null,WmNews::getChannelId,dto.getChannelId());
        queryWrapper.between(dto.getBeginPubDate() != null && dto.getEndPubDate() != null,
                WmNews::getPublishTime,dto.getBeginPubDate(),dto.getEndPubDate());
        queryWrapper.like(StringUtils.isNotBlank(dto.getKeyword()),WmNews::getTitle,dto.getKeyword());
        queryWrapper.eq(WmNews::getUserId, WmThreadLocalUtil.getUser().getId());
        queryWrapper.orderByDesc(WmNews::getPublishTime);
        page(page,queryWrapper);
        //3.结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult submitNews(WmNewsDto dto) {
        if (dto == null || dto.getContent() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //1.保存或修改文章
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto,wmNews);
        if (dto.getImages() != null && dto.getImages().size() > 0) {
            String imageStr = StringUtils.join(dto.getImages(), ",");
            wmNews.setImages(imageStr);
        }
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())){
            wmNews.setType(null);
        }
        saveOrUpdateWmNews(wmNews);
        //2.判断是否为草稿
        if (dto.getStatus().equals(WmNews.Status.NORMAL.getCode())){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        //2.1不是草稿,保存文章图片与素材的关系
        List<String> materials = ectractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials,wmNews.getId());
        //2.2不是草稿,保存文章封面与素材的关系,布局类型为自动
        saveRelativeInfoForCover(dto,wmNews,materials);

        //审核文章
        //wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        wmNewsTaskService.addNewsToTask(wmNews.getId(),wmNews.getPublishTime());

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult getNewsById(Integer id) {
        if (id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews news = getById(id);
        if (news == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"文章不存在");
        }
        return ResponseResult.okResult(news);
    }

    @Override
    public ResponseResult delNewsById(Integer id) {
        if (id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"文章Id不可缺少");
        }
        WmNews news = getById(id);
        if (news == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"文章不存在");
        }

        if (news.getStatus() == 9){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"文章已发布,不能删除");
        }
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult downOrUp(WmNewDto dto) {
        if (dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"文章Id不可缺少");
        }
        WmNews news = getById(dto.getId());
        if (news == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"文章不存在");
        }
        if (!(news.getStatus().equals(WmNews.Status.PUBLISHED.getCode()))){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"文章不是发布状态,不能上下架");
        }
        if (dto.getEnable() != null && dto.getEnable() > -1 && dto.getEnable() < 2){
            update(Wrappers.<WmNews>lambdaUpdate().eq(WmNews::getId,news.getId()).set(WmNews::getEnable,dto.getEnable()));
            if (news.getArticleId() != null){
                Map<String,Object> map = new HashMap<>();
                map.put("articleId",news.getArticleId());
                map.put("enable",dto.getEnable());
                kafkaTemplate.send(WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC,JSON.toJSONString(map));
            }
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult listVo(NewsAuthDto dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();

        int currentPage = dto.getPage();
        dto.setPage((dto.getPage() - 1) * dto.getSize());
        List<WmNewsVo> wmNews = wmNewsMapper.findListAndPage(dto);
        int count = wmNewsMapper.findListCount(dto);

        ResponseResult result = new PageResponseResult(currentPage,dto.getSize(),count);
        result.setData(wmNews);
        return result;
    }

    @Autowired
    private WmUserMapper wmUserMapper;
    @Override
    public ResponseResult getOneVo(Integer id) {
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        WmNewsVo wmNewsVo = new WmNewsVo();
        BeanUtils.copyProperties(wmNews,wmNewsVo);
        if (wmUser != null) {
            wmNewsVo.setAuthorName(wmUser.getName());
        }
        return ResponseResult.okResult(wmNewsVo);
    }

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;
    @Override
    public ResponseResult updateStatus(NewsAuthDto dto, Short status) {
        if (dto == null || dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews wmNews = getById(dto.getId());
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        wmNews.setStatus(status);
        if (dto.getMsg() != null) {
            wmNews.setReason(dto.getMsg());
        }
        updateById(wmNews);

        if (status.equals(WemediaConstants.WM_NEWS_AUTH_PASS)){
            ResponseResult result = wmNewsAutoScanService.saveAppArticle(wmNews);
            if (result.getCode().equals(200)) {
                wmNews.setArticleId((Long)result.getData());
                wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
                updateById(wmNews);
            }
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {
        List<String> images = dto.getImages();
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())){
            if (materials.size() >= 3){
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.stream().limit(3).collect(Collectors.toList());
            }else if (materials.size() >= 1 && materials.size() < 3){
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.stream().limit(1).collect(Collectors.toList());
            }else{
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }
            if (images != null && images.size() > 0){
                wmNews.setImages(StringUtils.join(images,","));
            }
            updateById(wmNews);
        }
        if (images != null && images.size() > 0){
            saveRelativeInfo(images, wmNews.getId(), WemediaConstants.WM_COVER_REFERENCE);
        }
    }

    private void saveRelativeInfoForContent(List<String> materials, Integer newsId) {
        saveRelativeInfo(materials,newsId,WemediaConstants.WM_CONTENT_REFERENCE);
    }

    private void saveRelativeInfo(List<String> materials, Integer newsId, Short type) {
        if (materials != null && !materials.isEmpty()){
            List<WmMaterial> dbMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery().in(WmMaterial::getUrl, materials));
            if (dbMaterials == null || dbMaterials.size() == 0){
                throw new CustomException(AppHttpCodeEnum.MATERIAL_REFERENCE_FAIL);
            }
            if (materials.size() != dbMaterials.size()){
                throw new CustomException(AppHttpCodeEnum.MATERIAL_REFERENCE_FAIL);
            }
            List<Integer> materialIds = dbMaterials.stream()
                    .map(material -> material.getId())
                    .collect(Collectors.toList());
            wmNewsMaterialMapper.saveRelations(materialIds,newsId,type);
        }
    }

    private List<String> ectractUrlInfo(String content) {
        List<String> materials = new ArrayList<>();
        List<Map> maps = JSON.parseArray(content, Map.class);
        for (Map map : maps) {
            if ("image".equals(map.get("type"))){
                String imgUrl = (String) map.get("value");
                materials.add(imgUrl);
            }
        }
        return materials;
    }

    /**
     * 保存或修改文章
     * @param wmNews
     */
    private void saveOrUpdateWmNews(WmNews wmNews) {
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short)1);
        if (wmNews.getId() == null){
            save(wmNews);
        }else{
            wmNewsMaterialMapper.deleteById(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId()));
            updateById(wmNews);
        }
    }
}
