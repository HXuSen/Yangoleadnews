package com.yango.user.service.impl;

import com.yango.common.constants.BehaviorConstants;
import com.yango.common.redis.CacheService;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.user.dtos.UserRelationDto;
import com.yango.model.user.pojos.ApUser;
import com.yango.user.service.ApUserRelationService;
import com.yango.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName: ApUserRelationServiceImpl
 * Package: com.yango.user.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-16:38
 */
@Service
@Slf4j
public class ApUserRelationServiceImpl implements ApUserRelationService {
    @Autowired
    private CacheService cacheService;
    @Override
    public ResponseResult follow(UserRelationDto dto) {
        if (dto == null || dto.getOperation() < 0 || dto.getOperation() > 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        if (dto.getOperation() == 0){
            cacheService.zAdd(BehaviorConstants.APUSER_FOLLOW_RELATION + user.getId(),dto.getAuthorId().toString(),System.currentTimeMillis());
            cacheService.zAdd(BehaviorConstants.APUSER_FANS_RELATION + dto.getAuthorId(),user.getId().toString(),System.currentTimeMillis());
        }else{
            cacheService.zRemove(BehaviorConstants.APUSER_FOLLOW_RELATION + user.getId(),dto.getAuthorId().toString());
            cacheService.zRemove(BehaviorConstants.APUSER_FANS_RELATION + dto.getAuthorId(),user.getId().toString());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
