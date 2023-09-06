package com.yango.behavior.service;

import com.yango.model.behavior.dtos.UnLikesBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;

/**
 * ClassName: ApUnlikesBehaviorService
 * Package: com.yango.behavior.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-16:07
 */
public interface ApUnlikesBehaviorService {
    ResponseResult unlike(UnLikesBehaviorDto dto);
}
