package org.seckill.dto;

/**
 * @Description SeckillResult 封装JSON返回的结果格式
 * @Author weihuiming
 * @Date 2020/5/13 18:16 2020
 */
public class SeckillResult<T> {

    private boolean success; //返回状态

    private T data; //返回数据

    private String error; // 错误信息

    public SeckillResult() {
    }

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "SeckillResult{" + "success=" + success + ", data=" + data + ", error='" + error + '\'' + '}';
    }
}
