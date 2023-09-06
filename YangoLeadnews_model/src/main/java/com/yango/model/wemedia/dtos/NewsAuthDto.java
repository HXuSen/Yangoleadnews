package com.yango.model.wemedia.dtos;

import com.yango.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * ClassName: NewsAuthDto
 * Package: com.yango.model.wemedia.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/2-13:43
 */
@Data
public class NewsAuthDto extends PageRequestDto {
    private Integer id;
    private String msg;
    private Integer status;
    private String title;
}
