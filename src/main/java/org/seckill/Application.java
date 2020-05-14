package org.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description Application
 * @Author weihuiming
 * @Date 2020/5/11 16:27 2020
 */
@SpringBootApplication
@MapperScan("org.seckill.dao.**")
public class Application {

    //日志
    private static Logger logger =  LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        logger.info("==================== SpringBoot Start Success ======================");
    }

}
