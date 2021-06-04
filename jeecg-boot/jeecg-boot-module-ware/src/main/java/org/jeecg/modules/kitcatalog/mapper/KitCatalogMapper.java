package org.jeecg.modules.kitcatalog.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.kitcatalog.entity.KitCatalog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 器材目录
 * @Author: jeecg-boot
 * @Date:   2021-04-07
 * @Version: V1.0
 */
public interface KitCatalogMapper extends BaseMapper<KitCatalog> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
