package org.jeecg.modules.kitcatalog2.service.impl;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.kitcatalog2.entity.KitCatalog2;
import org.jeecg.modules.kitcatalog2.mapper.KitCatalog2Mapper;
import org.jeecg.modules.kitcatalog2.service.IKitCatalog2Service;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 器材目录2
 * @Author: jeecg-boot
 * @Date:   2021-04-09
 * @Version: V1.0
 */
@Service
public class KitCatalog2ServiceImpl extends ServiceImpl<KitCatalog2Mapper, KitCatalog2> implements IKitCatalog2Service {

	@Override
	public void addKitCatalog2(KitCatalog2 kitCatalog2) {
		if(oConvertUtils.isEmpty(kitCatalog2.getPid())){
			kitCatalog2.setPid(IKitCatalog2Service.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			KitCatalog2 parent = baseMapper.selectById(kitCatalog2.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(kitCatalog2);
	}
	
	@Override
	public void updateKitCatalog2(KitCatalog2 kitCatalog2) {
		KitCatalog2 entity = this.getById(kitCatalog2.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = kitCatalog2.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				kitCatalog2.setPid(IKitCatalog2Service.ROOT_PID_VALUE);
			}
			if(!IKitCatalog2Service.ROOT_PID_VALUE.equals(kitCatalog2.getPid())) {
				baseMapper.updateTreeNodeStatus(kitCatalog2.getPid(), IKitCatalog2Service.HASCHILD);
			}
		}
		baseMapper.updateById(kitCatalog2);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteKitCatalog2(String id) throws JeecgBootException {
		//查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if(id.indexOf(",")>0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if(idVal != null){
                    KitCatalog2 kitCatalog2 = this.getById(idVal);
                    String pidVal = kitCatalog2.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<KitCatalog2> dataList = baseMapper.selectList(new QueryWrapper<KitCatalog2>().eq("pid", pidVal).notIn("id",Arrays.asList(idArr)));
                    if((dataList == null || dataList.size()==0) && !Arrays.asList(idArr).contains(pidVal)
                            && !sb.toString().contains(pidVal)){
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for(String pid : pidArr){
                this.updateOldParentNode(pid);
            }
        }else{
            KitCatalog2 kitCatalog2 = this.getById(id);
            if(kitCatalog2==null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(kitCatalog2.getPid());
            baseMapper.deleteById(id);
        }
	}
	
	@Override
    public List<KitCatalog2> queryTreeListNoPage(QueryWrapper<KitCatalog2> queryWrapper) {
        List<KitCatalog2> dataList = baseMapper.selectList(queryWrapper);
        List<KitCatalog2> mapList = new ArrayList<>();
        for(KitCatalog2 data : dataList){
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if(pidVal != null && !"0".equals(pidVal)){
                KitCatalog2 rootVal = this.getTreeRoot(pidVal);
                if(rootVal != null && !mapList.contains(rootVal)){
                    mapList.add(rootVal);
                }
            }else{
                if(!mapList.contains(data)){
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IKitCatalog2Service.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<KitCatalog2>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IKitCatalog2Service.NOCHILD);
			}
		}
	}

	/**
     * 递归查询节点的根节点
     * @param pidVal
     * @return
     */
    private KitCatalog2 getTreeRoot(String pidVal){
        KitCatalog2 data =  baseMapper.selectById(pidVal);
        if(data != null && !"0".equals(data.getPid())){
            return this.getTreeRoot(data.getPid());
        }else{
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if(pidVal != null){
                if(!sb.toString().contains(pidVal)){
                    if(sb.toString().length() > 0){
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal,sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
        List<KitCatalog2> dataList = baseMapper.selectList(new QueryWrapper<KitCatalog2>().eq("pid", pidVal));
        if(dataList != null && dataList.size()>0){
            for(KitCatalog2 tree : dataList) {
                if(!sb.toString().contains(tree.getId())){
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(),sb);
            }
        }
        return sb;
    }

}
