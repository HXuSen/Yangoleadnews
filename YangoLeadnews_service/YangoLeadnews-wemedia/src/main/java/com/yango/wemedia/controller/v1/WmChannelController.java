package com.yango.wemedia.controller.v1;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.WmChannelDto;
import com.yango.model.wemedia.pojos.WmChannel;
import com.yango.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: WmChannelController
 * Package: com.yango.wemedia.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-11:24
 */
@RestController
@RequestMapping("/api/v1/channel")
public class WmChannelController {
    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public ResponseResult findAll(){
        return wmChannelService.findAll();
    }

    @GetMapping("/del/{id}")
    public ResponseResult del(@PathVariable("id") Integer id){
        return wmChannelService.delById(id);
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmChannelDto dto){
        return wmChannelService.findByNameOrPage(dto);
    }

    @PostMapping("/save")
    public ResponseResult save(@RequestBody WmChannel channel){
        return wmChannelService.insert(channel);
    }

    @PostMapping("/update")
    public ResponseResult updateByEntity(@RequestBody WmChannel channel){
        return wmChannelService.updateByEntity(channel);
    }


}
