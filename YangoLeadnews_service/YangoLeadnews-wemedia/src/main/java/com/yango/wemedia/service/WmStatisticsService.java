package com.yango.wemedia.service;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.StatisticsDto;

/**
 * ClassName: WmStatisticsService
 * Package: com.yango.wemedia.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/8-13:33
 */
public interface WmStatisticsService {
    ResponseResult newDimension(String beginDate, String endDate);

    ResponseResult newsPage(StatisticsDto dto);
}
