package org.jeecg.modules.plancheck.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.plancheck.entity.PlanCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 计划盘点
 * @Author: jeecg-boot
 * @Date:   2021-04-13
 * @Version: V1.0
 */
public interface PlanCheckMapper extends BaseMapper<PlanCheck> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
