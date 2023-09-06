package com.yango.behavior;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName: BehaviorApplication
 * Package: com.yango.behavior
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-14:38
 */
@SpringBootApplication
@EnableDiscoveryClient
public class BehaviorApplication {
    public static void main(String[] args) {
        SpringApplication.run(BehaviorApplication.class,args);
    }
}
