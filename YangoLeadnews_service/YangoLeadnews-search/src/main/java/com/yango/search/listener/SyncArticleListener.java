package com.yango.search.listener;

import com.alibaba.fastjson.JSON;
import com.yango.common.constants.ArticleConstants;
import com.yango.model.search.vos.SearchArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ClassName: SyncArticleListener
 * Package: com.yango.search.listener
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-19:33
 */
@Component
@Slf4j
public class SyncArticleListener {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @KafkaListener(topics = ArticleConstants.ARTICLE_ES_SYNC_TOPIC)
    public void onMessage(String message){
        if (StringUtils.isNotBlank(message)) {
            log.info("SyncArticleListener-message:{}",message);
            SearchArticleVo vo = JSON.parseObject(message, SearchArticleVo.class);

            IndexRequest indexRequest = new IndexRequest("app_info_article");
            indexRequest.id(vo.getId().toString())
                            .source(message, XContentType.JSON);
            try {
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("sync es error:{}",e);
            }
        }
    }
}
