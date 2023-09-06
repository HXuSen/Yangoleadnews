package com.yango.schedule.service;

import com.yango.model.schedule.dtos.Task;

/**
 * ClassName: TaskService
 * Package: com.yango.schedule.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-19:31
 */
public interface TaskService {

    long addTask(Task task);

    boolean cancelTask(long taskId);

    Task poll(int type,int priority);
}
