package com.yango.schedule.feign;

import com.yango.apis.schedule.IScheduleClient;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.schedule.dtos.Task;
import com.yango.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: ScheduleClient
 * Package: com.yango.schedule.feign
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-10:46
 */
@RestController
public class ScheduleClient implements IScheduleClient {
    @Autowired
    private TaskService taskService;

    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@RequestBody Task task){
        return ResponseResult.okResult(taskService.addTask(task));
    }

    @Override
    @GetMapping("/api/v1/task/cancel/{taskId}")
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId) {
        return ResponseResult.okResult(taskService.cancelTask(taskId));
    }

    @Override
    @GetMapping("/api/v1/task/poll/{type}/{priority}")
    public ResponseResult poll(@PathVariable("type") int type,@PathVariable("priority")  int priority) {
        return ResponseResult.okResult(taskService.poll(type,priority));
    }
}
