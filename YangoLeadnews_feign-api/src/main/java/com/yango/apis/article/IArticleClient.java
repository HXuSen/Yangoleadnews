package com.yango.apis.article;

import com.yango.apis.article.fallback.IArticleClientFallback;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ClassName: IArticleClient
 * Package: com.yango.apis.article
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-17:17
 */
@FeignClient(value = "leadnews-article",fallback = IArticleClientFallback.class)
public interface IArticleClient {

    @PostMapping("/api/v1/article/save")
    ResponseResult saveArticle(@RequestBody ArticleDto dto);

    @GetMapping("/api/v1/article/findArticleConfigByArticleId/{articleId}")
    ResponseResult findArticleConfigByArticleId(@PathVariable("articleId") Long articleId);
}
