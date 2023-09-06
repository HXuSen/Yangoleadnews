package com.yango.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.yango.article.service.ApCollectionService;
import com.yango.common.constants.BehaviorConstants;
import com.yango.common.constants.HotArticleConstants;
import com.yango.common.redis.CacheService;
import com.yango.model.article.dtos.CollectionBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.message.UpdateArticleMess;
import com.yango.model.user.pojos.ApUser;
import com.yango.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * ClassName: ApCollectionServiceImpl
 * Package: com.yango.article.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-17:26
 */
@Service
@Slf4j
public class ApCollectionServiceImpl implements ApCollectionService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public ResponseResult collection(CollectionBehaviorDto dto) {
        if (dto == null || dto.getEntryId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        String collectionJson = (String) cacheService.hGet(BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(), dto.getEntryId().toString());
        if (StringUtils.isNotBlank(collectionJson) && dto.getOperation() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST,"已收藏");
        }

        UpdateArticleMess mess = new UpdateArticleMess();
        mess.setArticleId(dto.getEntryId());
        mess.setType(UpdateArticleMess.UpdateArticleType.COLLECTION);
        if (dto.getOperation() == 0){
            cacheService.hPut(BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(),dto.getEntryId().toString(),JSON.toJSONString(dto));
            mess.setAdd(1);
        }else{
            cacheService.hDelete(BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(),dto.getEntryId().toString());
            mess.setAdd(-1);
        }

        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC,JSON.toJSONString(mess));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
