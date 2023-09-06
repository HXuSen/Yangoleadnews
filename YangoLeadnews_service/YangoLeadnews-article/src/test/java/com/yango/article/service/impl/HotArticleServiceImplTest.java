package com.yango.article.service.impl;

import com.yango.article.ArticleApplication;
import com.yango.article.service.HotArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * ClassName: HotArticleServiceImplTest
 * Package: com.yango.article.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/3-17:01
 */
@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class HotArticleServiceImplTest {
    @Autowired
    private HotArticleService hotArticleService;

    @Test
    public void hotArticleCompute() {
        hotArticleService.HotArticleCompute();
    }
}