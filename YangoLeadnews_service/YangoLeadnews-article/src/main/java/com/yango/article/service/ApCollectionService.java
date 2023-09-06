package com.yango.article.service;

import com.yango.model.article.dtos.CollectionBehaviorDto;
import com.yango.model.common.dtos.ResponseResult;

/**
 * ClassName: ApCollectionService
 * Package: com.yango.article.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-17:26
 */
public interface ApCollectionService {
    ResponseResult collection(CollectionBehaviorDto dto);
}
