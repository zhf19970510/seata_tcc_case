package com.zhf.service.impl;


import com.zhf.database.tcc.entity.Order;
import com.zhf.feign.AccountFeignService;
import com.zhf.feign.StorageFeignService;
import com.zhf.service.OrderService;
import com.zhf.service.TccOrderService;
import com.zhf.util.SnowFlake;
import com.zhf.vo.OrderVo;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TccOrderService tccOrderService;


    @Autowired
    private AccountFeignService accountFeignService;

    @Autowired
    private StorageFeignService storageFeignService;

    @Override
    @Transactional
    @GlobalTransactional(name="createOrder")
    public Order saveOrder(OrderVo orderVo) {
        log.info("=============用户下单=================");
        log.info("当前 XID: {}", RootContext.getXID());

        //获取对应的唯一ID
        SnowFlake snowFlake = new SnowFlake(1, 2);
        Long orderId =snowFlake.nextId();
        Order order = tccOrderService.prepareSaveOrder(orderVo,orderId);
        //扣减余额   服务降级  throw
        Boolean debit= accountFeignService.debit(orderVo.getUserId(), orderVo.getMoney());
        //扣减库存
        storageFeignService.deduct(orderVo.getCommodityCode(), orderVo.getCount());

        return order;

    }


}