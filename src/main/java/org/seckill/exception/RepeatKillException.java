package org.seckill.exception;

/**
 * @Description RepeatKillException 重复执行秒杀异常（运行期异常）
 * @Author weihuiming
 * @Date 2020/5/14 9:48 2020
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
