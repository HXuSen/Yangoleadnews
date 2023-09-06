package com.yango.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.model.article.dtos.ArticleHomeDto;
import com.yango.model.article.pojos.ApArticle;
import com.yango.model.article.pojos.ApArticleContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: ApArticleContentMapper
 * Package: com.yango.article.mapper
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-17:48
 */
@Mapper
public interface ApArticleContentMapper extends BaseMapper<ApArticleContent> {

}
