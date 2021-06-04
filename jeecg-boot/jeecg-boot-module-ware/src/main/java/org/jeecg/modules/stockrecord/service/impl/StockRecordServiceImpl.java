package org.jeecg.modules.stockrecord.service.impl;

import org.jeecg.modules.stockrecord.entity.StockRecord;
import org.jeecg.modules.stockrecord.mapper.StockRecordMapper;
import org.jeecg.modules.stockrecord.service.IStockRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 盘点记录
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Service
public class StockRecordServiceImpl extends ServiceImpl<StockRecordMapper, StockRecord> implements IStockRecordService {

}
