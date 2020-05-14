package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;
import org.springframework.stereotype.Component;

/**
 * @Description SeckillDao 秒杀dao
 * @Author Administrator
 * @Date 2020/5/13 0:07 2020
 */
@Component
@Mapper
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 查询秒杀商品 by id
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 查询所有秒杀商品的记录信息
     *
     * @return
     */
    List<Seckill> findAll();

    /**
     * 查询全部秒杀商品
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 使用存储过程执行秒杀
     * @param paramMap
     */
    //void killByProcedure(Map<String, Object> paramMap);

}
