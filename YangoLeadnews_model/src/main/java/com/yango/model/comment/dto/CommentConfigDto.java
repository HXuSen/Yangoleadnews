package com.yango.model.comment.dto;

import com.yango.model.annotation.IdEncrypt;
import lombok.Data;

/**
 * ClassName: CommentConfigDto
 * Package: com.yango.model.comment.dto
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-19:47
 */
@Data
public class CommentConfigDto {
    @IdEncrypt
    private Long articleId;
    private Short operation;
}
