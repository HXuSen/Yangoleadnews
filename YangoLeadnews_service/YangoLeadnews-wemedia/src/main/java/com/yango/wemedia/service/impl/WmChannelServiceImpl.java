package com.yango.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yango.model.common.dtos.PageResponseResult;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.wemedia.dtos.WmChannelDto;
import com.yango.model.wemedia.pojos.WmChannel;
import com.yango.model.wemedia.pojos.WmNews;
import com.yango.wemedia.mapper.WmChannelMapper;
import com.yango.wemedia.service.WmChannelService;
import com.yango.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * ClassName: WmChannelServiceImpl
 * Package: com.yango.wemedia.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-11:27
 */
@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {
    @Autowired
    private WmNewsService wmNewsService;

    /**
     * 查询所有频道
     * @return
     */
    @Override
    public ResponseResult findAll() {

        return ResponseResult.okResult(list());
    }

    @Override
    public ResponseResult delById(Integer id) {
        if (id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmChannel channel = getById(id);
        if (channel == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (channel.getStatus()){
            return ResponseResult.errorResult(AppHttpCodeEnum.CHANNEL_IS_AVAILABLE);
        }
        int count = wmNewsService.count(Wrappers.<WmNews>lambdaQuery().eq(WmNews::getChannelId, channel.getId())
                .eq(WmNews::getStatus, WmNews.Status.PUBLISHED.getCode()));
        if (count > 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.CHANNEL_IS_REFERENCE);
        }

        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult findByNameOrPage(WmChannelDto dto) {
        if (dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();

        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmChannel> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotBlank(dto.getName()),WmChannel::getName,dto.getName());
        //按状态查找,前端没有发送状态信息,但是需求中有
        //queryWrapper.eq(StringUtils.isNotBlank(dto.getStatus()),WmChannel::getStatus,dto.getStatus);
        queryWrapper.orderByDesc(WmChannel::getCreatedTime);

        page(page,queryWrapper);
        ResponseResult result = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());

        return result;
    }

    @Override
    public ResponseResult insert(WmChannel channel) {
        if (channel == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmChannel wmChannel = getOne(Wrappers.<WmChannel>lambdaQuery().eq(WmChannel::getName, channel.getName()));
        if (wmChannel != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST,"频道已存在");
        }
        channel.setCreatedTime(new Date());
        save(channel);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult updateByEntity(WmChannel channel) {
        if (channel == null || channel.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        int count = wmNewsService.count(Wrappers.<WmNews>lambdaQuery().eq(WmNews::getChannelId, channel.getId())
                .eq(WmNews::getStatus, WmNews.Status.PUBLISHED.getCode()));
        if (count > 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.CHANNEL_IS_REFERENCE);
        }
        updateById(channel);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
