package com.fromlabs.inventory.notificationservice.notification.event;

import com.fromlabs.inventory.notificationservice.notification.messages.LowStockMessageValueObject;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import com.fromlabs.inventory.notificationservice.notification.messages.StatisticsMessageValueObject;
import lombok.Getter;

public enum EventType {

    INGREDIENT_ITEM_STATISTICS("Ingredient item monthly statistics",
            StatisticsMessageValueObject.class.getName()),
    INGREDIENT_ITEM_LOW_STOCK("Ingredient item quantity is low",
            LowStockMessageValueObject.class.getName()),
    NOTIFICATION_MAIL_SENT("Notification of mail sending",
            MessageValueObject.class.getName());

    @Getter
    private final String type;

    @Getter
    private final String messageType;

    EventType(String type, String messageType) {
        this.type = type;
        this.messageType = messageType;
    }
}
