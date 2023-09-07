package com.yango.comment.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ClassName: ApCommentRepayLike
 * Package: com.yango.comment.pojo
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-16:43
 */
@Data
@Document("ap_comment_repay_like")
public class ApCommentRepayLike {
    private String id;
    private Integer authorId;
    private String commentRepayId;
    private Short operation;
}
