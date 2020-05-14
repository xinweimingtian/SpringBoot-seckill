# SpringBoot-seckill
在慕课网Java高并发秒杀API 的基础上进行升级的springboot版本  springboot + redis (lua脚本）+ Mysql的秒杀系统

后端技术 SpringBoot2.x + redis

启动说明
  1. 导入src/db 数据库文件
  2. 修改application.yml 中数据库和redis连接地址
  3. 访问 http://localhost:6066/seckill

项目说明
  1. redis中只存储了订单数和商品总数，通过lua脚本进行判断。  
        1.1 （如果订单数+1  > 商品总数 ）下单失败   
        1.2 下单成功  提前让redis中的订单数+1
  2. redis中存储的商品key都没有做过期处理  到时候请注意
  3. 如果订单入库的时候失败 会让redis中的订单数-1
  4. 本项目通过手机号来确定是否重复秒杀  实际可以通过流水号等实现 每个人每个商品有唯一的流水号
  5. 最后说明 如果真是环境 没有付款成功 记得把redis中的下单数-1
  
 慕课网视频地址：https://www.imooc.com/u/2145618/courses?sort=publish
 
欢迎star
