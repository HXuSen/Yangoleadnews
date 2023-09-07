package com.yango.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ClassName: CommentApplication
 * Package: com.yango.comment
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/6-20:20
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.yango.apis")
public class CommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentApplication.class,args);
    }
}
