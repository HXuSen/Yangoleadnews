package com.yango.behavior.controller.v1;

import com.yango.behavior.service.ApLikesBehaviorService;
import com.yango.model.behavior.dtos.LikesBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ApLikesBehaviorController
 * Package: com.yango.behavior.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-14:41
 */
@RestController
@RequestMapping("/api/v1/likes_behavior")
public class ApLikesBehaviorController {
    @Autowired
    private ApLikesBehaviorService apLikesBehaviorService;

    @PostMapping
    public ResponseResult like(@RequestBody LikesBehaviorDto dto){
        return apLikesBehaviorService.like(dto);
    }
}
