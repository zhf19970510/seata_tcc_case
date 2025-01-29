package com.zhf.service.impl;

import com.zhf.service.AccountService;
import com.zhf.service.TccAccountService;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceImpl implements AccountService {


    @Autowired
    private TccAccountService tccAccountService;

    /**
     * 扣减用户金额
     * @param userId    用户id
     * @param money 金额
     */
    @Override
    public void debit(String userId, int money){
        tccAccountService.tryDebit(userId,money);
    }
}
