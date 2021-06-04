package org.jeecg.modules.kitcatalog2.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.kitcatalog2.entity.KitCatalog2;
import org.jeecg.modules.kitcatalog2.service.IKitCatalog2Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 器材目录2
 * @Author: jeecg-boot
 * @Date:   2021-04-09
 * @Version: V1.0
 */
@Api(tags="器材目录2")
@RestController
@RequestMapping("/kitcatalog2/kitCatalog2")
@Slf4j
public class KitCatalog2Controller extends JeecgController<KitCatalog2, IKitCatalog2Service>{
	@Autowired
	private IKitCatalog2Service kitCatalog2Service;
	
	/**
	 * 分页列表查询
	 *
	 * @param kitCatalog2
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "器材目录2-分页列表查询")
	@ApiOperation(value="器材目录2-分页列表查询", notes="器材目录2-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(KitCatalog2 kitCatalog2,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
        if(hasQuery != null && "true".equals(hasQuery)){
            QueryWrapper<KitCatalog2> queryWrapper =  QueryGenerator.initQueryWrapper(kitCatalog2, req.getParameterMap());
            List<KitCatalog2> list = kitCatalog2Service.queryTreeListNoPage(queryWrapper);
            IPage<KitCatalog2> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        }else{
            String parentId = kitCatalog2.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            kitCatalog2.setPid(null);
            QueryWrapper<KitCatalog2> queryWrapper = QueryGenerator.initQueryWrapper(kitCatalog2, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<KitCatalog2> page = new Page<KitCatalog2>(pageNo, pageSize);
            IPage<KitCatalog2> pageList = kitCatalog2Service.page(page, queryWrapper);
            return Result.OK(pageList);
        }
	}

	 /**
      * 获取子数据
      * @param kitCatalog2
      * @param req
      * @return
      */
	@AutoLog(value = "器材目录2-获取子数据")
	@ApiOperation(value="器材目录2-获取子数据", notes="器材目录2-获取子数据")
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(KitCatalog2 kitCatalog2,HttpServletRequest req) {
		QueryWrapper<KitCatalog2> queryWrapper = QueryGenerator.initQueryWrapper(kitCatalog2, req.getParameterMap());
		List<KitCatalog2> list = kitCatalog2Service.list(queryWrapper);
		IPage<KitCatalog2> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
		return Result.OK(pageList);
	}

    /**
      * 批量查询子节点
      * @param parentIds 父ID（多个采用半角逗号分割）
      * @return 返回 IPage
      * @param parentIds
      * @return
      */
	@AutoLog(value = "器材目录2-批量获取子数据")
    @ApiOperation(value="器材目录2-批量获取子数据", notes="器材目录2-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<KitCatalog2> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<KitCatalog2> list = kitCatalog2Service.list(queryWrapper);
            IPage<KitCatalog2> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }
	
	/**
	 *   添加
	 *
	 * @param kitCatalog2
	 * @return
	 */
	@AutoLog(value = "器材目录2-添加")
	@ApiOperation(value="器材目录2-添加", notes="器材目录2-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody KitCatalog2 kitCatalog2) {
		kitCatalog2Service.addKitCatalog2(kitCatalog2);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param kitCatalog2
	 * @return
	 */
	@AutoLog(value = "器材目录2-编辑")
	@ApiOperation(value="器材目录2-编辑", notes="器材目录2-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody KitCatalog2 kitCatalog2) {
		kitCatalog2Service.updateKitCatalog2(kitCatalog2);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "器材目录2-通过id删除")
	@ApiOperation(value="器材目录2-通过id删除", notes="器材目录2-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		kitCatalog2Service.deleteKitCatalog2(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "器材目录2-批量删除")
	@ApiOperation(value="器材目录2-批量删除", notes="器材目录2-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kitCatalog2Service.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "器材目录2-通过id查询")
	@ApiOperation(value="器材目录2-通过id查询", notes="器材目录2-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		KitCatalog2 kitCatalog2 = kitCatalog2Service.getById(id);
		if(kitCatalog2==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kitCatalog2);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param kitCatalog2
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, KitCatalog2 kitCatalog2) {
		return super.exportXls(request, kitCatalog2, KitCatalog2.class, "器材目录2");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, KitCatalog2.class);
    }

}
