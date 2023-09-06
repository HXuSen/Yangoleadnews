package com.yango.kafka.controller;

import com.alibaba.fastjson.JSON;
import com.yango.kafka.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: HelloController
 * Package: com.yango.kafka.controller
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-14:19
 */
@RestController
public class HelloController {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/hello")
    public String hello(){
        //kafkaTemplate.send("yango-topic","YangoLeadnews");
        User user = new User();
        user.setUsername("郑承宗");
        user.setAge(23);
        kafkaTemplate.send("user-topic", JSON.toJSONString(user));
        return "ok";
    }
}
