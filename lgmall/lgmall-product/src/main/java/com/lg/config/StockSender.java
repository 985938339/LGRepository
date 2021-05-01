package com.lg.config;


import com.lg.vo.LockStockVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class StockSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private final RabbitTemplate rabbitTemplate;

    public StockSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());//为rabbitmq的消息转发设置json序列化器
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if (b) {
            System.out.println("投递成功");
        } else {
            //todo 投递失败的话，那么就需要进行重新投递或者记录下来
            System.out.println("投递失败");
        }
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        //todo 消息没有被路由到指定队列的话，也是要进行重新投递或者记录的
        System.out.println("消息没有被路由到队列");
    }

    public void send(LockStockVo lockStockVo) {// 给库存服务发送信息，1分钟后没有检查到订单就解锁库存
        rabbitTemplate.convertAndSend("exchange.stock", "stock.delay", lockStockVo);
    }
}
