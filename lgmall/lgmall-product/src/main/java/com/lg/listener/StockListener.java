package com.lg.listener;

import com.lg.feign.OrderFeign;
import com.lg.service.PmsProductService;
import com.lg.vo.LockStockVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;


@Component
public class StockListener {
    @Resource
    private OrderFeign orderFeign;
    @Resource
    private PmsProductService pmsProductService;

    @RabbitListener(queues = "stockConsumeQueue")
    public void receive(@Payload LockStockVo lockStockVo, Channel channel, Message message) throws IOException {
        try {// todo 从rabbitmq的listener里调用feign，没有请求头，该怎么解决？
            Boolean result = orderFeign.exist(lockStockVo.getOrderSn()).getData();
            if (result != null && !result) {
                //表示不存在这个订单,那么就要释放库存
                pmsProductService.releaseStock(lockStockVo);
            }
            //todo message.getMessageProperties().getDeliveryTag()为该消息的索引，
            // 第二个参数表示是否批量，true表示一次性批量ack处理小于tag的消息，false表示只处理当前消息
            //成功确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (Exception e) {
            e.printStackTrace();
            //todo 失败确认，前面两个参数作用和上面一致，最后一个参数表示是否requeue：true表示重回队列，false表示从队列删除
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
