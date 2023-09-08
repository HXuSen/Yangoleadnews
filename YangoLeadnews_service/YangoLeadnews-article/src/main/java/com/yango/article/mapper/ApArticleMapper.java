package com.yango.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.model.article.dtos.ArticleCommentDto;
import com.yango.model.article.dtos.ArticleHomeDto;
import com.yango.model.article.pojos.ApArticle;
import com.yango.model.article.vo.ArticleCommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * ClassName: ApArticleMapper
 * Package: com.yango.article.mapper
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-15:17
 */
@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {
    /**
     * 加载文章列表
     * @param dto
     * @param type 1=加载更多 2=加载最新
     * @return
     */
    List<ApArticle> loadArticleList(@Param("dto") ArticleHomeDto dto,@Param("type") Short type);

    List<ApArticle> findArticleListByLast5Day(@Param("dayParam")Date dayParam);

    List<ArticleCommentVo> findNewsComment(@Param("dto") ArticleCommentDto dto);

    int findNewsCommentsCount(@Param("dto") ArticleCommentDto dto);
}
