package com.yango.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.model.schedule.pojos.Taskinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author itheima
 */
@Mapper
public interface TaskinfoMapper extends BaseMapper<Taskinfo> {

    List<Taskinfo> queryFutureTime(@Param("taskType")int type, @Param("priority")int priority, @Param("future")Date future);
}
