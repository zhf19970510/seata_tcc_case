package com.zhf.service;

public interface AccountService {

    /**
     * 用户账户扣款
     */
    void debit(String userId,  int money);
}