package com.yango.model.wemedia.dtos;

import com.yango.model.common.dtos.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: WmChannelDto
 * Package: com.yango.model.wemedia.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-18:26
 */
@Data
public class WmChannelDto extends PageRequestDto {
    @ApiModelProperty(value = "频道名称")
    private String name;
}
