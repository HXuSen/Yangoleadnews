package com.yango.comment.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ClassName: ApCommentLike
 * Package: com.yango.comment.pojo
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-15:47
 */
@Data
@Document("ap_comment_like")
public class ApCommentLike {
    private String id;
    private Integer authorId;
    private String commentId;
    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Short operation;
}
