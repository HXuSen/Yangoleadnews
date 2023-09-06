package com.yango.es;

import com.alibaba.fastjson.JSON;
import com.yango.es.mapper.ApArticleMapper;
import com.yango.es.pojo.SearchArticleVo;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * ClassName: ApArticleTest
 * Package: com.yango.es
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-16:06
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApArticleTest {
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void init() throws IOException {
        //查询所有符合条件的文章数据
        List<SearchArticleVo> searchArticleVos = apArticleMapper.loadArticleList();
        //批量导入es索引库
        BulkRequest request = new BulkRequest("app_info_article");
        for (SearchArticleVo searchArticleVo : searchArticleVos) {
            IndexRequest indexRequest = new IndexRequest()
                    .id(searchArticleVo.getId().toString())
                    .source(JSON.toJSONString(searchArticleVo), XContentType.JSON);
            request.add(indexRequest);
        }
        restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
    }
}
