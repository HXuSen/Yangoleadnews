package com.yango.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName: AdminApplication
 * Package: com.yango.admin
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-16:04
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.yango.admin.mapper")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class,args);
    }
}
