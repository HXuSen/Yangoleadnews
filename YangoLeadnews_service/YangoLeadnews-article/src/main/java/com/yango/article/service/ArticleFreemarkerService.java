package com.yango.article.service;

import com.yango.model.article.pojos.ApArticle;

/**
 * ClassName: ArticleFreemarkerService
 * Package: com.yango.article.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-17:23
 */
public interface ArticleFreemarkerService {

    void buildArticleToMinio(ApArticle article,String content);
}
