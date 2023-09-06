package com.yango.schedule.test;

import com.yango.common.redis.CacheService;
import com.yango.schedule.ScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

/**
 * ClassName: RedisTest
 * Package: com.yango.schedule.test
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-19:19
 */
@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    private CacheService cacheService;

    @Test
    public void testList(){
        cacheService.lLeftPush("list_001","hello,redis");

        String list_001 = cacheService.lRightPop("list_001");
        System.out.println("list_001 = " + list_001);
    }

    @Test
    public void testZset(){
        //cacheService.zAdd("zset_key_001","hello zset_001",1000);
        //cacheService.zAdd("zset_key_001","hello zset_002",8888);
        //cacheService.zAdd("zset_key_001","hello zset_003",7777);
        //cacheService.zAdd("zset_key_001","hello zset_004",999999);

        Set<String> zset_key_001 = cacheService.zRangeByScore("zset_key_001", 0, 8888);
        System.out.println("zset_key_001 = " + zset_key_001);
    }
}
