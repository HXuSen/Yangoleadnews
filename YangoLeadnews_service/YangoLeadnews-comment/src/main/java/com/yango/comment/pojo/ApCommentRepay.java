package com.yango.comment.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * ClassName: ApCommentRepay
 * Package: com.yango.comment.pojo
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-16:26
 */
@Data
@Document("ap_comment_repay")
public class ApCommentRepay {
    @Id
    private String id;
    private Integer authorId;
    private String authorName;
    private String commentId;
    private String content;
    private Integer likes;
    private Date createdTime;
    private Date updatedTime;
}
