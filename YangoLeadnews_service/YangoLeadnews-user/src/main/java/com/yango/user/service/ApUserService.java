package com.yango.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.user.dtos.LoginDto;
import com.yango.model.user.pojos.ApUser;

/**
 * ClassName: ApUserService
 * Package: com.yango.user.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/27-16:26
 */
public interface ApUserService extends IService<ApUser> {
    //APP端登录功能
    ResponseResult login(LoginDto dto);
}
