package com.zhf.database.tcc.mapper;

import com.zhf.database.tcc.entity.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderMapper {
    
    /**
     * 保存订单
     * @param record
     * @return
     */
    @Insert("INSERT INTO `order_tcc` (id,user_id, commodity_code, count, status, money) VALUES (#{id},#{userId}, #{commodityCode}, #{count}, #{status}, #{money})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Order record);
    
    /**
     * 更新订单状态
     * @param id
     * @param status
     * @return
     */
    @Update("UPDATE `order_tcc` SET status = #{status} WHERE id = #{id}")
    int updateOrderStatus(@Param("id") Long id, @Param("status") int status);


}
