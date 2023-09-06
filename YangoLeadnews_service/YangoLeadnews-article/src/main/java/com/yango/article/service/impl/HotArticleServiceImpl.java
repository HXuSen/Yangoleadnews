package com.yango.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.yango.apis.wemedia.IWemediaClient;
import com.yango.article.mapper.ApArticleMapper;
import com.yango.article.service.HotArticleService;
import com.yango.common.constants.ArticleConstants;
import com.yango.common.redis.CacheService;
import com.yango.model.article.pojos.ApArticle;
import com.yango.model.article.vo.HotArticleVo;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * ClassName: HotArticleServiceImpl
 * Package: com.yango.article.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/3-16:26
 */
@Service
@Transactional
@Slf4j
public class HotArticleServiceImpl implements HotArticleService {
    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public void HotArticleCompute() {
        //查询最近五天的文章
        Date dateParam = DateTime.now().minusDays(5).toDate();
        List<ApArticle> apArticleList = apArticleMapper.findArticleListByLast5Day(dateParam);

        //计算文章分值
        List<HotArticleVo> hotArticleVos = computeArticle(apArticleList);

        //为每个频道缓存30条分值较高的文章
        cacheTagToRedis(hotArticleVos);
    }

    @Autowired
    private IWemediaClient iWemediaClient;
    @Autowired
    private CacheService cacheService;
    private void cacheTagToRedis(List<HotArticleVo> hotArticleVos) {
        ResponseResult responseResult = iWemediaClient.getChannels();
        if (responseResult.getCode().equals(200)){
            String jsonString = JSON.toJSONString(responseResult.getData());
            List<WmChannel> wmChannels = JSON.parseArray(jsonString, WmChannel.class);
            if (wmChannels != null && wmChannels.size() > 0) {
                for (WmChannel wmChannel : wmChannels) {
                    List<HotArticleVo> collect = hotArticleVos.stream()
                            .filter(vo -> wmChannel.getId().equals(vo.getChannelId()))
                            .collect(Collectors.toList());

                    sortAndCache(collect, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + wmChannel.getId());
                }
            }
        }

        sortAndCache(hotArticleVos,ArticleConstants.DEFAULT_TAG);
    }

    private void sortAndCache(List<HotArticleVo> collect,String key) {
        List<HotArticleVo> result = collect.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
        if (result.size() > 30){
            result = result.subList(0,30);
        }
        cacheService.set(key,JSON.toJSONString(result));
    }


    private List<HotArticleVo> computeArticle(List<ApArticle> apArticleList) {
        List<HotArticleVo> list = new ArrayList();
        if (apArticleList != null && apArticleList.size() > 0) {
            for (ApArticle article : apArticleList) {
                HotArticleVo hotArticleVo = new HotArticleVo();
                BeanUtils.copyProperties(article,hotArticleVo);
                Integer score = computeScore(article);
                hotArticleVo.setScore(score);
                list.add(hotArticleVo);
            }
        }
        return list;
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
