package com.yango.model.wemedia.dtos;

import com.yango.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmSensitiveDto extends PageRequestDto {

    /**
     * 敏感词名称
     */
    private String name;
}
