package com.yango.model.article.dtos;

import com.yango.model.annotation.IdEncrypt;
import lombok.Data;

/**
 * ClassName: ArticleInfoDto
 * Package: com.yango.model.article.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-18:56
 */
@Data
public class ArticleInfoDto {
    @IdEncrypt
    private Long articleId;
    @IdEncrypt
    private Integer authorId;
}
