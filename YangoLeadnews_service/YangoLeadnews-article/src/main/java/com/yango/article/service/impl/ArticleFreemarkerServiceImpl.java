package com.yango.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yango.article.service.ApArticleService;
import com.yango.article.service.ArticleFreemarkerService;
import com.yango.common.constants.ArticleConstants;
import com.yango.file.service.FileStorageService;
import com.yango.model.article.pojos.ApArticle;
import com.yango.model.search.vos.SearchArticleVo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ArticleFreemarkerServiceImpl
 * Package: com.yango.article.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-17:24
 */
@Service
@Transactional
@Slf4j
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {
    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ApArticleService apArticleService;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Override
    @Async
    public void buildArticleToMinio(ApArticle article, String content) {
        if (StringUtils.isNotBlank(content)){
            //2.生成freemarker文件
            Template template = null;
            StringWriter out = new StringWriter();;
            try {
                template = configuration.getTemplate("article.ftl");
                Map<String,Object> contentDataModel = new HashMap<>();
                contentDataModel.put("content", JSONArray.parseArray(content));
                template.process(contentDataModel,out);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //3.上传minio
            InputStream bis = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", article.getId() + ".html", bis);
            //4.修改ap_article表 更新字段static_url
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,article.getId())
                    .set(ApArticle::getStaticUrl,path));
            //发送消息 创建索引
            createArticleESIndex(article,content,path);
        }
    }

    private void createArticleESIndex(ApArticle article, String content, String path) {
        SearchArticleVo vo = new SearchArticleVo();
        BeanUtils.copyProperties(article,vo);
        vo.setContent(content);
        vo.setStaticUrl(path);
        kafkaTemplate.send(ArticleConstants.ARTICLE_ES_SYNC_TOPIC, JSON.toJSONString(vo));
    }
}
