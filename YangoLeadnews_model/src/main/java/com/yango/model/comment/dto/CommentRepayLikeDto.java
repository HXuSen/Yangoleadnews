package com.yango.model.comment.dto;

import lombok.Data;

/**
 * ClassName: CommentRepayLikeDto
 * Package: com.yango.model.comment.dto
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-16:37
 */
@Data
public class CommentRepayLikeDto {
    private String commentRepayId;
    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Short operation;
}
