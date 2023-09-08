package com.yango.apis.article.fallback;

import com.yango.apis.article.IArticleClient;
import com.yango.model.article.dtos.ArticleCommentDto;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.comment.dto.CommentConfigDto;
import com.yango.model.common.dtos.PageResponseResult;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

/**
 * ClassName: IArticleClientFallback
 * Package: com.yango.apis.article.fallback
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-15:47
 */
@Component
public class IArticleClientFallback implements IArticleClient {
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取数据失败");
    }

    @Override
    public ResponseResult findArticleConfigByArticleId(Long articleId) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取数据失败");
    }

    @Override
    public ResponseResult findNewsComments(ArticleCommentDto dto) {
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),0);
        responseResult.setCode(501);
        responseResult.setErrorMessage("获取数据失败");
        return responseResult;
    }

    @Override
    public ResponseResult updateCommentStatus(CommentConfigDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取数据失败");
    }
}
