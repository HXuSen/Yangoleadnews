package com.yango.model.user.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: LoginDto
 * Package: com.yango.model.user.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/27-16:24
 */
@Data
public class LoginDto {
    //手机号
    @ApiModelProperty(value = "手机号",required = true)
    private String phone;
    //密码
    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
