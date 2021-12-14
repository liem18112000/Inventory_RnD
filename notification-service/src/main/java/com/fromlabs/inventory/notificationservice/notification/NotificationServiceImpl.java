package com.fromlabs.inventory.notificationservice.notification;

import com.fromlabs.inventory.notificationservice.notification.messages.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
public class NotificationServiceImpl implements NotificationService {

    protected final RabbitTemplate rabbitTemplate;

    public NotificationServiceImpl(
            RabbitTemplate  rabbitTemplate
    ) {
        trackDependencyInjection(rabbitTemplate);
        this.rabbitTemplate = rabbitTemplate;
    }

    private void trackDependencyInjection(
            RabbitTemplate  rabbitTemplate
    ) {
        log.info("Rabbit template : {}", rabbitTemplate.getClass().getName());
    }


    /**
     * Send message to a queue with specific Exchange
     *
     * @param message    Message
     * @param queueName  Queue name
     * @param routingKey Queue routing key
     * @param exchange   Exchange
     * @return return true if message is sent successfully. otherwise false
     */
    @Override
    public <V extends Serializable> boolean sendMessage(
            NotificationMessage<V>  message,
            String                  queueName,
            String                  routingKey,
            Exchange                exchange
    ) {
        // Check pre-conditions
        assert StringUtils.hasLength(queueName);
        assert StringUtils.hasLength(routingKey);
        assert Objects.nonNull(exchange);
        assert Objects.nonNull(rabbitTemplate);

        try {
            rabbitTemplate.convertAndSend(queueName, routingKey, message);
            return true;
        }catch (Exception exception) {
            exception.printStackTrace();
            log.error(exception.getMessage());
            return false;
        }
    }

    /**
     * Receive message from low stock queue
     *
     * @param queueName  Queue name
     * @param routingKey Queue routing key
     * @param exchange   Exchange
     * @return NotificationMessage
     */
    @Override
    public <V extends Serializable> NotificationMessage<V> receiveLowStockMessage(
            String      queueName,
            String      routingKey,
            Exchange    exchange
    ) {
        return null;
    }

    /**
     * Receive message from custom queue
     *
     * @param queueName  Queue name
     * @param routingKey Queue routing key
     * @param exchange   Exchange
     * @return NotificationMessage
     */
    @Override
    public <V extends Serializable> NotificationMessage<V> receiveCustomMessage(
            String      queueName,
            String      routingKey,
            Exchange    exchange
    ) {
        return null;
    }

    /**
     * Receive message from main queue
     *
     * @param queueName  Queue name
     * @param routingKey Queue routing key
     * @param exchange   Exchange
     * @return NotificationMessage
     */
    @Override
    public <V extends Serializable> NotificationMessage<V> receiveGeneralMessage(
            String      queueName,
            String      routingKey,
            Exchange    exchange
    ) {
        return null;
    }
}
