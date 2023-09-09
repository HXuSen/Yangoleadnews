package com.yango.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yango.apis.article.IArticleClient;
import com.yango.model.common.dtos.PageResponseResult;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.StatisticsDto;
import com.yango.model.wemedia.pojos.WmNews;
import com.yango.model.wemedia.pojos.WmUser;
import com.yango.utils.common.DateUtils;
import com.yango.utils.thread.WmThreadLocalUtil;
import com.yango.wemedia.service.WmNewsService;
import com.yango.wemedia.service.WmStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: WmStatisticsServiceImpl
 * Package: com.yango.wemedia.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/8-13:34
 */
@Service
@Slf4j
public class WmStatisticsServiceImpl implements WmStatisticsService {
    @Autowired
    private IArticleClient articleClient;
    @Autowired
    private WmNewsService wmNewsServicem;
    @Override
    public ResponseResult newDimension(String beginDate, String endDate) {

        Date beginDateTime = DateUtils.stringToDate(beginDate);
        Date endDateTime = DateUtils.stringToDate(endDate);
        WmUser user = WmThreadLocalUtil.getUser();
        /*
        int publishNum = wmNewsServicem.count(Wrappers.<WmNews>lambdaQuery()
                .eq(WmNews::getUserId, user.getId())
                .eq(WmNews::getStatus, WmNews.Status.PUBLISHED.getCode())
                .eq(WmNews::getEnable, 1)
                .between(WmNews::getPublishTime, beginDateTime, endDateTime));
        map.put("publishNum",publishNum);
         */

        ResponseResult result = articleClient.queryBehaviors(user.getId(),beginDateTime,endDateTime);
        Map<String,Object> map = new HashMap<>();
        if (result.getCode().equals(200)) {
            String resJson = JSON.toJSONString(result.getData());
            Map returnMap = JSON.parseObject(resJson, Map.class);
            map.put("likesNum",returnMap.get("likes") == null ? 0 : returnMap.get("likes"));
            map.put("collectNum",returnMap.get("collections") == null ? 0 : returnMap.get("collections"));
            map.put("publishNum",returnMap.get("newsCount") == null ? 0 : returnMap.get("newsCount"));
        }
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult newsPage(StatisticsDto dto) {
        WmUser user = WmThreadLocalUtil.getUser();
        dto.setWmUserId(user.getId());
        PageResponseResult result = articleClient.newPage(dto);
        return result;
    }
}
