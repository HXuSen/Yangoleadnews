package com.yango.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.WmLoginDto;
import com.yango.model.wemedia.pojos.WmUser;

/**
 * ClassName: WmUserService
 * Package: com.yango.wemedia.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-19:38
 */
public interface WmUserService extends IService<WmUser> {
    /**
     * 自媒体端登录
     * @param dto
     * @return
     */
    public ResponseResult login(WmLoginDto dto);

}
