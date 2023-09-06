package com.yango.article.stream;

import com.alibaba.fastjson.JSON;
import com.yango.common.constants.HotArticleConstants;
import com.yango.model.message.ArticleVisitStreamMess;
import com.yango.model.message.UpdateArticleMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * ClassName: HotArticleStreamHandler
 * Package: com.yango.article.stream
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/4-14:58
 */
@Configuration
@Slf4j
public class HotArticleStreamHandler {

    @Bean
    public KStream<String,String> kStream(StreamsBuilder streamsBuilder){
        //1.接受消息
        KStream<String, String> stream = streamsBuilder.stream(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC);
        //2.流式处理
        stream.map((key,val) -> {
            UpdateArticleMess mess = JSON.parseObject(val, UpdateArticleMess.class);
            return new KeyValue<>(mess.getArticleId().toString(),mess.getType().name() + ":" + mess.getAdd());
        })
                .groupBy((key,val) -> key)
                .windowedBy(TimeWindows.of(Duration.ofSeconds(5)))
                .aggregate(new Initializer<String>() {
                    @Override
                    public String apply() {
                        return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
                    }
                }, new Aggregator<String, String, String>() {
                    @Override
                    public String apply(String key, String val, String aggVal) {
                        if (StringUtils.isBlank(val)) {
                            return aggVal;
                        }
                        String[] aggAry = aggVal.split(",");
                        int col=0,com=0,lik=0,vie=0;
                        for (String agg : aggAry) {
                            String[] split = agg.split(":");
                            switch (UpdateArticleMess.UpdateArticleType.valueOf(split[0])){
                                case COLLECTION:
                                    col = Integer.parseInt(split[1]);
                                    break;
                                case COMMENT:
                                    com = Integer.parseInt(split[1]);
                                    break;
                                case LIKES:
                                    lik = Integer.parseInt(split[1]);
                                    break;
                                case VIEWS:
                                    vie = Integer.parseInt(split[1]);
                                    break;
                            }
                        }
                        String[] valAry = val.split(":");
                        switch (UpdateArticleMess.UpdateArticleType.valueOf(valAry[0])){
                            case COLLECTION:
                                col += Integer.parseInt(valAry[1]);
                                break;
                            case COMMENT:
                                com += Integer.parseInt(valAry[1]);
                                break;
                            case LIKES:
                                lik += Integer.parseInt(valAry[1]);
                                break;
                            case VIEWS:
                                vie += Integer.parseInt(valAry[1]);
                                break;
                        }
                        String formatStr = String.format("COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d", col, com, lik, vie);
                        return formatStr;
                    }
                },Materialized.as("hot-article-stream-count-001"))
                .toStream()
                .map((key,val) -> {
                    return new KeyValue<>(key.key().toString(),formatObj(key.key().toString(),val));
                })
                .to(HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC);
        //3.发送
        return stream;
    }

    private String formatObj(String articleId, String val) {
        ArticleVisitStreamMess mess = new ArticleVisitStreamMess();
        mess.setArticleId(Long.valueOf(articleId));
        String[] valAry = val.split(",");
        for (String value : valAry) {
            String[] split = value.split(":");
            switch (UpdateArticleMess.UpdateArticleType.valueOf(split[0])){
                case COLLECTION:
                    mess.setCollect(Integer.parseInt(split[1]));
                    break;
                case COMMENT:
                    mess.setComment(Integer.parseInt(split[1]));
                    break;
                case LIKES:
                    mess.setLike(Integer.parseInt(split[1]));
                    break;
                case VIEWS:
                    mess.setView(Integer.parseInt(split[1]));
                    break;
            }
        }
        return JSON.toJSONString(mess);
    }
}
