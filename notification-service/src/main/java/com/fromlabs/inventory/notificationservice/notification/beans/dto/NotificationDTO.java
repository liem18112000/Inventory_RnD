package com.fromlabs.inventory.notificationservice.notification.beans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Event Data transfer object
 * @author Liem
 */
@JsonInclude(NON_NULL)
@Data
@Builder
public class NotificationDTO implements Serializable {
    private static final long serialVersionUID = 1850127351218069058L;

    private Long id;

    private String name;

    private String description;

    private EventDTO event;

    private MessageValueObject message;

    private String type;

    private String notifyAt;

    private String createdAt;

    private String status;

    private boolean active;

    private final String accessAt = Instant.now().toString();
}
