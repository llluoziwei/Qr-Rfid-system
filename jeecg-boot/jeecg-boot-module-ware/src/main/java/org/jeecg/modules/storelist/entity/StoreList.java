package org.jeecg.modules.storelist.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 存放管理
 * @Author: jeecg-boot
 * @Date:   2021-04-12
 * @Version: V1.0
 */
@Data
@TableName("store_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="store_list对象", description="存放管理")
public class StoreList implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**器材编号*/
	@Excel(name = "器材编号", width = 15)
    @ApiModelProperty(value = "器材编号")
    private java.lang.String wareId;
	/**目录类型*/
    @Dict(dictTable = "kit_catalog2", dicText = "kit_name" ,dicCode = "id")
	@Excel(name = "目录类型", width = 15)
    @ApiModelProperty(value = "目录类型")
    private java.lang.String kitName;
	/**包装类型*/
	@Excel(name = "包装类型", width = 15, dicCode = "ware_type")
	@Dict(dicCode = "ware_type")
    @ApiModelProperty(value = "包装类型")
    private java.lang.String packType;
	/**器材数量*/
	@Excel(name = "器材数量", width = 15)
    @ApiModelProperty(value = "器材数量")
    private java.math.BigDecimal wareNum;
	/**库房绑定*/
	@Excel(name = "库房绑定", width = 15, dictTable = "ware_depot", dicText = "depot_name", dicCode = "id")
	@Dict(dictTable = "ware_depot", dicText = "depot_name", dicCode = "id")
    @ApiModelProperty(value = "库房绑定")
    private java.lang.String depotBind;
	/**货架绑定*/
	@Excel(name = "货架绑定", width = 15, dictTable = "ware_goods", dicText = "goods_id", dicCode = "id")
	@Dict(dictTable = "ware_goods", dicText = "goods_id", dicCode = "id")
    @ApiModelProperty(value = "货架绑定")
    private java.lang.String goodsBind;
	/**二维码绑定*/
	@Excel(name = "二维码绑定", width = 15)
    @ApiModelProperty(value = "二维码绑定")
    private java.lang.String qrBind;
	/**RFID绑定*/
	@Excel(name = "RFID绑定", width = 15)
    @ApiModelProperty(value = "RFID绑定")
    private java.lang.String rfidBind;
	/**入库时间*/
	@Excel(name = "入库时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "入库时间")
    private java.util.Date enterTime;
}
