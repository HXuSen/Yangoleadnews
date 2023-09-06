package com.yango.search.controller.v1;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.search.dtos.UserSearchDto;
import com.yango.search.service.ApAssociateWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ApAssociateWordsController
 * Package: com.yango.search.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-21:02
 */
@RestController
@RequestMapping("/api/v1/associate")
public class ApAssociateWordsController {
    @Autowired
    private ApAssociateWordsService apAssociateWordsService;

    @PostMapping("/search")
    public ResponseResult search(@RequestBody UserSearchDto dto){
        return apAssociateWordsService.search(dto);
    }
}
