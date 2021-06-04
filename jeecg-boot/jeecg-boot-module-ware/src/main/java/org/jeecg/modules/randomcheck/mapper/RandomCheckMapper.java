package org.jeecg.modules.randomcheck.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.randomcheck.entity.RandomCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 随机盘点
 * @Author: jeecg-boot
 * @Date:   2021-04-14
 * @Version: V1.0
 */
public interface RandomCheckMapper extends BaseMapper<RandomCheck> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
