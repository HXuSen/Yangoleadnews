package com.yango.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.yango.behavior.service.ApUnlikesBehaviorService;
import com.yango.common.constants.BehaviorConstants;
import com.yango.common.redis.CacheService;
import com.yango.model.behavior.dtos.UnLikesBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.user.pojos.ApUser;
import com.yango.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName: ApUnlikesBehaviorServiceImpl
 * Package: com.yango.behavior.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-16:07
 */
@Service
@Slf4j
public class ApUnlikesBehaviorServiceImpl implements ApUnlikesBehaviorService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Override
    public ResponseResult unlike(UnLikesBehaviorDto dto) {
        if (dto == null || dto.getArticleId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        if (dto.getType() == 0) {
            cacheService.hPut(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId(),user.getId().toString(), JSON.toJSONString(dto));
        }else{
            cacheService.hDelete(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId(),user.getId().toString());
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
    }
}
