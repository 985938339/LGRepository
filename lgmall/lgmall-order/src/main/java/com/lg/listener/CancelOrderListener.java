package com.lg.listener;

import com.lg.entity.OmsOrder;
import com.lg.feign.CartFeign;
import com.lg.feign.ProductFeign;
import com.lg.service.OmsOrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@RabbitListener(queues = "orderConsumeQueue")
@Component
public class CancelOrderListener {
    @Resource
    private OmsOrderService omsOrderService;
    @Resource
    private ProductFeign productFeign;

    @RabbitHandler
    public void receive(OmsOrder order, Channel channel, Message message) throws IOException {
        try {
            omsOrderService.closeOrder(order);
            //todo message.getMessageProperties().getDeliveryTag()为该消息的索引，
            // 第二个参数表示是否批量，true表示一次性批量ack处理小于tag的消息，false表示只处理当前消息
            //成功确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (Exception e){
            e.printStackTrace();
            //todo 失败确认，前面两个参数作用和上面一致，最后一个参数表示是否requeue：true表示重回队列，false表示从队列删除
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
        }
    }
}
