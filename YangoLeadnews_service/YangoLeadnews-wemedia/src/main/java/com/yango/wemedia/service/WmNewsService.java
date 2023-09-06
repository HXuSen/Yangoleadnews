package com.yango.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.NewsAuthDto;
import com.yango.model.wemedia.dtos.WmNewDto;
import com.yango.model.wemedia.dtos.WmNewsDto;
import com.yango.model.wemedia.dtos.WmNewsPageReqDto;
import com.yango.model.wemedia.pojos.WmNews;

/**
 * ClassName: WmNewsService
 * Package: com.yango.wemedia.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-11:51
 */
public interface WmNewsService extends IService<WmNews> {
    /**
     * 条件查询文章列表
     * @param dto
     * @return
     */
    ResponseResult findList(WmNewsPageReqDto dto);

    /**
     * 发布修改文章或保存为草稿
     * @param dto
     * @return
     */
    ResponseResult submitNews(WmNewsDto dto);

    ResponseResult getNewsById(Integer id);

    ResponseResult delNewsById(Integer id);

    ResponseResult downOrUp(WmNewDto dto);

    ResponseResult listVo(NewsAuthDto dto);

    ResponseResult getOneVo(Integer id);

    ResponseResult updateStatus(NewsAuthDto dto, Short status);
}
