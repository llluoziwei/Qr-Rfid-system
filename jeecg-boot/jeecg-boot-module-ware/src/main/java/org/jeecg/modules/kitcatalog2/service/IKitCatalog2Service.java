package org.jeecg.modules.kitcatalog2.service;

import org.jeecg.modules.kitcatalog2.entity.KitCatalog2;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 器材目录2
 * @Author: jeecg-boot
 * @Date:   2021-04-09
 * @Version: V1.0
 */
public interface IKitCatalog2Service extends IService<KitCatalog2> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addKitCatalog2(KitCatalog2 kitCatalog2);
	
	/**修改节点*/
	void updateKitCatalog2(KitCatalog2 kitCatalog2) throws JeecgBootException;
	
	/**删除节点*/
	void deleteKitCatalog2(String id) throws JeecgBootException;

	/**查询所有数据，无分页*/
    List<KitCatalog2> queryTreeListNoPage(QueryWrapper<KitCatalog2> queryWrapper);

}
