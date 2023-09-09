package com.yango.wemedia.controller.v1;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.StatisticsDto;
import com.yango.wemedia.service.WmStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: WmStatisticsController
 * Package: com.yango.wemedia.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/8-13:33
 */
@RestController
@RequestMapping("/api/v1/statistics")
public class WmStatisticsController {
    @Autowired
    private WmStatisticsService wmStatisticsService;

    @GetMapping("/newsDimension")
    public ResponseResult newDimension(String beginDate,String endDate){
        return wmStatisticsService.newDimension(beginDate,endDate);
    }

    @GetMapping("/newsPage")
    public ResponseResult newsPage(StatisticsDto dto){
        return wmStatisticsService.newsPage(dto);
    }
}
