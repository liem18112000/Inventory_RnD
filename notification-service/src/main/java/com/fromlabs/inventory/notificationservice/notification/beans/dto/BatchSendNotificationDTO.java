package com.fromlabs.inventory.notificationservice.notification.beans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Data
@Builder
public class BatchSendNotificationDTO {
    private List<NotificationDTO> notifications;
    private int totalCount;
    private int successCount;
    private List<String> failedNotifications;
}
