package com.yango.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.model.user.pojos.ApUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: ApUserMapper
 * Package: com.yango.user.mapper
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/27-16:25
 */
@Mapper
public interface ApUserMapper extends BaseMapper<ApUser> {
}
