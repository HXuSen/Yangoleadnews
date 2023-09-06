package com.yango.wemedia.controller.v1;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.WmSensitiveDto;
import com.yango.model.wemedia.pojos.WmSensitive;
import com.yango.wemedia.service.WmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: SensitiveWordsController
 * Package: com.yango.admin.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-17:19
 */
@RestController
@RequestMapping("/api/v1/sensitive")
public class WmSensitiveController {
    @Autowired
    private WmSensitiveService wmSensitiveService;

    @DeleteMapping("/del/{id}")
    public ResponseResult delWords(@PathVariable("id") Integer id){
        return wmSensitiveService.delById(id);
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmSensitiveDto dto){
        return wmSensitiveService.listAll(dto);
    }

    @PostMapping("/save")
    public ResponseResult insert(@RequestBody WmSensitive wmSensitive){
        return wmSensitiveService.insert(wmSensitive);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody WmSensitive wmSensitive){
        return wmSensitiveService.update(wmSensitive);
    }
}
