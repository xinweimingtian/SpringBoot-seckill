package org.seckill.dao.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

/**
 * @Description RedisDao
 * @Author weihuiming
 * @Date 2020/5/14 17:01 2020
 */
@Component
public class RedisDao {

    @Autowired
    private RedisTemplate redisTemplate;

    // 设置redis 缓存的key开头部分
    private final String key = "secKey";

    // 商品总数
    private final String order = "orderCount";

    // 下单总数
    private final String place = "orderPlace";

    /** 使用lua脚本判断 如果下单数量orderPlace + 1 小于 商品总数orderCount  然后下单数加1 **/
    private static final String GET_LUA_SCRIPT = "local orderPlace = tonumber(redis.call('get', KEYS[1]) or 0)  " +
            "local orderPlaces = orderPlace + 1 " +
            "local orderCount = tonumber(redis.call('get', KEYS[2]) or 0) " +
            "if  orderPlaces > orderCount then " +
            "return -1 " +
            "else " +
            "redis.call('set', KEYS[1], orderPlaces)" +
            "return 3 end";

    private static final String PUT_LUA_SCRIPT = "local orderPlace = tonumber(redis.call('get', KEYS[1]) or 0) redis.call('set', KEYS[1], orderPlace-1) return 3";


    public String setSeckillBySeckillId(Long seckillId, long stockCount) {
        redisTemplate.opsForValue().set(key + seckillId + place, 0);
        redisTemplate.opsForValue().set(key + seckillId + order, stockCount);
        return null;
    }

    public Integer getSeckillBySeckillId(Long seckillId) {
        return  (Integer)redisTemplate.opsForValue().get(key + seckillId + order);
    }

    /**
     * 使用lua脚本redis预下单
     * @param seckillId 商品对象id
     * @return -1 下单失败  3 下单成功
     * @throws Exception
     */
    public Long getLUASeckill(Long seckillId) throws Exception{
        List<String> list = new ArrayList<>();
        list.add(key + seckillId + place);
        list.add(key + seckillId + order);
        Long status = carriedLuaScript(GET_LUA_SCRIPT, list);
        return status;
    }

    /**
     * 下单失败 redis 下单数减1
     * @param seckillId
     * @return true  减少成功  false 减少失败
     * @throws Exception
     */
    public boolean putLUASeckill(Long seckillId) throws Exception{
        List<String> list = new ArrayList<>();
        list.add(key + seckillId + place);
        Long status = carriedLuaScript(PUT_LUA_SCRIPT, list);
        if (status == 3){
            return true;
        }
        return false;
    }


    private Long carriedLuaScript(String script, List<String> keys, Object... args) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = (Long) redisTemplate.execute(redisScript, keys, args);
        return result;
    }

}
