package org.seckill.dto;

import org.seckill.entity.Seckill;
import org.seckill.entity.SeckillOrder;
import org.seckill.enums.SeckillStatEnum;

/**
 * @Description SeckillExecution 执行秒杀后的结果
 * @Author weihuiming
 * @Date 2020/5/13 18:11 2020
 */
public class SeckillExecution {

    private Long seckillId; // 主键id

    private int state;  //执行秒杀结果状态

    private String stateInfo; //状态标识

    private SeckillOrder seckillOrder; // 秒杀成功的订单对象

    public SeckillExecution() {
    }

    public SeckillExecution(Long seckillId, SeckillStatEnum seckillStatEnum, SeckillOrder seckillOrder) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getStatus();
        this.stateInfo = seckillStatEnum.getStateInfo();
        this.seckillOrder = seckillOrder;
    }

    public SeckillExecution(Long seckillId, SeckillStatEnum seckillStatEnum) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getStatus();
        this.stateInfo = seckillStatEnum.getStateInfo();
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SeckillOrder getSeckillOrder() {
        return seckillOrder;
    }

    public void setSeckillOrder(SeckillOrder seckillOrder) {
        this.seckillOrder = seckillOrder;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" + "seckillId=" + seckillId + ", state=" + state + ", stateInfo='" + stateInfo + '\'' + ", seckillOrder=" + seckillOrder + '}';
    }
}
