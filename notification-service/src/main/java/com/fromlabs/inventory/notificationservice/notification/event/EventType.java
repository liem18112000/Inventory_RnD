package com.fromlabs.inventory.notificationservice.notification.event;

import lombok.Getter;

public enum EventType {

    INGREDIENT_ITEM_LOW_STOCK("Ingredient item quantity is low"),
    NOTIFICATION_MAIL_SENT("Notification of mail sending");

    @Getter
    private final String type;

    EventType(String type) {
        this.type = type;
    }
}
