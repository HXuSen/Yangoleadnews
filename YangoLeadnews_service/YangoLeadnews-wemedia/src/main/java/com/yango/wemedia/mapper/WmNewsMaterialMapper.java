package com.yango.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yango.model.wemedia.pojos.WmNews;
import com.yango.model.wemedia.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: WmNewsMaterialMapper
 * Package: com.yango.wemedia.mapper
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-14:43
 */
@Mapper
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {
    void saveRelations(@Param("materialIds")List<Integer> materialIds,@Param("newsId") Integer newsId,@Param("type") Short type);
}
