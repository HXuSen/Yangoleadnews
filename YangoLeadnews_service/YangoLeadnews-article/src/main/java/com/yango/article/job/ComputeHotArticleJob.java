package com.yango.article.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.yango.article.service.HotArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName: ComputeHotArticleJob
 * Package: com.yango.article.job
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/3-18:04
 */
@Component
@Slf4j
public class ComputeHotArticleJob {
    @Autowired
    private HotArticleService hotArticleService;

    @XxlJob("computeHotArticleJob")
    public void handle(){
        log.info("hotArticle compute starting...");
        hotArticleService.HotArticleCompute();
        log.info("hotArticle compute ending...");
    }
}
