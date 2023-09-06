package com.yango.common.exception;

import com.yango.model.common.enums.AppHttpCodeEnum;
/**
 * ClassName: CustomException
 * Package: com.yango.common.exception
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/27-14:24
 */
public class CustomException extends RuntimeException{
    private AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum){
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public AppHttpCodeEnum getAppHttpCodeEnum() {
        return appHttpCodeEnum;
    }
}
