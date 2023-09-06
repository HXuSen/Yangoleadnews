package com.yango.model.wemedia.dtos;

import com.yango.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * ClassName: WmMaterialDto
 * Package: com.yango.model.wemedia.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-11:07
 */
@Data
public class WmMaterialDto extends PageRequestDto {
    /**
     * 1:收藏 0:未收藏
     */
    private Short isCollection;
}
