package com.yango.user.feign;

import com.yango.apis.user.IUserClient;
import com.yango.model.user.pojos.ApUser;
import com.yango.user.mapper.ApUserMapper;
import com.yango.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: UserClient
 * Package: com.yango.user.feign
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/6-22:30
 */
@RestController
public class UserClient implements IUserClient {
    @Autowired
    private ApUserService apUserService;
    @Override
    @GetMapping("/api/v1/user/{id}")
    public ApUser findUserById(@PathVariable("id") Integer id) {
        return apUserService.getById(id);
    }
}
