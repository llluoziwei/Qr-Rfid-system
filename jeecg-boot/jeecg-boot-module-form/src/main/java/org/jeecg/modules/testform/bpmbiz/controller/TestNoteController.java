package org.jeecg.modules.testform.bpmbiz.controller;

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
import org.jeecg.modules.testform.bpmbiz.entity.TestNote;
import org.jeecg.modules.testform.bpmbiz.service.ITestNoteService;

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
 * @Description: 测试请假单form
 * @Author: jeecg-boot
 * @Date:   2021-03-02
 * @Version: V1.0
 */
@Api(tags="测试请假单form")
@RestController
@RequestMapping("/bpmbiz/testNote")
@Slf4j
public class TestNoteController extends JeecgController<TestNote, ITestNoteService> {
	@Autowired
	private ITestNoteService testNoteService;
	
	/**
	 * 分页列表查询
	 *
	 * @param testNote
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试请假单form-分页列表查询")
	@ApiOperation(value="测试请假单form-分页列表查询", notes="测试请假单form-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TestNote testNote,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestNote> queryWrapper = QueryGenerator.initQueryWrapper(testNote, req.getParameterMap());
		Page<TestNote> page = new Page<TestNote>(pageNo, pageSize);
		IPage<TestNote> pageList = testNoteService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param testNote
	 * @return
	 */
	@AutoLog(value = "测试请假单form-添加")
	@ApiOperation(value="测试请假单form-添加", notes="测试请假单form-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TestNote testNote) {
		testNoteService.save(testNote);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param testNote
	 * @return
	 */
	@AutoLog(value = "测试请假单form-编辑")
	@ApiOperation(value="测试请假单form-编辑", notes="测试请假单form-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TestNote testNote) {
		testNoteService.updateById(testNote);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试请假单form-通过id删除")
	@ApiOperation(value="测试请假单form-通过id删除", notes="测试请假单form-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		testNoteService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试请假单form-批量删除")
	@ApiOperation(value="测试请假单form-批量删除", notes="测试请假单form-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testNoteService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试请假单form-通过id查询")
	@ApiOperation(value="测试请假单form-通过id查询", notes="测试请假单form-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TestNote testNote = testNoteService.getById(id);
		if(testNote==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testNote);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testNote
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestNote testNote) {
        return super.exportXls(request, testNote, TestNote.class, "测试请假单form");
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
        return super.importExcel(request, response, TestNote.class);
    }

}
