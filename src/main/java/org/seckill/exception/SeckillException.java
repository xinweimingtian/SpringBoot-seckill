package org.seckill.exception;

/**
 * @Description SeckillException 秒杀相关的异常
 * @Author weihuiming
 * @Date 2020/5/14 9:49 2020
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
