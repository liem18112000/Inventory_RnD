package com.fromlabs.inventory.notificationservice.notification.message_queue;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


import static com.fromlabs.inventory.notificationservice.notification.message_queue.enums.NotificationQueue.*;

@Configuration
public class RabbitMessageQueueConfiguration {

    public static final String DIRECT_EXCHANGE  = "exchange.direct";
    public static final String MAIN_QUEUE       = "mainQueue";
    public static final String LOW_STOCK_QUEUE  = "lowStockQueue";

    @Bean(name = MAIN_QUEUE)
    @Primary
    public Queue createMainQueue() {
        return new Queue(MAIN.getName(), false);
    }

    @Bean(name = LOW_STOCK_QUEUE)
    public Queue createLowStockQueue() {
        return new Queue(LOW_STOCK.getName(), false);
    }

    @Bean(name = DIRECT_EXCHANGE)
    @Primary
    public DirectExchange createDirectExchange() {
        return new DirectExchange("exchange.direct");
    }

    @Bean(name = MAIN_QUEUE + DIRECT_EXCHANGE)
    public Binding bindingMainQueue(
            Queue             queue,
            DirectExchange    exchange
    ) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(MAIN.getRoutingKey());
    }

    @Bean(name = LOW_STOCK_QUEUE + DIRECT_EXCHANGE)
    public Binding bindingLowStockQueue(
            Queue       queue,
            DirectExchange    exchange
    ) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(LOW_STOCK.getRoutingKey());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory   factory,
            MessageConverter    messageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
