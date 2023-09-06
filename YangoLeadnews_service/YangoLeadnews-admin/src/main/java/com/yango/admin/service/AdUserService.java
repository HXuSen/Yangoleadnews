package com.yango.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.admin.dtos.AdUserDto;
import com.yango.model.admin.pojos.AdUser;
import com.yango.model.common.dtos.ResponseResult;

/**
 * ClassName: AdUserService
 * Package: com.yango.admin.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-16:08
 */
public interface AdUserService extends IService<AdUser> {
    ResponseResult login(AdUserDto dto);
}
