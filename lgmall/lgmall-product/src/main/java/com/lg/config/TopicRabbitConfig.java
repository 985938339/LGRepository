package com.lg.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 通配符模式是可以根据路由键匹配规则选择性给多个消费者发送消息的模式，一个交换机，多个消费者、多个队列。
 * 两个消费者同时绑定到不同的队列上去，两个队列通过路由键匹配规则绑定到交换机上去，生产者发送消息到交换机，
 * 交换机通过路由键匹配规则转发到不同队列，队列绑定的消费者接收并消费消息
 * 特殊匹配符号
 * *：只能匹配一个单词；  如符合A.*的有A.A  A.B  A.C
 * #：可以匹配零个或多个单词。  如符合A.#的有A.A  A.B  A.C  A.B.C
 */
@Configuration
public class TopicRabbitConfig {

    @Bean
    public MessageConverter messageConverter() {
        //定义rabbitmq的消息转化器，必须要加这样，不然消费消息不能正确转化
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange stockExchange() {
        //开启durable为true，保证交换机的持久化
        return new TopicExchange("exchange.stock", true, false);
    }

    @Bean
    public Queue stockDelayQueue() {
        //todo 设置argument
        //开启durable为true，保证队列的持久化
        Map<String, Object> arguments = new HashMap<>();
        //设置过期时间为5分钟，单位是毫秒
        arguments.put("x-message-ttl", 3000);
        arguments.put("x-dead-letter-exchange", "exchange.stock");
        arguments.put("x-dead-letter-routing-key", "stock.consume");
        return new Queue("stockDelayQueue", true, false, false, arguments);
    }

    @Bean
    public Queue stockConsumeQueue() {
        //todo 设置argument
        //开启durable为true，保证队列的持久化
        return new Queue("stockConsumeQueue", true, false, false, null);
    }

    @Bean
    public Binding stockDelayBind(TopicExchange stockExchange, Queue stockDelayQueue) {
        return BindingBuilder.bind(stockDelayQueue).to(stockExchange).with("stock.delay");
    }


    @Bean
    public Binding stockConsumeBind(TopicExchange stockExchange, Queue stockConsumeQueue) {
        return BindingBuilder.bind(stockConsumeQueue).to(stockExchange).with("stock.consume");
    }

}
