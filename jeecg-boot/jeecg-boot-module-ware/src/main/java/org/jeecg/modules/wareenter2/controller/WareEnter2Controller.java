package org.jeecg.modules.wareenter2.controller;

import java.util.Arrays;
import java.util.Date;
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
import org.jeecg.modules.wareenter2.entity.WareEnter2;
import org.jeecg.modules.wareenter2.service.IWareEnter2Service;

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
 * @Description: 入库申请2
 * @Author: jeecg-boot
 * @Date:   2021-04-09
 * @Version: V1.0
 */
@Api(tags="入库申请2")
@RestController
@RequestMapping("/wareenter2/wareEnter2")
@Slf4j
public class WareEnter2Controller extends JeecgController<WareEnter2, IWareEnter2Service> {
	@Autowired
	private IWareEnter2Service wareEnter2Service;
	
	/**
	 * 分页列表查询
	 *
	 * @param wareEnter2
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "入库申请2-分页列表查询")
	@ApiOperation(value="入库申请2-分页列表查询", notes="入库申请2-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WareEnter2 wareEnter2,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WareEnter2> queryWrapper = QueryGenerator.initQueryWrapper(wareEnter2, req.getParameterMap());
		Page<WareEnter2> page = new Page<WareEnter2>(pageNo, pageSize);
		IPage<WareEnter2> pageList = wareEnter2Service.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 /**
	  * 根据当前时间获取入库记录
	  *
	  * @param wareEnter2
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "入库申请2-分页列表查询")
	 @ApiOperation(value="入库申请2-分页列表查询", notes="入库申请2-分页列表查询")
	 @GetMapping(value = "/queryByCreateTime")
	 public Result<?> queryByCreateTime(WareEnter2 wareEnter2,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {

		 WareEnter2 wareEnter = wareEnter2Service.queryByCreateTime();
		 return Result.OK(wareEnter);
	 }
	
	/**
	 *   添加
	 *
	 * @param wareEnter2
	 * @return
	 */
	@AutoLog(value = "入库申请2-添加")
	@ApiOperation(value="入库申请2-添加", notes="入库申请2-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WareEnter2 wareEnter2) {
		System.out.println(wareEnter2);

		wareEnter2Service.save(wareEnter2);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wareEnter2
	 * @return
	 */
	@AutoLog(value = "入库申请2-编辑")
	@ApiOperation(value="入库申请2-编辑", notes="入库申请2-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WareEnter2 wareEnter2) {
		wareEnter2Service.updateById(wareEnter2);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "入库申请2-通过id删除")
	@ApiOperation(value="入库申请2-通过id删除", notes="入库申请2-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wareEnter2Service.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "入库申请2-批量删除")
	@ApiOperation(value="入库申请2-批量删除", notes="入库申请2-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wareEnter2Service.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "入库申请2-通过id查询")
	@ApiOperation(value="入库申请2-通过id查询", notes="入库申请2-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WareEnter2 wareEnter2 = wareEnter2Service.getById(id);
		if(wareEnter2==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wareEnter2);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wareEnter2
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WareEnter2 wareEnter2) {
        return super.exportXls(request, wareEnter2, WareEnter2.class, "入库申请2");
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
        return super.importExcel(request, response, WareEnter2.class);
    }

}
