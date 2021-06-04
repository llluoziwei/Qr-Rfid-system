package org.jeecg.modules.plancheck.service;

import org.jeecg.modules.plancheck.entity.PlanCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 计划盘点
 * @Author: jeecg-boot
 * @Date:   2021-04-13
 * @Version: V1.0
 */
public interface IPlanCheckService extends IService<PlanCheck> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addPlanCheck(PlanCheck planCheck);
	
	/**修改节点*/
	void updatePlanCheck(PlanCheck planCheck) throws JeecgBootException;
	
	/**删除节点*/
	void deletePlanCheck(String id) throws JeecgBootException;

	/**查询所有数据，无分页*/
    List<PlanCheck> queryTreeListNoPage(QueryWrapper<PlanCheck> queryWrapper);

}
