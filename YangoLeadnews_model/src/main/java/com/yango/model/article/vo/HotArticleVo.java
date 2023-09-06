package com.yango.model.article.vo;

import com.yango.model.article.pojos.ApArticle;
import lombok.Data;

/**
 * ClassName: HotArticle
 * Package: com.yango.model.article.vo
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/3-16:33
 */
@Data
public class HotArticleVo extends ApArticle {
    private Integer score;
}
