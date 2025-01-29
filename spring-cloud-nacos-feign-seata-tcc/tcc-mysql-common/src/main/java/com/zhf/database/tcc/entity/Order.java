package com.zhf.database.tcc.entity;

import lombok.Data;


@Data
public class Order {
    private Long id;
    
    private String userId;
    /** 商品编号 */
    private String commodityCode;
    
    private Integer count;
    
    private Integer money;
    
    private Integer status;
}
