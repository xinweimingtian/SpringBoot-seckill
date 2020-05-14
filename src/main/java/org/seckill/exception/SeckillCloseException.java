package org.seckill.exception;

/**
 * @Description SeckillCloseException  秒杀异常关闭
 * @Author weihuiming
 * @Date 2020/5/14 9:48 2020
 */
public class SeckillCloseException extends SeckillException{

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
