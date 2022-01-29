package com.fromlabs.inventory.notificationservice.notification.notfication;

import lombok.Getter;

/**
 * Notification status
 * @author Liem
 */
public enum NotificationStatus {
    NEW("created"), SENDING("sending"), COMPLETE("complete"), FAILED("failed");

    @Getter
    private final String status;

    NotificationStatus(String status) {
        this.status = status;
    }
}
