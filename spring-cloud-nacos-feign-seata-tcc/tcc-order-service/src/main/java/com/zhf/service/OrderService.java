package com.zhf.service;

import com.zhf.database.tcc.entity.Order;
import com.zhf.vo.OrderVo;

public interface OrderService {

    Order saveOrder(OrderVo orderVo);

}