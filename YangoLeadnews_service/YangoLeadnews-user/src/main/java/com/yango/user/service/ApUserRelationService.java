package com.yango.user.service;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.user.dtos.UserRelationDto;

/**
 * ClassName: ApUserRelationService
 * Package: com.yango.user.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-16:38
 */
public interface ApUserRelationService {
    ResponseResult follow(UserRelationDto dto);
}
