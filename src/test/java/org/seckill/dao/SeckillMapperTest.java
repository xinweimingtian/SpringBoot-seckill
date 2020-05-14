package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description SeckillMapperTest
 * @Author Administrator
 * @Date 2020/5/13 23:16 2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SeckillMapperTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void findAll() {
        List<Seckill> all = seckillDao.findAll();
        for (Seckill seckill : all) {
            System.out.println(seckill.getTitle());
        }
    }

    @Test
    public void findById() {
        Seckill seckill = seckillDao.queryById(1l);
        System.out.println(seckill.getTitle());
    }

    @Test
    public void reduceStock() {
        int row = seckillDao.reduceNumber(1l, new Date());
        System.out.println(row);
    }

}
