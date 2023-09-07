package com.yango.comment.service;

import com.yango.model.comment.dto.CommentRepayDto;
import com.yango.model.comment.dto.CommentRepayLikeDto;
import com.yango.model.comment.dto.CommentRepaySaveDto;
import com.yango.model.common.dtos.ResponseResult;

/**
 * ClassName: CommentRepayService
 * Package: com.yango.comment.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-16:21
 */
public interface CommentRepayService {
    ResponseResult saveCommentRepay(CommentRepaySaveDto dto);

    ResponseResult loadCommentRepayByCommentId(CommentRepayDto dto);

    ResponseResult like(CommentRepayLikeDto dto);
}
