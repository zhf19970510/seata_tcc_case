package com.zhf.service.impl;

import com.zhf.database.tcc.entity.Storage;
import com.zhf.database.tcc.mapper.StorageMapper;
import com.zhf.service.TccStorageService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class TccStorageServiceImpl implements TccStorageService {
    @Autowired
    private StorageMapper storageMapper;
    @Override
    public void tryDeduct(String commodityCode, int count) {
        log.info("=============冻结库存=================");
        log.info("当前 XID: {}", RootContext.getXID());
        // 检查库存
        checkStock(commodityCode,count);

        log.info("开始冻结 {} 库存", commodityCode);
        Integer record = storageMapper.freezeStorage(commodityCode,count);
        log.info("开始冻结 {} 库存结果:{}", commodityCode, record > 0 ? "操作成功" : "扣减库存失败");
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        log.info("=================扣减冻结库存==================");
        String commodityCode = actionContext.getActionContext("commodityCode").toString();
        int count = (int)actionContext.getActionContext("count");
        // 扣减冻结库存
        storageMapper.reduceFreezeStorage(commodityCode,count);
        return true;
    }

    /**
     * 解除冻结库存
     * @param actionContext
     * @return
     */
    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        log.info("=================扣减冻结库存==================");
        String commodityCode = actionContext.getActionContext("commodityCode").toString();
        int count = (int)actionContext.getActionContext("count");
        // 解除冻结库存
        storageMapper.unFreezeStorage(commodityCode,count);
        return true;
    }


    private void checkStock(String commodityCode, int count){

        log.info("检查 {} 库存", commodityCode);
        Storage storage = storageMapper.findByCommodityCode(commodityCode);

        if (storage.getCount() < count) {
            log.warn("{} 库存不足，当前库存:{}", commodityCode, count);
            throw new RuntimeException("库存不足");
        }

    }
}