package com.yango.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.WmSensitiveDto;
import com.yango.model.wemedia.pojos.WmSensitive;

/**
 * ClassName: AdSensitiveWordsService
 * Package: com.yango.admin.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-17:21
 */
public interface WmSensitiveService extends IService<WmSensitive> {
    ResponseResult delById(Integer id);

    ResponseResult listAll(WmSensitiveDto dto);

    ResponseResult insert(WmSensitive wmSensitive);
    ResponseResult update(WmSensitive wmSensitive);
}
