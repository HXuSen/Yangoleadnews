package com.yango.behavior.controller.v1;

import com.yango.behavior.service.ApLikesBehaviorService;
import com.yango.behavior.service.ApReadBehaviorService;
import com.yango.model.behavior.dtos.LikesBehaviorDto;
import com.yango.model.behavior.dtos.ReadBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ApReadBehaviorController
 * Package: com.yango.behavior.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-15:52
 */
@RestController
@RequestMapping("/api/v1/read_behavior")
public class ApReadBehaviorController {
    @Autowired
    private ApReadBehaviorService apReadBehaviorService;

    @PostMapping
    public ResponseResult readBehavior(@RequestBody ReadBehaviorDto dto){
        return apReadBehaviorService.readBehavior(dto);
    }
}
