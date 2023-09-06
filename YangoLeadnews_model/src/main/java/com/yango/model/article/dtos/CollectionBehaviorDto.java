package com.yango.model.article.dtos;

import com.yango.model.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: CollectionBehaviorDto
 * Package: com.yango.model.article.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-17:27
 */
@Data
public class CollectionBehaviorDto {
    @IdEncrypt
    private Long entryId;
    private Short operation;
    private Date publishedTime;
    private Short type;
}
