package org.jeecg.modules.waregoods.entity;

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
 * @Description: 货架列表
 * @Author: jeecg-boot
 * @Date:   2021-04-10
 * @Version: V1.0
 */
@Data
@TableName("ware_goods")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ware_goods对象", description="货架列表")
public class WareGoods implements Serializable {
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
	/**货架编号*/
	@Excel(name = "货架编号", width = 15)
    @ApiModelProperty(value = "货架编号")
    private java.lang.String goodsId;
	/**库房绑定*/
	@Excel(name = "库房绑定", width = 15, dictTable = "ware_depot", dicText = "depot_name", dicCode = "id")
	@Dict(dictTable = "ware_depot", dicText = "depot_name", dicCode = "id")
    @ApiModelProperty(value = "库房绑定")
    private java.lang.String depotBind;
	/**二维码绑定*/
	@Excel(name = "二维码绑定", width = 15)
    @ApiModelProperty(value = "二维码绑定")
    private java.lang.String qrBind;
	/**RFID绑定*/
	@Excel(name = "RFID绑定", width = 15)
    @ApiModelProperty(value = "RFID绑定")
    private java.lang.String rfidBind;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date goodsTime;
}
