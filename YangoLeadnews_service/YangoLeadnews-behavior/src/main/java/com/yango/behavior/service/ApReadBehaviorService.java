package com.yango.behavior.service;

import com.yango.model.behavior.dtos.ReadBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;

/**
 * ClassName: ApReadBehaviorService
 * Package: com.yango.behavior.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-15:54
 */
public interface ApReadBehaviorService {
    ResponseResult readBehavior(ReadBehaviorDto dto);
}
