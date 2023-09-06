package com.yango.article.controller.v1;

import com.yango.article.service.ApCollectionService;
import com.yango.model.article.dtos.CollectionBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ApCollectionController
 * Package: com.yango.article.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-17:26
 */
@RestController
@RequestMapping("/api/v1/collection_behavior")
public class ApCollectionController {
    @Autowired
    private ApCollectionService apCollectionService;

    @PostMapping
    public ResponseResult collection(@RequestBody CollectionBehaviorDto dto){
        return apCollectionService.collection(dto);
    }
}
