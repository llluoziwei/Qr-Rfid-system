package org.jeecg.modules.plForm.controller;

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
import org.jeecg.modules.plForm.entity.PlForm;
import org.jeecg.modules.plForm.service.IPlFormService;

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
 * @Description: 盈亏表
 * @Author: jeecg-boot
 * @Date:   2021-04-23
 * @Version: V1.0
 */
@Api(tags="盈亏表")
@RestController
@RequestMapping("/plForm/plForm")
@Slf4j
public class PlFormController extends JeecgController<PlForm, IPlFormService> {
	@Autowired
	private IPlFormService plFormService;
	
	/**
	 * 分页列表查询
	 *
	 * @param plForm
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "盈亏表-分页列表查询")
	@ApiOperation(value="盈亏表-分页列表查询", notes="盈亏表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PlForm plForm,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PlForm> queryWrapper = QueryGenerator.initQueryWrapper(plForm, req.getParameterMap());
		Page<PlForm> page = new Page<PlForm>(pageNo, pageSize);
		IPage<PlForm> pageList = plFormService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param plForm
	 * @return
	 */
	@AutoLog(value = "盈亏表-添加")
	@ApiOperation(value="盈亏表-添加", notes="盈亏表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PlForm plForm) {
		plFormService.save(plForm);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param plForm
	 * @return
	 */
	@AutoLog(value = "盈亏表-编辑")
	@ApiOperation(value="盈亏表-编辑", notes="盈亏表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PlForm plForm) {
		plFormService.updateById(plForm);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "盈亏表-通过id删除")
	@ApiOperation(value="盈亏表-通过id删除", notes="盈亏表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		plFormService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "盈亏表-批量删除")
	@ApiOperation(value="盈亏表-批量删除", notes="盈亏表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.plFormService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "盈亏表-通过id查询")
	@ApiOperation(value="盈亏表-通过id查询", notes="盈亏表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PlForm plForm = plFormService.getById(id);
		if(plForm==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(plForm);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param plForm
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PlForm plForm) {
        return super.exportXls(request, plForm, PlForm.class, "盈亏表");
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
        return super.importExcel(request, response, PlForm.class);
    }

}
