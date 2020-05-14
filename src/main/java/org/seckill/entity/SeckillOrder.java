package org.seckill.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Description SeckillOrder 秒杀订单表
 * @Author Administrator
 * @Date 2020/5/13 0:06 2020
 */
public class SeckillOrder {

    private long seckillId; // 秒杀商品id
    private BigDecimal money; //支付金额
    private long userPhone; // 秒杀用户手机号

    private int state; //订单状态， -1:无效 0:成功 1:已付款

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime; //创建时间

    private Seckill seckill; //秒杀商品，和订单是一对多的关系

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" + "seckillId=" + seckillId + ", money=" + money + ", userPhone=" + userPhone + ", state=" + state + ", createTime=" + createTime + ", seckill=" + seckill + '}';
    }
}
