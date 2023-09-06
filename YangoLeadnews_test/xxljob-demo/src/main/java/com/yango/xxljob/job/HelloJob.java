package com.yango.xxljob.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: HelloJob
 * Package: com.yango.xxljob.job
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/3-15:48
 */
@Component
public class HelloJob {
    @Value("${server.port}")
    private String port;

    @XxlJob("demoJobHandler")
    public void helloJob(){
        System.out.println("简单任务执行了:" + port);
    }

    @XxlJob("shardingDemo")
    public void shardingJobHandler(){
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        List<Integer> list = getList();

        for (Integer integer : list) {
            if (integer % shardTotal == shardIndex){
                System.out.println("shardIndex:" + shardIndex + ",data:" + integer);
            }
        }
    }

    private List getList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        return list;
    }
}
