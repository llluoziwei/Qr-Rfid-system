package org.jeecg.modules.kitcatalog2.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.kitcatalog2.entity.KitCatalog2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 器材目录2
 * @Author: jeecg-boot
 * @Date:   2021-04-09
 * @Version: V1.0
 */
public interface KitCatalog2Mapper extends BaseMapper<KitCatalog2> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
