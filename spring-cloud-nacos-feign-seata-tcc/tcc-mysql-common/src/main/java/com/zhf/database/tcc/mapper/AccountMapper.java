package com.zhf.database.tcc.mapper;

import com.zhf.database.tcc.entity.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;



@Repository
public interface AccountMapper {
    
    /**
     * 查询账户
     * @param userId
     * @return
     */
    @Select("select id, user_id, money from account_tcc WHERE user_id = #{userId}")
    Account selectByUserId(@Param("userId") String userId);
    
    /**
     * 扣减余额
     * @param userId 用户id
     * @param money 要扣减的金额
     * @return
     */
    @Update("update account_tcc set money =money-#{money} where user_id = #{userId}")
    int reduceBalance(@Param("userId") String userId, @Param("money") Integer money);

    /**
     * 冻结余额：余额 — 支付金额  ， 冻结资金 + 支付金额
     * @param userId
     * @param money
     * @return
     */
    @Update("update account_tcc set money =money - #{money},freeze_money = freeze_money +  #{money} where user_id = #{userId}")
    Integer freezeBalance(String userId, int money);

    /**
     * 扣除冻结金额  冻结资金 - 扣减金额
     * @param userId
     * @param money
     */
    @Update("update account_tcc set freeze_money = freeze_money -  #{money} where user_id = #{userId}")
    void reduceFreezeMoney(String userId, int money);

    /**
     * 解除冻结余额：余额 — 支付金额  ， 冻结资金 + 支付金额
     * @param userId
     * @param money
     * @return
     */
    @Update("update account_tcc set money =money + #{money},freeze_money = freeze_money -  #{money} where user_id = #{userId}")
    void unFreezeMoney(String userId, int money);
}






























