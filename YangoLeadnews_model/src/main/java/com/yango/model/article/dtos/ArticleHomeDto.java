package com.yango.model.article.dtos;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: ArticleHomeDto
 * Package: com.yango.model.article.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-15:16
 */
@Data
public class ArticleHomeDto {
    // 最大时间
    Date maxBehotTime;
    // 最小时间
    Date minBehotTime;
    // 分页size
    Integer size;
    // 频道ID
    String tag;
}
