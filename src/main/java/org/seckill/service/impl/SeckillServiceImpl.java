package org.seckill.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SeckillOrder;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.dao.SeckillOrderDao;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * @Description SeckillServiceImpl 业务接口实现
 * @Author weihuiming
 * @Date 2020/5/14 9:54 2020
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 设置盐 进行加密
    private final String salt = "fweqr43543&*&*hjfdh";

    // 库存卖完标记
    private static ConcurrentMap<String, Boolean> productSoldOutMap = new ConcurrentHashMap<>();

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Autowired
    private RedisDao redisDao;

    @Override
    public List<Seckill> findAll() {
        return seckillDao.findAll();
    }

    @Override
    public Seckill findById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    @Transactional
    public Exposer exportSeckillUrl(long seckillId) throws SeckillException, RepeatKillException, SeckillCloseException {
        Seckill seckill = null;
        try {
            Integer value = redisDao.getSeckillBySeckillId(seckillId);
            if (value == null) {
                //说明redis缓存中没有此key对应的value
                //查询数据库，并将数据放入缓存中
                seckill = seckillDao.queryById(seckillId);
                if (seckill == null) {
                    //说明没有查询到
                    return new Exposer(false, seckillId);
                } else {
                    //查询到了，存入redis缓存中。 key:秒杀表的ID值； value:秒杀表数据
                    redisDao.setSeckillBySeckillId(seckillId, seckill.getStockCount());
                    logger.info("RedisTemplate -> 从数据库中读取并放入缓存中");
                }
            } else {
                //查询数据库
                seckill = seckillDao.queryById(seckillId);
                logger.info("RedisTemplate -> 从缓存中读取");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常，转换为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //获取系统时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转换特定字符串的过程，不可逆的算法
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }


    /**
     * 使用注解式事务方法的有优点：开发团队达成了一致约定，明确标注事务方法的编程风格
     * 使用事务控制需要注意：
     * 1.保证事务方法的执行时间尽可能短，不要穿插其他网络操作PRC/HTTP请求（可以将这些请求剥离出来）
     * 2.不是所有的方法都需要事务控制，如只有一条修改的操作、只读操作等是不需要进行事务控制的
     * <p>
     * Spring默认只对运行期异常进行事务的回滚操作，对于编译异常Spring是不进行回滚的，所以对于需要进行事务控制的方法尽可能将可能抛出的异常都转换成运行期异常
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, BigDecimal money, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }

        if (productSoldOutMap.get(seckillId + md5) != null) {
            // 库存不足
            throw new SeckillCloseException("Spike failed");
        }

        //执行秒杀逻辑：1.减库存；2.储存秒杀订单
        Date nowTime = new Date();

        try {
            // redis记录是否可以秒杀 防止超卖
            Long luaSeckill = redisDao.getLUASeckill(seckillId);
            if (luaSeckill < 0) {
                // 库存卖完 设置为true
                productSoldOutMap.put(seckillId + md5, true);
                // 库存不足
                throw new SeckillCloseException("Spike failed");
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常，转换为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
        try {
            //记录秒杀订单信息
            int insertCount = seckillOrderDao.insertSuccessKilled(seckillId, money, userPhone);
            //唯一性：seckillId,userPhone，保证一个用户只能秒杀一件商品
            if (insertCount <= 0) {
                // redis下单数减一
                redisDao.putLUASeckill(seckillId);
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // redis下单数减一
                    redisDao.putLUASeckill(seckillId);
                    // redis顶点减少  清除卖完标记
                    if (productSoldOutMap.get(seckillId + md5) != null) {
                        productSoldOutMap.remove(seckillId + md5);
                    }
                    //没有更新记录，秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功
                    SeckillOrder seckillOrder = seckillOrderDao.queryByIdWithSeckill(seckillId, userPhone);
                    //更新缓存（更新库存数量）
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, seckillOrder);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常，转换为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    //生成MD5值
    private String getMD5(Long seckillId) {
        String base = seckillId + "/dd" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
