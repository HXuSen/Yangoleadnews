package com.yango.wemedia.service;

import com.yango.model.article.dtos.ArticleCommentDto;
import com.yango.model.comment.dto.CommentConfigDto;
import com.yango.model.comment.dto.CommentLikeDto;
import com.yango.model.comment.dto.CommentManageDto;
import com.yango.model.comment.dto.CommentRepaySaveDto;
import com.yango.model.common.dtos.ResponseResult;

/**
 * ClassName: CommentManageService
 * Package: com.yango.wemedia.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-18:44
 */
public interface CommentManageService {
    ResponseResult findNewsComments(ArticleCommentDto dto);
    ResponseResult list(CommentManageDto dto);

    ResponseResult updateCommentStatus(CommentConfigDto dto);

    ResponseResult saveCommentRepay(CommentRepaySaveDto dto);

    ResponseResult like(CommentLikeDto dto);

    ResponseResult delComment(String commentId);

    ResponseResult delCommentRepay(String commentRepayId);
}
