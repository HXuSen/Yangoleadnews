package com.yango.apis.article;

import com.yango.apis.article.fallback.IArticleClientFallback;
import com.yango.model.article.dtos.ArticleCommentDto;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.comment.dto.CommentConfigDto;
import com.yango.model.common.dtos.PageResponseResult;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.StatisticsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    @PostMapping("/api/v1/article/findNewsComments")
    ResponseResult findNewsComments(@RequestBody ArticleCommentDto dto);

    @PostMapping("/api/v1/article/updateCommentStatus")
    ResponseResult updateCommentStatus(@RequestBody CommentConfigDto dto);

    @PostMapping("/api/v1/article/newPage")
    PageResponseResult newPage(@RequestBody StatisticsDto dto);

    @GetMapping("/api/v1/article/queryLikesAndConllections")
    ResponseResult queryBehaviors(@RequestParam("wmUserId") Integer wmUserId,@RequestParam("beginDate") Date beginDate,@RequestParam("endDate") Date endDate);
}
