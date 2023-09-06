package com.yango.model.behavior.dtos;

import lombok.Data;

/**
 * ClassName: LikesBehaviorDto
 * Package: com.yango.model.behavior.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-14:43
 */
@Data
public class LikesBehaviorDto {
    // 文章、动态、评论等ID
    Long articleId;
    /**
     * 喜欢内容类型
     * 0文章
     * 1动态
     * 2评论
     */
    Short type;

    /**
     * 喜欢操作方式
     * 0 点赞
     * 1 取消点赞
     */
    Short operation;
}
