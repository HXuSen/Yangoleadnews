package com.yango.article.feign;

import com.yango.apis.article.IArticleClient;
import com.yango.article.service.ApArticleService;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ArticleClient
 * Package: com.yango.article.feign
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-17:19
 */
@RestController
public class ArticleClient implements IArticleClient {
    @Autowired
    private ApArticleService apArticleService;

    @Override
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto) {
        return apArticleService.saveArticle(dto);
    }
}
