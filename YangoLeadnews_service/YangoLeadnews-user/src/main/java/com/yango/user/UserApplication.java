package com.yango.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ClassName: UserApplication
 * Package: com.yango.user
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/27-16:09
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.yango.user.mapper")
@EnableFeignClients(basePackages = "com.yango.apis")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
