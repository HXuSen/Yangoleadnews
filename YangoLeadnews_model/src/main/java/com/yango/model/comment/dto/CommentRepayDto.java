package com.yango.model.comment.dto;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: CommentRepayDto
 * Package: com.yango.model.comment.dto
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-16:35
 */
@Data
public class CommentRepayDto {
    private String commentId;
    private Date minDate;
    private Integer size;
}
