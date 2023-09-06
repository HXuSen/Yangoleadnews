package com.yango.search.service;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.search.dtos.HistorySearchDto;

/**
 * ClassName: ApUserSearchService
 * Package: com.yango.search.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-20:07
 */
public interface ApUserSearchService {

    void insert(String keyword,Integer userId);

    ResponseResult findUserSearch();

    ResponseResult delUserSearch(HistorySearchDto dto);
}
