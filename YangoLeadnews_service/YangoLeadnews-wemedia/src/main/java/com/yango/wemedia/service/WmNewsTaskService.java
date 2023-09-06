package com.yango.wemedia.service;

import java.util.Date;

/**
 * ClassName: WmNewsTaskService
 * Package: com.yango.wemedia.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-10:50
 */
public interface WmNewsTaskService {

    void addNewsToTask(Integer articleId, Date publishTime);

    void scanNewsByTask();
}
