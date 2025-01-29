package com.zhf.service;

import io.seata.rm.tcc.api.BusinessActionContextParameter;

public interface StorageService {

    /**
     * Try： 库存 - 扣减数量， 冻结库存 + 扣减数量
     */
    void deduct( String commodityCode,
                 @BusinessActionContextParameter(paramName = "count") int count);



}
