package com.yango.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yango.model.article.pojos.ApArticleConfig;

import java.util.Map;

/**
 * ClassName: ApArticleConfigService
 * Package: com.yango.article.service
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-14:59
 */
public interface ApArticleConfigService extends IService<ApArticleConfig> {
    void updateByMap(Map map);
}
