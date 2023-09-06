package com.yango.admin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName: AdminGatewayApplication
 * Package: com.yango.admin.gateway
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-16:23
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AdminGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminGatewayApplication.class,args);
    }
}
