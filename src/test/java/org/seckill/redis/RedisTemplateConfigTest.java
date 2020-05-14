package org.seckill.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description RedisTemplateConfigTest
 * @Author weihuiming
 * @Date 2020/5/12 16:43 2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTemplateConfigTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisForString() {
//        redisTemplate.boundValueOps("ddd").set("ceshi");
        System.out.println(redisTemplate.opsForValue().get("seckill1orderCount").getClass());
        System.out.println(redisTemplate.opsForValue().get("seckill1orderCount") instanceof String);
//        System.out.println("*/*********" + redisTemplate.boundValueOps("ddd").get());
//        System.out.println(redisTemplate.delete("ddd"));
    }

    @Test
    public void testRedisForList() {
        List list = new ArrayList();
        list.add("测试");
        list.add("测试1");
        redisTemplate.boundHashOps("redis_for_list").put("list", list);
        System.out.println(redisTemplate.opsForHash().keys("redis_for_list"));
        System.out.println(redisTemplate.opsForHash().delete("redis_for_list", "list"));
    }

    /**
     * 释放锁lua脚本  先获取指定key的值，然后和传入的arg比较是否相等，相等值删除key，否则直接返回0。
     */
    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('get', KEYS[1]) else return 0 end";
    private static final String RELEASE_LOCK_LUA_SCRIPT1 = "local key1 = KEYS[1] local value = ARGV[1] redis.log(redis.LOG_DEBUG, key1) redis.log(redis.LOG_DEBUG, value)  return redis.call('get', key1)";
    /**
     * 使用lua脚本判断 如果下单数量orderCount + 1 小于 商品总数orders  然后下单数加1
     **/
    private static final String RELEASE_LOCK_LUA_SCRIPT2 = "local orderCount = tonumber(redis.call('get', KEYS[1]) or 0)  " + "local orders = tonumber(redis.call('get', KEYS[2]) or 0) " + "if orderCount + 1 >= orders then " + "return -1 " + "else " + "redis.call('set', KEYS[1], orderCount+1)" + "return 3 end";

    private static final String PUT_LUA_SCRIPT = "local orderCount = tonumber(redis.call('get', KEYS[1]) or 0) redis.call('set', KEYS[1], orderCount-1) return 3";

    /**
     * 测试lua脚本
     */
    @Test
    public void testLua() {
        String lockKey1 = "keu1";
        String lockKey2 = "keu2";
        boolean success1 = redisTemplate.opsForValue().setIfAbsent(lockKey1, 10, 3, TimeUnit.MINUTES);
        boolean success2 = redisTemplate.opsForValue().setIfAbsent(lockKey2, 30, 3, TimeUnit.MINUTES);
        if (!success1 || !success2) {
            System.out.println("锁已存在");
            System.out.println(redisTemplate.opsForValue().get(lockKey1));
            System.out.println(redisTemplate.opsForValue().get(lockKey2));
        }
        // 指定 lua 脚本，并且指定返回值类型
        System.out.println(RELEASE_LOCK_LUA_SCRIPT1);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(PUT_LUA_SCRIPT, Long.class);
        List<String> arrayList = new ArrayList<>();
        arrayList.add(lockKey1);
        arrayList.add(lockKey2);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = (Long) redisTemplate.execute(redisScript, arrayList, 10);
        System.out.println(result);
    }
}

