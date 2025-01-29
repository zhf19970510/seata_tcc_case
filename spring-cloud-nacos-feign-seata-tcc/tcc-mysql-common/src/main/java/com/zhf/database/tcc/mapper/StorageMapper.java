package com.zhf.database.tcc.mapper;

import com.zhf.database.tcc.entity.Storage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;



@Repository
public interface StorageMapper {
    
    /**
     * 获取库存
     * @param commodityCode 商品编号
     * @return
     */
    @Select("SELECT id,commodity_code,count FROM storage_tcc WHERE commodity_code = #{commodityCode}")
    Storage findByCommodityCode(@Param("commodityCode") String commodityCode);
    
    /**
     * 扣减库存
     * @param commodityCode 商品编号
     * @param count  要扣减的库存
     * @return
     */
    @Update("UPDATE storage_tcc SET count = count - #{count} WHERE commodity_code = #{commodityCode}")
    int reduceStorage(@Param("commodityCode") String commodityCode,@Param("count") Integer count);

    /**
     * 冻结库存  try: 库存 - 扣减库存， 冻结库存 + 扣减库存
     * @param commodityCode
     * @param count
     * @return
     */
    @Update("UPDATE storage_tcc SET count = count - #{count},freeze_count = freeze_count + #{count}  WHERE commodity_code = #{commodityCode}")
    Integer freezeStorage(String commodityCode, int count);

    /**
     * 扣减冻结库存   confirm: 冻结库存 - 扣减数量
     * @param commodityCode
     * @param count
     */
    @Update("UPDATE storage_tcc SET freeze_count = freeze_count - #{count} WHERE commodity_code = #{commodityCode}")
    void reduceFreezeStorage(String commodityCode, int count);

    /**
     * 解冻库存   rollback: 冻结库存 - 扣减数量
     * @param commodityCode
     * @param count
     */
    @Update("UPDATE storage_tcc SET count = count + #{count},freeze_count = freeze_count - #{count}  WHERE commodity_code = #{commodityCode}")
    void unFreezeStorage(String commodityCode, int count);
}
