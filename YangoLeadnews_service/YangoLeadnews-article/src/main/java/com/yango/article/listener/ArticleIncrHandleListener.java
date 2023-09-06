package com.yango.article.listener;

import com.alibaba.fastjson.JSON;
import com.yango.article.service.ApArticleService;
import com.yango.common.constants.HotArticleConstants;
import com.yango.model.message.ArticleVisitStreamMess;
import com.yango.model.message.UpdateArticleMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: ArticleIncrHandleListener
 * Package: com.yango.article.listener
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/4-16:02
 */
@Component
@Slf4j
public class ArticleIncrHandleListener {
    @Autowired
    private ApArticleService apArticleService;

    @KafkaListener(topics = HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC)
    public void onMessage(String mess){
        if (StringUtils.isNotBlank(mess)) {
            ArticleVisitStreamMess updateArticleMess = JSON.parseObject(mess, ArticleVisitStreamMess.class);
            apArticleService.updateScore(updateArticleMess);
        }
    }
}
