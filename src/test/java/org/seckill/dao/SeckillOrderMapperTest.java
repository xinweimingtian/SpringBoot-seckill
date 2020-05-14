package org.seckill.dao;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description SeckillOrderMapperTest
 * @Author Administrator
 * @Date 2020/5/13 23:30 2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SeckillOrderMapperTest {

    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Test
    public void insertOrder() {
        Integer i = seckillOrderDao.insertSuccessKilled(1l, BigDecimal.valueOf(120.00), 12247047);
        System.out.println(i);
    }

    @Test
    public void findById() {
        SeckillOrder seckillOrder = seckillOrderDao.queryByIdWithSeckill(1l, 12247047);
        System.out.println(seckillOrder.getSeckillId() + ": " + seckillOrder.getSeckill().getTitle());
    }

}
