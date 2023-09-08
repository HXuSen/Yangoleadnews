package com.yango.model.comment.dto;

import com.yango.model.annotation.IdEncrypt;
import com.yango.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * ClassName: CommentManageDto
 * Package: com.yango.model.comment.dto
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-18:46
 */
@Data
public class CommentManageDto extends PageRequestDto {
    //@IdEncrypt
    private Long articleId;
}
