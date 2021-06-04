package org.jeecg.modules.wareenter2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.wareenter2.entity.WareEnter2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 入库申请2
 * @Author: jeecg-boot
 * @Date:   2021-04-09
 * @Version: V1.0
 */
public interface WareEnter2Mapper extends BaseMapper<WareEnter2> {

    @Select("SELECT * from ware_enter2 ORDER BY create_time desc LIMIT 1")
    WareEnter2 queryByCreateTime();

}
