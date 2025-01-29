package com.zhf.controller;

import com.zhf.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @RequestMapping("/debit")
    public Boolean debit(String userId, int money) throws Exception {
        log.info("=============用户账户扣款 开始============");
        // 用户账户扣款
        accountService.debit(userId, money);
        log.info("=============用户账户扣款  开始============");
        return true;
    }
    
}
