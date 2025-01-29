package com.zhf.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "tcc-account-service",path = "/account")
public interface AccountFeignService {

    @RequestMapping("/debit")
    Boolean debit(@RequestParam("userId") String userId,@RequestParam("money") int money);
}
