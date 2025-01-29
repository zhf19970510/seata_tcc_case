package com.zhf.service.impl;

import com.zhf.service.StorageService;
import com.zhf.service.TccStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class StorageServiceImpl implements StorageService {


    @Autowired
    TccStorageService tccStorageService;

    /**
     * 扣减库存
     * @param commodityCode 商品编码
     * @param count 修改库存数
     */
    @Override
    public void deduct(String commodityCode, int count){
        tccStorageService.tryDeduct(commodityCode,count);
    }





}
