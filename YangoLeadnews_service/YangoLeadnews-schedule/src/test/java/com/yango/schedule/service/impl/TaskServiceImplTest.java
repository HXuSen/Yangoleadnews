package com.yango.schedule.service.impl;

import com.yango.model.schedule.dtos.Task;
import com.yango.schedule.ScheduleApplication;
import com.yango.schedule.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * ClassName: TaskServiceImplTest
 * Package: com.yango.schedule.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-19:45
 */
@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceImplTest {
    @Autowired
    private TaskService taskService;

    @Test
    public void addTask() {
        for (int i = 0;i < 5;i++){
            Task task = new Task();
            task.setTaskType(100 + i);
            task.setPriority(50);
            task.setParameters("task_test".getBytes());
            task.setExecuteTime(new Date().getTime()+500*i);
            long taskId = taskService.addTask(task);
        }
    }

    @Test
    public void cancelTask() {
        taskService.cancelTask(1696853520365436930L);
    }

    @Test
    public void pollTask() {
        Task task = taskService.poll(100, 50);
        System.out.println("task = " + task);
    }
}