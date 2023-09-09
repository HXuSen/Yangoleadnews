package com.yango.article.feign;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yango.apis.article.IArticleClient;
import com.yango.article.service.ApArticleConfigService;
import com.yango.article.service.ApArticleService;
import com.yango.model.article.dtos.ArticleCommentDto;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.article.pojos.ApArticleConfig;
import com.yango.model.comment.dto.CommentConfigDto;
import com.yango.model.common.dtos.PageResponseResult;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.StatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    @Autowired
    private ApArticleConfigService apArticleConfigService;
    @Override
    @GetMapping("/api/v1/article/findArticleConfigByArticleId/{articleId}")
    public ResponseResult findArticleConfigByArticleId(@PathVariable("articleId") Long articleId) {
        ApArticleConfig articleConfig = apArticleConfigService.getOne(Wrappers.<ApArticleConfig>lambdaQuery().eq(ApArticleConfig::getArticleId, articleId));
        return ResponseResult.okResult(articleConfig);
    }

    @Override
    @PostMapping("/api/v1/article/findNewsComments")
    public ResponseResult findNewsComments(@RequestBody ArticleCommentDto dto) {
        return apArticleService.findNewsComments(dto);
    }

    @Override
    @PostMapping("/api/v1/article/updateCommentStatus")
    public ResponseResult updateCommentStatus(@RequestBody CommentConfigDto dto) {
        return apArticleConfigService.updateCommentStatus(dto);
    }

    @Override
    @PostMapping("/api/v1/article/newPage")
    public PageResponseResult newPage(@RequestBody StatisticsDto dto) {
        return apArticleService.newPage(dto);
    }

    @Override
    @GetMapping("/api/v1/article/queryLikesAndConllections")
    public ResponseResult queryBehaviors(@RequestParam("wmUserId") Integer wmUserId,@RequestParam("beginDate") Date beginDate,@RequestParam("endDate") Date endDate) {
        return apArticleService.queryBehaviors(wmUserId,beginDate,endDate);
    }
}
