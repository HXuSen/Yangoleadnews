package com.yango.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.yango.behavior.service.ApLikesBehaviorService;
import com.yango.common.constants.BehaviorConstants;
import com.yango.common.constants.HotArticleConstants;
import com.yango.common.redis.CacheService;
import com.yango.model.behavior.dtos.LikesBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.message.UpdateArticleMess;
import com.yango.model.user.pojos.ApUser;
import com.yango.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName: ApLikesBehaviorServiceImpl
 * Package: com.yango.behavior.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-14:42
 */
@Service
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult like(LikesBehaviorDto dto) {
        if (dto == null || dto.getArticleId() == null || checkParam(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        UpdateArticleMess mess = new UpdateArticleMess();
        mess.setArticleId(dto.getArticleId());
        mess.setType(UpdateArticleMess.UpdateArticleType.LIKES);
        if (dto.getOperation() == 0){
            Object obj = cacheService.hGet(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(),
                    user.getId().toString());
            if (obj != null) {
                return ResponseResult.okResult(AppHttpCodeEnum.BEHAVIOR_LIKE_SUCCESS);
            }
            cacheService.hPut(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(),user.getId().toString(), JSON.toJSONString(dto));
            mess.setAdd(1);
        }else{
            cacheService.hDelete(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(),user.getId().toString());
            mess.setAdd(-1);
        }
        //发送消息,数据聚合
        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC,JSON.toJSONString(mess));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private boolean checkParam(LikesBehaviorDto dto) {
        if (dto.getType() > 2 || dto.getType() < 0 || dto.getOperation() > 1 || dto.getOperation() < 0){
            return true;
        }
        return false;
    }
}
