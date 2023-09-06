package com.yango.model.behavior.dtos;

import com.yango.model.annotation.IdEncrypt;
import lombok.Data;

/**
 * ClassName: UnLikesBehaviorDto
 * Package: com.yango.model.behavior.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-16:08
 */
@Data
public class UnLikesBehaviorDto {
    @IdEncrypt
    private Long articleId;
    private Short type;
}
