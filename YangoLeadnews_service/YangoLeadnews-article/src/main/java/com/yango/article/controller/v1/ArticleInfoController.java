package com.yango.article.controller.v1;

import com.yango.article.service.ApArticleService;
import com.yango.model.article.dtos.ArticleInfoDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ArticleInfoController
 * Package: com.yango.article.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-18:55
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleInfoController {
    @Autowired
    private ApArticleService apArticleService;

    @PostMapping("/load_article_behavior")
    public ResponseResult loadArticleBehavior(@RequestBody ArticleInfoDto dto){
        return apArticleService.loadArticleBehavior(dto);
    }
}
