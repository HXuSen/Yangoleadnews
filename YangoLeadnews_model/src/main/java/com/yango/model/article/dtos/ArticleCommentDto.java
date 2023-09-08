package com.yango.model.article.dtos;

import com.yango.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * ClassName: ArticleCommentDto
 * Package: com.yango.model.article.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-18:50
 */
@Data
public class ArticleCommentDto extends PageRequestDto {
    private String beginDate;
    private String endDate;
    private Integer wmUserId;
}
