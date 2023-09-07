package com.yango.comment.service;

import com.yango.model.comment.dto.CommentDto;
import com.yango.model.comment.dto.CommentLikeDto;
import com.yango.model.comment.dto.CommentSaveDto;
import com.yango.model.common.dtos.ResponseResult;

/**
 * ClassName: CommentService
 * Package: com.yango.comment.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/6-20:36
 */
public interface CommentService {
    ResponseResult saveComment(CommentSaveDto dto);

    ResponseResult like(CommentLikeDto dto);

    ResponseResult loadCommentsByArticleId(CommentDto dto);
}
