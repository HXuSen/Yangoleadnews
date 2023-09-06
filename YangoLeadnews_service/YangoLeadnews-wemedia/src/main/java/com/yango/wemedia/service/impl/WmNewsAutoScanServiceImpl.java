package com.yango.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sun.deploy.net.MessageHeader;
import com.yango.apis.article.IArticleClient;
import com.yango.common.tess4j.Tess4jClient;
import com.yango.file.service.FileStorageService;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.pojos.WmChannel;
import com.yango.model.wemedia.pojos.WmNews;
import com.yango.model.wemedia.pojos.WmSensitive;
import com.yango.model.wemedia.pojos.WmUser;
import com.yango.utils.common.SensitiveWordUtil;
import com.yango.wemedia.mapper.WmChannelMapper;
import com.yango.wemedia.mapper.WmNewsMapper;
import com.yango.wemedia.mapper.WmSensitiveMapper;
import com.yango.wemedia.mapper.WmUserMapper;
import com.yango.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: WmNewsAutoScanServiceImpl
 * Package: com.yango.wemedia.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-14:16
 */
@Service
@Transactional
@Slf4j
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {
    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Autowired
    private IArticleClient articleClient;
    @Autowired
    private WmChannelMapper wmChannelMapper;
    @Autowired
    private WmUserMapper wmUserMapper;
    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;
    @Autowired
    private Tess4jClient tess4jClient;
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    @Async
    public void autoScanWmNews(Integer id) {
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null) {
            throw new RuntimeException("WmNewsAutoScanServiceImpl-文章不存在");
        }
        if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            Map<String, Object> textAndImages = handleTextAndImages(wmNews);
            //自管理的敏感词过滤
            boolean isSensitive = handleSensitiveScan((String) textAndImages.get("content"),wmNews);
            if (!isSensitive) return;

            //TODO 阿里云接入图片与文本审核
            //自定义图片审核
            boolean isImgScan = handleImageScan((List<String>)textAndImages.get("images"),wmNews);
            if (!isImgScan) return;
            //审核成功,保存app端的相关文章数据
            ResponseResult responseResult = saveAppArticle(wmNews);
            if (!responseResult.getCode().equals(200)) {
                throw new RuntimeException("WmNewsAutoScanServiceImpl-文章审核,保存app端相关文章数据失败");
            }
            wmNews.setArticleId((Long)responseResult.getData());
            updateWmNews(wmNews,(short) 9,"审核成功");
        }
    }

    private boolean handleImageScan(List<String> images, WmNews wmNews) {
        boolean flag = true;
        if (images == null || images.size() == 0){
            return flag;
        }
        List<byte[]> imageList = new ArrayList<>();
        images = images.stream().distinct().collect(Collectors.toList());

        try {
            for (String image : images) {
                byte[] bytes = fileStorageService.downLoadFile(image);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                BufferedImage read = ImageIO.read(bais);
                //OCR
                String result = tess4jClient.doOCR(read);
                //filter
                boolean isSensitive = handleSensitiveScan(result, wmNews);
                if (!isSensitive){
                    return isSensitive;
                }
                imageList.add(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO 接入阿里云图片检测
        return flag;
    }

    private boolean handleSensitiveScan(String content, WmNews wmNews) {
        boolean flag= true;
        //获取所有敏感词
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(Wrappers.<WmSensitive>lambdaQuery().select(WmSensitive::getSensitives));
        List<String> sensitiveList = wmSensitives.stream()
                .map(WmSensitive::getSensitives)
                .collect(Collectors.toList());
        //初始化敏感词库
        SensitiveWordUtil.initMap(sensitiveList);
        //检索
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        if (map.size() > 0){
            updateWmNews(wmNews,(short)2,"当前文章存在违规内容" + map);
            flag = false;
        }

        return flag;
    }

    private void updateWmNews(WmNews wmNews, short status, String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }

    public ResponseResult saveAppArticle(WmNews wmNews) {
        ArticleDto dto = new ArticleDto();
        BeanUtils.copyProperties(wmNews,dto);
        dto.setLayout(wmNews.getType());
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if (wmChannel != null) {
            dto.setChannelName(wmChannel.getName());
        }
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if (wmUser != null) {
            dto.setAuthorName(wmUser.getName());
        }
        if (wmNews.getArticleId() != null){
            dto.setId(wmNews.getArticleId());
        }
        dto.setCreatedTime(new Date());
        return articleClient.saveArticle(dto);
    }

    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> images = new ArrayList<>();
        if (StringUtils.isNotBlank(wmNews.getContent())){
            List<Map> maps = JSONArray.parseArray(wmNews.getContent(), Map.class);
            for (Map map : maps) {
                if ("text".equals(map.get("type"))){
                    stringBuilder.append(map.get("value"));
                }
                if ("image".equals(map.get("type"))){
                    images.add((String) map.get("value"));
                }
            }
        }
        //文章封面提取
        if (StringUtils.isNotBlank(wmNews.getImages())){
            String[] covers = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(covers));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("content",stringBuilder.toString());
        result.put("images",images);
        return result;
    }
}
