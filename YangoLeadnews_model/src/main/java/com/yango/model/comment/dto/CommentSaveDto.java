package com.yango.model.comment.dto;

import com.yango.model.annotation.IdEncrypt;
import lombok.Data;

/**
 * ClassName: CommentSaveDto
 * Package: com.yango.model.comment.dto
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/6-20:26
 */
@Data
public class CommentSaveDto {
    @IdEncrypt
    private Long articleId;
    private String content;
}
