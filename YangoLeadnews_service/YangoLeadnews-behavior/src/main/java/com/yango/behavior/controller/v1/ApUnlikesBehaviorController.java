package com.yango.behavior.controller.v1;

import com.yango.behavior.service.ApLikesBehaviorService;
import com.yango.behavior.service.ApUnlikesBehaviorService;
import com.yango.model.behavior.dtos.LikesBehaviorDto;
import com.yango.model.behavior.dtos.UnLikesBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ApUnlikesBehaviorController
 * Package: com.yango.behavior.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-16:06
 */
@RestController
@RequestMapping("/api/v1/un_likes_behavior")
public class ApUnlikesBehaviorController {
    @Autowired
    private ApUnlikesBehaviorService apUnlikesBehaviorService;

    @PostMapping
    public ResponseResult unlike(@RequestBody UnLikesBehaviorDto dto){
        return apUnlikesBehaviorService.unlike(dto);
    }
}
