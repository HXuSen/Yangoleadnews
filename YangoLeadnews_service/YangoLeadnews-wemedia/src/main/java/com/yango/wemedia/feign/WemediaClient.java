package com.yango.wemedia.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yango.apis.wemedia.IWemediaClient;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.wemedia.pojos.WmSensitive;
import com.yango.model.wemedia.pojos.WmUser;
import com.yango.wemedia.mapper.WmSensitiveMapper;
import com.yango.wemedia.service.WmChannelService;
import com.yango.wemedia.service.WmSensitiveService;
import com.yango.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: WemediaClient
 * Package: com.yango.wemedia.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-20:30
 */
@RestController
public class WemediaClient implements IWemediaClient {
    @Autowired
    private WmUserService wmUserService;
    @Autowired
    private WmChannelService wmChannelService;
    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    @Override
    @GetMapping("/api/v1/user/findByName/{name}")
    public WmUser findWmUserByName(@PathVariable("name") String name) {
        return wmUserService.getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName,name));
    }

    @Override
    @PostMapping("/api/v1/wm_user/save")
    public ResponseResult saveWmUser(WmUser wmUser) {
        wmUserService.save(wmUser);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    @GetMapping("/api/v1/channel/list")
    public ResponseResult getChannels() {
        return wmChannelService.findAll();
    }

    @Override
    @GetMapping("/api/v1/allSensitives")
    public ResponseResult getAllSensitive() {
        //获取所有敏感词
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(Wrappers.<WmSensitive>lambdaQuery().select(WmSensitive::getSensitives));
        List<String> sensitiveList = wmSensitives.stream()
                .map(WmSensitive::getSensitives)
                .collect(Collectors.toList());
        return ResponseResult.okResult(sensitiveList);
    }

}
