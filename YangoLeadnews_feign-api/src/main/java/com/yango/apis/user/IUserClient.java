package com.yango.apis.user;

import com.yango.model.user.pojos.ApUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * ClassName: IUserClient
 * Package: com.yango.apis.user
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/6-22:28
 */
@FeignClient("leadnews-user")
public interface IUserClient {

    @GetMapping("/api/v1/user/{id}")
    ApUser findUserById(@PathVariable("id") Integer id);
}
