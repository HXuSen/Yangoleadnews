package com.yango.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.article.dtos.ArticleCommentDto;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.article.dtos.ArticleHomeDto;
import com.yango.model.article.dtos.ArticleInfoDto;
import com.yango.model.article.pojos.ApArticle;
import com.yango.model.common.dtos.PageResponseResult;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.message.ArticleVisitStreamMess;
import com.yango.model.message.UpdateArticleMess;
import com.yango.model.wemedia.dtos.StatisticsDto;

import java.util.Date;

/**
 * ClassName: ApArticleService
 * Package: com.yango.article.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-15:25
 */
public interface ApArticleService extends IService<ApArticle> {
    ResponseResult load(ArticleHomeDto dto,Short type);

    ResponseResult saveArticle(ArticleDto dto);

    ResponseResult loadArticleBehavior(ArticleInfoDto dto);

    ResponseResult load2(ArticleHomeDto dto, Short type,boolean firstPage);

    void updateScore(ArticleVisitStreamMess mess);

    ResponseResult findNewsComments(ArticleCommentDto dto);

    PageResponseResult newPage(StatisticsDto dto);

    ResponseResult queryBehaviors(Integer wmUserId, Date beginDate, Date endDate);
}
