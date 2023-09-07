package com.yango.comment.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: ApComment
 * Package: com.yango.comment.pojo
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/6-20:30
 */
@Data
@Document("ap_comment")
public class ApComment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 用户ID  发表评论的用户id
     */
    private Integer authorId;
    /**
     * 用户昵称
     */
    private String authorName;
    /**
     * 文章id或动态id
     */
    private Long entryId;
    /**
     * 评论内容类型
     * 0 文章
     * 1 动态
     */
    private Short type;
    private String content;
    private Integer likes;
    private Integer reply;
    /**
     * 文章标记
     * 0 普通评论
     * 1 热点评论
     * 2 推荐评论
     * 3 置顶评论
     * 4 精品评论
     * 5 大V 评论
     */
    private Short flag;
    private Date createdTime;

    /**
     * 频道ID
     */
    private Integer channelId;
    /**
     * 作者头像
     */
    private String image;
    /**
     * 评论排列序号
     */
    private Integer ord;
    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 评论状态  0 关闭状态   1 正常状态
     */
    private Boolean status;

}
