package org.seckill.reids;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

/**
 * @Description RedisTemplateConfig
 * @Author weihuiming
 * @Date 2020/5/11 22:06 2020
 */
@Configuration
public class RedisTemplateConfig<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RedisTemplate redisTemplate;

    @Autowired
    public RedisTemplateConfig(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public RedisTemplate<String, ?> redisTemplate() {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        RedisSerializer<?> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(redisSerializer);
        // value的序列化采用jackson
        redisTemplate.setValueSerializer(jacksonSerializer);
        // hash的key也采用String的序列化
        redisTemplate.setHashKeySerializer(redisSerializer);
        // hash的value也采用jackson
        redisTemplate.setValueSerializer(jacksonSerializer);
        logger.info("RedisTemplate序列化配置，转化方式：" + jacksonSerializer.getClass().getName());
        return redisTemplate;
    }

}
