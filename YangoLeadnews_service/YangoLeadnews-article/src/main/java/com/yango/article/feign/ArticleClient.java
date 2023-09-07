package com.yango.article.feign;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yango.apis.article.IArticleClient;
import com.yango.article.service.ApArticleConfigService;
import com.yango.article.service.ApArticleService;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.article.pojos.ApArticleConfig;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
