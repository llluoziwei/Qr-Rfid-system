package org.jeecg.modules.waredepot.controller;

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
import org.jeecg.modules.waredepot.entity.WareDepot;
import org.jeecg.modules.waredepot.service.IWareDepotService;

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
 * @Description: 库房列表
 * @Author: jeecg-boot
 * @Date:   2021-04-10
 * @Version: V1.0
 */
@Api(tags="库房列表")
@RestController
@RequestMapping("/waredepot/wareDepot")
@Slf4j
public class WareDepotController extends JeecgController<WareDepot, IWareDepotService> {
	@Autowired
	private IWareDepotService wareDepotService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wareDepot
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "库房列表-分页列表查询")
	@ApiOperation(value="库房列表-分页列表查询", notes="库房列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WareDepot wareDepot,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WareDepot> queryWrapper = QueryGenerator.initQueryWrapper(wareDepot, req.getParameterMap());
		Page<WareDepot> page = new Page<WareDepot>(pageNo, pageSize);
		IPage<WareDepot> pageList = wareDepotService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wareDepot
	 * @return
	 */
	@AutoLog(value = "库房列表-添加")
	@ApiOperation(value="库房列表-添加", notes="库房列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WareDepot wareDepot) {
		wareDepotService.save(wareDepot);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wareDepot
	 * @return
	 */
	@AutoLog(value = "库房列表-编辑")
	@ApiOperation(value="库房列表-编辑", notes="库房列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WareDepot wareDepot) {
		wareDepotService.updateById(wareDepot);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库房列表-通过id删除")
	@ApiOperation(value="库房列表-通过id删除", notes="库房列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wareDepotService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库房列表-批量删除")
	@ApiOperation(value="库房列表-批量删除", notes="库房列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wareDepotService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库房列表-通过id查询")
	@ApiOperation(value="库房列表-通过id查询", notes="库房列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WareDepot wareDepot = wareDepotService.getById(id);
		if(wareDepot==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wareDepot);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wareDepot
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WareDepot wareDepot) {
        return super.exportXls(request, wareDepot, WareDepot.class, "库房列表");
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
        return super.importExcel(request, response, WareDepot.class);
    }

}
