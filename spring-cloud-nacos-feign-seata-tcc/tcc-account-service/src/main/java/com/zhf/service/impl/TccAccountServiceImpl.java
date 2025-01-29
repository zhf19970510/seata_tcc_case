package com.zhf.service.impl;

import com.zhf.database.tcc.entity.Account;
import com.zhf.database.tcc.mapper.AccountMapper;
import com.zhf.service.TccAccountService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TccAccountServiceImpl implements TccAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void tryDebit(String userId, int money) {
        log.info("=============冻结支付金额=================");
        log.info("当前 XID: {}", RootContext.getXID());

        checkBalance(userId, money);

        log.info("开始冻结支付金额 {} 余额", userId);
        Integer record = accountMapper.freezeBalance(userId, money);
        log.info("开始冻结支付金额 {} 冻结余额结果:{}", userId, record > 0 ? "操作成功" : "扣减余额失败");
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        log.info("=================扣减冻结金额==================");
        String userId = actionContext.getActionContext("userId").toString();
        int money = (int) actionContext.getActionContext("money");
        // 扣减冻结金额
        accountMapper.reduceFreezeMoney(userId, money);
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        log.info("=================解除冻结金额==================");
        String userId = actionContext.getActionContext("userId").toString();
        int money = (int) actionContext.getActionContext("money");
        // 解除冻结金额
        accountMapper.unFreezeMoney(userId, money);
        return true;
    }


    private void checkBalance(String userId, int money) {
        log.info("检查用户 {} 余额", userId);
        Account account = accountMapper.selectByUserId(userId);

        if (account.getMoney() < money) {
            log.warn("用户 {} 余额不足，当前余额:{}", userId, account.getMoney());
            throw new RuntimeException("余额不足");
        }
    }

}