package com.yango.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.user.dtos.AuthDto;
import com.yango.model.user.pojos.ApUserRealname;

/**
 * ClassName: ApUserRealnameService
 * Package: com.yango.user.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-20:09
 */
public interface ApUserRealnameService extends IService<ApUserRealname> {
    ResponseResult loadListByStatus(AuthDto dto);

    ResponseResult updateStatus(AuthDto dto, Short status);
}
