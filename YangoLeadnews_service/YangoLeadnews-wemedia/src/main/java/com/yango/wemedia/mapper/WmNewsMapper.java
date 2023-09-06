package com.yango.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.model.wemedia.dtos.NewsAuthDto;
import com.yango.model.wemedia.pojos.WmNews;
import com.yango.model.wemedia.vo.WmNewsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: WmNewsMapper
 * Package: com.yango.wemedia.mapper
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-11:51
 */
@Mapper
public interface WmNewsMapper extends BaseMapper<WmNews> {
    List<WmNewsVo> findListAndPage(@Param("dto") NewsAuthDto dto);

    int findListCount(@Param("dto") NewsAuthDto dto);

}
