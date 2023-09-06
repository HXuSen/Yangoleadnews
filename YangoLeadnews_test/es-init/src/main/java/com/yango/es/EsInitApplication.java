package com.yango.es;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: EsInitApplication
 * Package: com.yango.es
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-16:02
 */
@SpringBootApplication
@MapperScan("com.yango.es.mapper")
public class EsInitApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsInitApplication.class,args);
    }
}
