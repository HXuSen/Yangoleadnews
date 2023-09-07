package com.yango.model.comment.dto;

import lombok.Data;

/**
 * ClassName: CommentLikeDto
 * Package: com.yango.model.comment.dto
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-15:42
 */
@Data
public class CommentLikeDto {
    private String commentId;
    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Short operation;
}
