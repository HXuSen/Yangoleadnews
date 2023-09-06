package com.yango.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.WmChannelDto;
import com.yango.model.wemedia.pojos.WmChannel;

/**
 * ClassName: WmChannelService
 * Package: com.yango.wemedia.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-11:25
 */
public interface WmChannelService extends IService<WmChannel> {
    ResponseResult findAll();

    ResponseResult delById(Integer id);

    ResponseResult findByNameOrPage(WmChannelDto dto);

    ResponseResult insert(WmChannel channel);
    ResponseResult updateByEntity(WmChannel channel);
}
