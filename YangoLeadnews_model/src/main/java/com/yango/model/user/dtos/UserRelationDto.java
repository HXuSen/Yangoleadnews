package com.yango.model.user.dtos;

import com.yango.model.annotation.IdEncrypt;
import lombok.Data;

/**
 * ClassName: UserRelationDto
 * Package: com.yango.model.user.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-16:39
 */
@Data
public class UserRelationDto {
    @IdEncrypt
    private Long articleId;
    @IdEncrypt
    private Integer authorId;

    private Short operation;
}
