package com.yango.wemedia.controller.v1;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.WmMaterialDto;
import com.yango.wemedia.service.WmMaterialService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName: WmMaterialController
 * Package: com.yango.wemedia.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-10:10
 */
@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {
    @Autowired
    private WmMaterialService wmMaterialService;

    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        return wmMaterialService.uploadPicture(multipartFile);
    }

    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmMaterialDto dto){
        return wmMaterialService.findList(dto);
    }

    @GetMapping("/del_picture/{id}")
    public ResponseResult delPicture(@PathVariable("id")Integer id){
        return wmMaterialService.delPicById(id);
    }

    @GetMapping("/collect/{id}")
    public ResponseResult collectPic(@PathVariable("id")Integer id){
        return wmMaterialService.collectPicById(id);
    }

    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollectPic(@PathVariable("id")Integer id){
        return wmMaterialService.cancelCollectPicById(id);
    }
}
