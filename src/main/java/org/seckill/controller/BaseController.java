package org.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description BaseController
 * @Author weihuiming
 * @Date 2020/5/14 17:40 2020
 */
@Controller
public class BaseController {

    /**
     * 跳转到秒杀商品页
     *
     * @return
     */
    @RequestMapping("/seckill")
    public String seckillGoods() {
        return "redirect:seckill/list";
    }
}
