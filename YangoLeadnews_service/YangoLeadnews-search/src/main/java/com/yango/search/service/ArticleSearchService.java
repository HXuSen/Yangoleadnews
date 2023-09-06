package com.yango.search.service;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.search.dtos.UserSearchDto;


/**
 * ClassName: ArticleSearchService
 * Package: com.yango.search.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-16:22
 */

public interface ArticleSearchService {

    ResponseResult search(UserSearchDto dto);
}
