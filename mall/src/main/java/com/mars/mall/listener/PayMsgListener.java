package com.mars.mall.listener;

import com.google.gson.Gson;
import com.mars.mall.pojo.PayInfo;
import com.mars.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: RabbitMQ 消息队列监听，用来接收从pay工程监听到的消息 (异步处理)
 * 关于PayInfo，正确姿势：pay项目提供client.jar, mall项目引入jar包
 * @author: Mars
 * @create: 2022-02-10 17:46
 **/
@Component
@RabbitListener(queues = "payNotify") //配置监听的消息队列名
@Slf4j
public class PayMsgListener {

    @Autowired
    private IOrderService orderService;

    @RabbitHandler
    public void process(String msg) {
        log.info("【接收到消息】=> {}", msg);

        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if (payInfo.getPlatformStatus().equals("SUCCESS")) {
            //修改订单里的状态 “未支付”->“已支付”
            orderService.paid(payInfo.getOrderNo());
        }
    }

}
