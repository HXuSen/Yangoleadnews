package com.yango.kafka.listener;

import com.alibaba.fastjson.JSON;
import com.yango.kafka.pojo.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * ClassName: HelloListener
 * Package: com.yango.kafka.listener
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-14:20
 */
@Component
public class HelloListener {

    @KafkaListener(topics = "user-topic")
    public void onMessage(String msg){
        if (!StringUtils.isEmpty(msg)) {
            //System.out.println(msg);
            User user = JSON.parseObject(msg, User.class);
            System.out.println(user);
        }
    }
}
