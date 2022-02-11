package com.fromlabs.inventory.notificationservice.notification.notfication;

import lombok.Getter;

/**
 * Notification type
 * @author Liem
 */
public enum NotificationType {
    EMAIL("email"), SMS("sms");

    @Getter
    private final String type;

    NotificationType(final String type) {
        this.type = type;
    }
}
