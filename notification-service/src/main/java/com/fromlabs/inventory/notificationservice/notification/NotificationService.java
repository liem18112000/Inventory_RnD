package com.fromlabs.inventory.notificationservice.notification;

import com.fromlabs.inventory.notificationservice.notification.messages.NotificationMessage;
import org.springframework.amqp.core.Exchange;

import java.io.Serializable;

public interface NotificationService {

    /**
     * Send message to a queue with specific Exchange
     * @param message       Message
     * @param queueName     Queue name
     * @param routingKey    Queue routing key
     * @param exchange      Exchange
     * @param <V>           Message Content Datatype
     * @return              return true if message is sent successfully. otherwise false
     */
    <V extends Serializable> boolean sendMessage(
            NotificationMessage<V>  message,
            String                  queueName,
            String                  routingKey,
            Exchange                exchange
    );

    /**
     * Receive message from low stock queue
     * @param queueName     Queue name
     * @param routingKey    Queue routing key
     * @param exchange      Exchange
     * @param <V>           Message Content Datatype
     * @return              NotificationMessage
     */
    <V extends Serializable> NotificationMessage<V> receiveLowStockMessage(
            String                  queueName,
            String                  routingKey,
            Exchange                exchange
    );

    /**
     * Receive message from custom queue
     * @param queueName     Queue name
     * @param routingKey    Queue routing key
     * @param exchange      Exchange
     * @param <V>           Message Content Datatype
     * @return              NotificationMessage
     */
    <V extends Serializable> NotificationMessage<V> receiveCustomMessage(
            String                  queueName,
            String                  routingKey,
            Exchange                exchange
    );

    /**
     * Receive message from main queue
     * @param queueName     Queue name
     * @param routingKey    Queue routing key
     * @param exchange      Exchange
     * @param <V>           Message Content Datatype
     * @return              NotificationMessage
     */
    <V extends Serializable> NotificationMessage<V> receiveGeneralMessage(
            String                  queueName,
            String                  routingKey,
            Exchange                exchange
    );
}
