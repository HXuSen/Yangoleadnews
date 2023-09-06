package com.yango.wemedia.service;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.pojos.WmNews;

/**
 * ClassName: WmNewsAutoScanService
 * Package: com.yango.wemedia.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-14:16
 */
public interface WmNewsAutoScanService {

    void autoScanWmNews(Integer id);

    ResponseResult saveAppArticle(WmNews wmNews);
}
