package org.jeecg.modules.wareenter2.service;

import org.jeecg.modules.wareenter2.entity.WareEnter2;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 入库申请2
 * @Author: jeecg-boot
 * @Date:   2021-04-09
 * @Version: V1.0
 */
public interface IWareEnter2Service extends IService<WareEnter2> {

    WareEnter2 queryByCreateTime();
}
