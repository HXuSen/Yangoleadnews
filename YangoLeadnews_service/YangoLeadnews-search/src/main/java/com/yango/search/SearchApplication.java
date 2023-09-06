package com.yango.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ClassName: SearchApplication
 * Package: com.yango.search
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-16:17
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class,args);
    }
}
