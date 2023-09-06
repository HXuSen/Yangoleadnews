package com.yango.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.yango.behavior.service.ApReadBehaviorService;
import com.yango.common.constants.BehaviorConstants;
import com.yango.common.constants.HotArticleConstants;
import com.yango.common.redis.CacheService;
import com.yango.model.behavior.dtos.ReadBehaviorDto;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName: ApReadBehaviorServiceImpl
 * Package: com.yango.behavior.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-15:54
 */
@Service
@Slf4j
public class ApReadBehaviorServiceImpl implements ApReadBehaviorService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Override
    public ResponseResult readBehavior(ReadBehaviorDto dto) {
        if (dto == null || dto.getArticleId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        String readBehaviorJson = (String) cacheService.hGet(BehaviorConstants.READ_BEHAVIOR + dto.getArticleId(), user.getId().toString());
        if (StringUtils.isNotBlank(readBehaviorJson)) {
            ReadBehaviorDto readBehaviorDto = JSON.parseObject(readBehaviorJson, ReadBehaviorDto.class);
            dto.setCount((short)(readBehaviorDto.getCount() + dto.getCount()));
        }
        cacheService.hPut(BehaviorConstants.READ_BEHAVIOR + dto.getArticleId(), user.getId().toString(),JSON.toJSONString(dto));

        //发送消息,数据聚合
        UpdateArticleMess mess = new UpdateArticleMess();
        mess.setArticleId(dto.getArticleId());
        mess.setType(UpdateArticleMess.UpdateArticleType.VIEWS);
        mess.setAdd(1);
        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC,JSON.toJSONString(mess));

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
