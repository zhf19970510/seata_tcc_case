package com.zhf.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * 通过@LocalTCC这个注解，RM初始化时候会向TC注册一个分支事务
 */
@LocalTCC
public interface TccStorageService {

    /**
     * Try： 库存 - 扣减数量， 冻结库存 + 扣减数量
     */
    @TwoPhaseBusinessAction(name = "deduct" ,commitMethod = "commit" ,rollbackMethod = "rollback",useTCCFence = true)
    void tryDeduct(@BusinessActionContextParameter(paramName = "commodityCode") String commodityCode,
                   @BusinessActionContextParameter(paramName = "count") int count);

    /**
     * Confirm:冻结库存-扣减数量
     */
    boolean commit(BusinessActionContext actionContext);


    /**
     *
     * CanceL:库存+扣减数量，冻结库存-扣减数量
     */
    boolean rollback(BusinessActionContext actionContext);


}