package com.yango.model.wemedia.vo;

import com.yango.model.wemedia.pojos.WmNews;
import lombok.Data;

/**
 * ClassName: WmNewsVo
 * Package: com.yango.model.wemedia.vo
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-13:53
 */
@Data
public class WmNewsVo extends WmNews {
    private String authorName;
}
