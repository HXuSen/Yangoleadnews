package com.yango.admin.controller.v1;

import com.yango.admin.service.AdUserService;
import com.yango.model.admin.dtos.AdUserDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: LoginController
 * Package: com.yango.wemedia.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-19:36
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private AdUserService adUserService;

    @PostMapping("/in")
    public ResponseResult login(@RequestBody AdUserDto dto){
        return adUserService.login(dto);
    }
}