package com.yango.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.yango.apis.schedule.IScheduleClient;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.TaskTypeEnum;
import com.yango.model.schedule.dtos.Task;
import com.yango.model.wemedia.pojos.WmNews;
import com.yango.utils.common.ProtostuffUtil;
import com.yango.wemedia.service.WmNewsAutoScanService;
import com.yango.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * ClassName: WmNewsTaskServiceImpl
 * Package: com.yango.wemedia.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-10:51
 */
@Service
@Transactional
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {
    @Autowired
    private IScheduleClient iScheduleClient;
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Override
    @Async
    public void addNewsToTask(Integer articleId, Date publishTime) {
        Task task = new Task();
        task.setExecuteTime(publishTime.getTime());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        WmNews wmNews = new WmNews();
        wmNews.setId(articleId);
        task.setParameters(ProtostuffUtil.serialize(wmNews));
        iScheduleClient.addTask(task);
    }

    @Override
    @Scheduled(fixedRate = 1000)
    public void scanNewsByTask() {
        ResponseResult responseResult = iScheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if (responseResult.getCode().equals(200) && responseResult.getData() != null) {
            Task task = JSON.parseObject(JSON.toJSONString(responseResult.getData()), Task.class);
            WmNews wmNews = ProtostuffUtil.deserialize(task.getParameters(), WmNews.class);
            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        }
    }

}
