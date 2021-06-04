package org.jeecg.modules.randomcheck.controller;

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
import org.jeecg.modules.randomcheck.entity.RandomCheck;
import org.jeecg.modules.randomcheck.service.IRandomCheckService;

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
 * @Description: 随机盘点
 * @Author: jeecg-boot
 * @Date:   2021-04-14
 * @Version: V1.0
 */
@Api(tags="随机盘点")
@RestController
@RequestMapping("/randomcheck/randomCheck")
@Slf4j
public class RandomCheckController extends JeecgController<RandomCheck, IRandomCheckService>{
	@Autowired
	private IRandomCheckService randomCheckService;
	
	/**
	 * 分页列表查询
	 *
	 * @param randomCheck
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 *
	 * @return
	 */
	@AutoLog(value = "随机盘点-分页列表查询")
	@ApiOperation(value="随机盘点-分页列表查询", notes="随机盘点-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(RandomCheck randomCheck,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
        if(hasQuery != null && "true".equals(hasQuery)){
            QueryWrapper<RandomCheck> queryWrapper =  QueryGenerator.initQueryWrapper(randomCheck, req.getParameterMap());
            List<RandomCheck> list = randomCheckService.queryTreeListNoPage(queryWrapper);
            IPage<RandomCheck> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        }else{
            String parentId = randomCheck.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            randomCheck.setPid(null);
            QueryWrapper<RandomCheck> queryWrapper = QueryGenerator.initQueryWrapper(randomCheck, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<RandomCheck> page = new Page<RandomCheck>(pageNo, pageSize);
            IPage<RandomCheck> pageList = randomCheckService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
	}

	 /**
      * 获取子数据
      * @param randomCheck
      * @param req
      * @return
      */
	@AutoLog(value = "随机盘点-获取子数据")
	@ApiOperation(value="随机盘点-获取子数据", notes="随机盘点-获取子数据")
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(RandomCheck randomCheck,HttpServletRequest req) {
		QueryWrapper<RandomCheck> queryWrapper = QueryGenerator.initQueryWrapper(randomCheck, req.getParameterMap());
		List<RandomCheck> list = randomCheckService.list(queryWrapper);
		IPage<RandomCheck> pageList = new Page<>(1, 10, list.size());
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
	@AutoLog(value = "随机盘点-批量获取子数据")
    @ApiOperation(value="随机盘点-批量获取子数据", notes="随机盘点-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<RandomCheck> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<RandomCheck> list = randomCheckService.list(queryWrapper);
            IPage<RandomCheck> pageList = new Page<>(1, 10, list.size());
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
	 * @param randomCheck
	 * @return
	 */
	@AutoLog(value = "随机盘点-添加")
	@ApiOperation(value="随机盘点-添加", notes="随机盘点-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RandomCheck randomCheck) {
		randomCheckService.addRandomCheck(randomCheck);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param randomCheck
	 * @return
	 */
	@AutoLog(value = "随机盘点-编辑")
	@ApiOperation(value="随机盘点-编辑", notes="随机盘点-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RandomCheck randomCheck) {
		randomCheckService.updateRandomCheck(randomCheck);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "随机盘点-通过id删除")
	@ApiOperation(value="随机盘点-通过id删除", notes="随机盘点-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		randomCheckService.deleteRandomCheck(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "随机盘点-批量删除")
	@ApiOperation(value="随机盘点-批量删除", notes="随机盘点-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.randomCheckService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "随机盘点-通过id查询")
	@ApiOperation(value="随机盘点-通过id查询", notes="随机盘点-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RandomCheck randomCheck = randomCheckService.getById(id);
		if(randomCheck==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(randomCheck);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param randomCheck
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RandomCheck randomCheck) {
		return super.exportXls(request, randomCheck, RandomCheck.class, "随机盘点");
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
		return super.importExcel(request, response, RandomCheck.class);
    }

}
