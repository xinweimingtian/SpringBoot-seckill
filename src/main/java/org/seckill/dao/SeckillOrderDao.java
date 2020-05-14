package org.seckill.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SeckillOrder;
import org.springframework.stereotype.Component;

/**
 * @Description SeckillOrderDao 秒杀明细dao
 * @Author Administrator
 * @Date 2020/5/13 0:08 2020
 */
@Component
@Mapper
public interface SeckillOrderDao {

    /**
     * 插入购买订单明细
     *
     * @param seckillId 秒杀到的商品ID
     * @param money     秒杀的金额
     * @param userPhone 秒杀的用户
     * @return 返回该SQL更新的记录数，如果>=1则更新成功
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("money") BigDecimal money, @Param("userPhone") long userPhone);

    /**
     * 查询秒杀成功明细 携带秒杀商品
     * @param seckillId
     * @param userPhone
     * @return
     */
    SeckillOrder queryByIdWithSeckill(@Param("seckillId")long seckillId, @Param("userPhone") long userPhone);

}
