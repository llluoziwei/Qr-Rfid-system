package org.jeecg.modules.wareenter2.service.impl;

import org.jeecg.modules.wareenter2.entity.WareEnter2;
import org.jeecg.modules.wareenter2.mapper.WareEnter2Mapper;
import org.jeecg.modules.wareenter2.service.IWareEnter2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 入库申请2
 * @Author: jeecg-boot
 * @Date:   2021-04-09
 * @Version: V1.0
 */
@Service
public class WareEnter2ServiceImpl extends ServiceImpl<WareEnter2Mapper, WareEnter2> implements IWareEnter2Service {

    @Autowired
    private  WareEnter2Mapper wareEnter2Mapper;

    @Override
    public WareEnter2 queryByCreateTime() {
        return wareEnter2Mapper.queryByCreateTime();
    }
}
