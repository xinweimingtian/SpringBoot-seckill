package org.seckill.enums;

/**
 * @Description SeckillStatEnum  秒杀返回状态
 * @Author weihuiming
 * @Date 2020/5/14 9:37 2020
 */
public enum SeckillStatEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3, "数据篡改");

    private int status;
    private String stateInfo;

    SeckillStatEnum(int status, String stateInfo) {
        this.status = status;
        this.stateInfo = stateInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public static SeckillStatEnum valueOf(int status) {
        for (SeckillStatEnum statEnum : values()) {
            if (statEnum.getStatus() == status) {
                return statEnum;
            }
        }
        return null;
    }


}
