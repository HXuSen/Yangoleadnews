package com.yango.model.comment.dto;

import com.yango.model.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: CommentDto
 * Package: com.yango.model.comment.dto
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-15:39
 */
@Data
public class CommentDto {
    @IdEncrypt
    private Long articleId;
    // 最小时间
    private Date minDate;
    //是否是首页
    private Short index;
}
