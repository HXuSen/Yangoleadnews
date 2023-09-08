package com.yango.model.article.vo;

import com.yango.model.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: ArticleCommentVo
 * Package: com.yango.model.article.vo
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-18:58
 */
@Data
public class ArticleCommentVo {
    @IdEncrypt
    private Long id;

    private String title;

    private Integer comments;

    private Boolean isComment;

    private Date publishTime;
}