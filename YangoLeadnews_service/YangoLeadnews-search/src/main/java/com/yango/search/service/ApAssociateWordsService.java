package com.yango.search.service;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.search.dtos.UserSearchDto;

/**
 * ClassName: ApAssociateWordsService
 * Package: com.yango.search.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-21:04
 */
public interface ApAssociateWordsService {
    ResponseResult search(UserSearchDto dto);
}
