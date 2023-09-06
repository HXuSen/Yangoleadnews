package com.yango.model.common.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: PageRequestDto
 * Package: com.yango.model.common.dtos
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/27-14:30
 */
@Data
@Slf4j
public class PageRequestDto {
    protected Integer size;
    protected Integer page;

    public void checkParam() {
        if (this.page == null || this.page < 0) {
            setPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            setSize(10);
        }
    }
}
