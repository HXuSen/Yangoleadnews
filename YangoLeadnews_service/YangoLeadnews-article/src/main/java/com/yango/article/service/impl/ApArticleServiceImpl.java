package com.yango.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yango.article.mapper.ApArticleConfigMapper;
import com.yango.article.mapper.ApArticleContentMapper;
import com.yango.article.mapper.ApArticleMapper;
import com.yango.article.service.ApArticleService;
import com.yango.article.service.ArticleFreemarkerService;
import com.yango.common.constants.ArticleConstants;
import com.yango.common.constants.BehaviorConstants;
import com.yango.common.redis.CacheService;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.article.dtos.ArticleHomeDto;
import com.yango.model.article.dtos.ArticleInfoDto;
import com.yango.model.article.pojos.ApArticle;
import com.yango.model.article.pojos.ApArticleConfig;
import com.yango.model.article.pojos.ApArticleContent;
import com.yango.model.article.vo.HotArticleVo;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.message.ArticleVisitStreamMess;
import com.yango.model.user.pojos.ApUser;
import com.yango.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: ApArticleServiceImpl
 * Package: com.yango.article.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-15:26
 */
@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ArticleFreemarkerService articleFreemarkerService;
    @Autowired
    private CacheService cacheService;

    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        //1.校验参数
        //分页条数的校验
        Integer size = dto.getSize();
        if (size == null || size == 0){
            dto.setSize(10);
        }
        //分页值不能超过50
        Math.min(size, ArticleConstants.MAX_PAGE_SIZE);
        //type校验
        if (!ArticleConstants.LOADTYPE_LOAD_MORE.equals(type) && !ArticleConstants.LOADTYPE_LOAD_NEW.equals(type)){
            type = 1;
        }
        //频道参数校验
        if (StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        //时间校验
        if (dto.getMaxBehotTime() == null) dto.setMaxBehotTime(new Date());
        if (dto.getMinBehotTime() == null) dto.setMinBehotTime(new Date());

        List<ApArticle> apArticles = apArticleMapper.loadArticleList(dto, type);
        return ResponseResult.okResult(apArticles);
    }

    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        //try {
        //    Thread.sleep(3000);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
        //1.check
        if (dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticle article = new ApArticle();
        BeanUtils.copyProperties(dto,article);
        //2.id isExist
        if (dto.getId() == null) {
            //2.1 false save(article,articleConfig,articleContent)
            //2.1.1 article
            save(article);
            //2.1.2 articleConfig
            ApArticleConfig articleConfig = new ApArticleConfig(article.getId());
            apArticleConfigMapper.insert(articleConfig);
            //2.1.3 articleContent
            ApArticleContent articleContent = new ApArticleContent();
            articleContent.setArticleId(article.getId());
            articleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(articleContent);
        }else{
            //2.2 true update(article,articleContent)
            //2.2.1 article
            updateById(article);
            //2.2.2 articleContent
            ApArticleContent articleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, dto.getId()));
            articleContent.setContent(dto.getContent());
            apArticleContentMapper.updateById(articleContent);
        }

        articleFreemarkerService.buildArticleToMinio(article,dto.getContent());

        return ResponseResult.okResult(article.getId());
    }

    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {
        if (dto == null || dto.getArticleId() == null || dto.getAuthorId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        boolean islike = false,isunlike = false,iscollection = false,isfollow = false;
        ApUser user = AppThreadLocalUtil.getUser();
        if (user != null) {
            String likeBehaviorJSON = (String) cacheService.hGet(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId(), user.getId().toString());
            if (StringUtils.isNotBlank(likeBehaviorJSON)) {
                islike = true;
            }
            String unLikeBehaviorJson = (String) cacheService.hGet(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId(), user.getId().toString());
            if(StringUtils.isNotBlank(unLikeBehaviorJson)){
                isunlike = true;
            }
            String collectionJson = (String) cacheService.hGet(BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(),dto.getArticleId().toString());
            if(StringUtils.isNotBlank(collectionJson)){
                iscollection = true;
            }
            Double score = cacheService.zScore(BehaviorConstants.APUSER_FOLLOW_RELATION + user.getId(), dto.getAuthorId().toString());
            if(score != null){
                isfollow = true;
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("islike",islike);
        map.put("isunlike",isunlike);
        map.put("iscollection",iscollection);
        map.put("isfollow",isfollow);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage) {
        if (firstPage){
            String jsonStr = cacheService.get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + dto.getTag());
            if (StringUtils.isNotBlank(jsonStr)){
                List<HotArticleVo> hotArticleVos = JSON.parseArray(jsonStr, HotArticleVo.class);
                ResponseResult responseResult = ResponseResult.okResult(hotArticleVos);
                return responseResult;
            }
        }
        return load(dto,type);
    }

    @Override
    public void updateScore(ArticleVisitStreamMess mess) {
        //1.更新文章的阅读 点赞 收藏 评论的数量
        ApArticle apArticle = updateArticle(mess);
        //2.计算文章的分值
        Integer score = computeScore(apArticle);
        score = score * 3;
        //3.替换当前文章对应频道的热点数据
        replaceDataToRedis(apArticle,score,ArticleConstants.HOT_ARTICLE_FIRST_PAGE + apArticle.getChannelId());
        //4.替换推荐对应的热点数据
        replaceDataToRedis(apArticle,score,ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);
    }

    private void replaceDataToRedis(ApArticle apArticle, Integer score, String type) {
        String articleListStr = cacheService.get(type);
        if (StringUtils.isNotBlank(articleListStr)){
            List<HotArticleVo> hotArticleVos = JSON.parseArray(articleListStr, HotArticleVo.class);
            boolean flag = true;
            for (HotArticleVo hotArticleVo : hotArticleVos) {
                if (hotArticleVo.getId().equals(apArticle.getId())) {
                    hotArticleVo.setScore(score);
                    flag = false;
                    break;
                }
            }
            if (flag){
                if (hotArticleVos.size() >= 30){
                    hotArticleVos = hotArticleVos.stream()
                            .sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
                    HotArticleVo lastHot = hotArticleVos.get(hotArticleVos.size() - 1);
                    if (lastHot.getScore() < score) {
                        hotArticleVos.remove(lastHot);
                        HotArticleVo vo = new HotArticleVo();
                        BeanUtils.copyProperties(apArticle,vo);
                        vo.setScore(score);
                        hotArticleVos.add(vo);
                    }
                }else{
                    HotArticleVo vo = new HotArticleVo();
                    BeanUtils.copyProperties(apArticle,vo);
                    vo.setScore(score);
                    hotArticleVos.add(vo);
                }
            }

            hotArticleVos = hotArticleVos.stream()
                    .sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
            cacheService.set(type,JSON.toJSONString(hotArticleVos));
        }
    }

    private ApArticle updateArticle(ArticleVisitStreamMess mess) {
        ApArticle apArticle = getById(mess.getArticleId());
        apArticle.setCollection((apArticle.getCollection() == null ? 0 : apArticle.getCollection()) + mess.getCollect());
        apArticle.setComment((apArticle.getComment() == null ? 0 : apArticle.getComment()) + mess.getComment());
        apArticle.setLikes((apArticle.getLikes() == null ? 0 : apArticle.getLikes()) + mess.getLike());
        apArticle.setViews((apArticle.getViews() == null ? 0 : apArticle.getViews()) + mess.getView());
        updateById(apArticle);
        return apArticle;
    }
    private Integer computeScore(ApArticle article) {
        Integer score = 0;
        if (article.getViews() != null) {
            score += article.getViews();
        }
        if (article.getLikes() != null) {
            score += article.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if (article.getComment() != null) {
            score += article.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if (article.getCollection() != null) {
            score += article.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        return score;
    }
}
