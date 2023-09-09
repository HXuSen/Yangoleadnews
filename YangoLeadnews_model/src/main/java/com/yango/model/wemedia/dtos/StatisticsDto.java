package com.yango.model.wemedia.dtos;

import com.yango.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * ClassName: StatisticsDto
 * Package: com.yango.model.wemedia.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/8-15:18
 */
@Data
public class StatisticsDto extends PageRequestDto {
    private String beginDate;
    private String endDate;
    private Integer wmUserId;
}
