package com.yango.apis.article.fallback;

import com.yango.apis.article.IArticleClient;
import com.yango.model.article.dtos.ArticleDto;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

/**
 * ClassName: IArticleClientFallback
 * Package: com.yango.apis.article.fallback
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-15:47
 */
@Component
public class IArticleClientFallback implements IArticleClient {
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取数据失败");
    }
}
