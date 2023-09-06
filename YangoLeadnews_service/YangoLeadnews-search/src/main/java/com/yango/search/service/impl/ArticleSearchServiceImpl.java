package com.yango.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.search.dtos.UserSearchDto;
import com.yango.model.user.pojos.ApUser;
import com.yango.search.service.ApUserSearchService;
import com.yango.search.service.ArticleSearchService;
import com.yango.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ArticleSearchServiceImpl
 * Package: com.yango.search.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-16:24
 */
@Service
@Transactional
@Slf4j
public class ArticleSearchServiceImpl implements ArticleSearchService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private ApUserSearchService apUserSearchService;

    @Override
    public ResponseResult search(UserSearchDto dto){
        //1.check params
        if (dto == null || StringUtils.isBlank(dto.getSearchWords())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUser user = AppThreadLocalUtil.getUser();
        if (user != null && dto.getFromIndex() == 0) {
            apUserSearchService.insert(dto.getSearchWords(), user.getId());
        }

        //2.set search condition
        List<Map> list = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest("app_info_article");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //2.0 分词之后在查询
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(dto.getSearchWords()).field("title").field("content").defaultOperator(Operator.OR);
            boolQueryBuilder.must(queryStringQueryBuilder);
            //2.1 < mindate's data
            RangeQueryBuilder publishTime = QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime().getTime());
            boolQueryBuilder.filter(publishTime);
            //2.2 page
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(dto.getPageSize());
            //2.3 orderByPublishtime
            searchSourceBuilder.sort("publishTime", SortOrder.DESC);
            //2.4 highlight title
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title");
            highlightBuilder.preTags("<font style='color: red; font-size: inherit;'>");
            highlightBuilder.postTags("</font>");
            searchSourceBuilder.highlighter(highlightBuilder);

            searchSourceBuilder.query(boolQueryBuilder);
            searchRequest.source(searchSourceBuilder);
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //3.return
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                String json = hit.getSourceAsString();
                Map map = JSON.parseObject(json, Map.class);
                if (hit.getHighlightFields() != null && hit.getHighlightFields().size() > 0){
                    Text[] titles = hit.getHighlightFields().get("title").getFragments();
                    String title = StringUtils.join(titles);
                    map.put("h_title",title);
                }else{
                    map.put("h_title",map.get("title"));
                }
                list.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseResult.okResult(list);
    }
}
