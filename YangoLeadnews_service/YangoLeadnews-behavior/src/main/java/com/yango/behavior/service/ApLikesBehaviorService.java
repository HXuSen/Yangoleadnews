package com.yango.behavior.service;

import com.yango.model.behavior.dtos.LikesBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;

/**
 * ClassName: ApLikesBehaviorService
 * Package: com.yango.behavior.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-14:41
 */
public interface ApLikesBehaviorService {
    ResponseResult like(LikesBehaviorDto dto);
}
