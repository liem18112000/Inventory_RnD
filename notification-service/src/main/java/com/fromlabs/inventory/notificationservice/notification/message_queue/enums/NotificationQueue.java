package com.fromlabs.inventory.notificationservice.notification.message_queue.enums;

import lombok.Getter;

public enum NotificationQueue {
    MAIN(       "queue.mainQueue",      "routing.mainQueue"),
    LOW_STOCK(  "queue.lowStockQueue",  "routing.lowStockQueue");

    @Getter private final String name;
    @Getter private final String routingKey;

    NotificationQueue(String name, String routingKey) {
        this.name = name;
        this.routingKey = routingKey;
    }

}
