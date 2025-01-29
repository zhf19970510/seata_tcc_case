package com.zhf.service.impl;

import com.zhf.database.tcc.entity.Order;
import com.zhf.database.tcc.entity.OrderStatus;
import com.zhf.database.tcc.mapper.OrderMapper;
import com.zhf.service.TccOrderService;
import com.zhf.vo.OrderVo;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class TccOrderServiceImpl implements TccOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Override
    public Order prepareSaveOrder(OrderVo orderVo, Long orderId) {
        // 保存订单
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(orderVo.getUserId());
        order.setCommodityCode(orderVo.getCommodityCode());
        order.setCount(orderVo.getCount());
        order.setMoney(orderVo.getMoney());
        order.setStatus(OrderStatus.INIT.getValue());

        Integer saveOrderRecord = orderMapper.insert(order);
        log.info("保存订单{}", saveOrderRecord > 0 ? "成功" : "失败");
        return order;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        // 获取订单Id
        Long orderId = (Long) actionContext.getActionContext("orderId");
        //更新订单
        Integer updateOrderRecord = orderMapper.updateOrderStatus(orderId,OrderStatus.SUCCESS.getValue());
        log.info("支付订单成功：更新订单id:{} {}", orderId, updateOrderRecord > 0 ? "成功" : "失败");    //更新订单
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        // 获取订单Id
        Long orderId = (Long) actionContext.getActionContext("orderId");
        //更新订单
        Integer updateOrderRecord = orderMapper.updateOrderStatus(orderId,OrderStatus.FAIL.getValue());
        log.info("支付订单失败：更新订单id:{} {}", orderId, updateOrderRecord > 0 ? "成功" : "失败");    //更新订单
        return true;
    }
}