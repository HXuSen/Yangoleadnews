package com.yango.article;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ClassName: ArticleApplication
 * Package: com.yango.article
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-15:09
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.yango.article.mapper")
@EnableAsync
@EnableFeignClients("com.yango.apis")
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class,args);
    }

    //@Bean
    //public MybatisPlusInterceptor mybatisPlusInterceptor(){
    //    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    //    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    //    return interceptor;
    //}
}
