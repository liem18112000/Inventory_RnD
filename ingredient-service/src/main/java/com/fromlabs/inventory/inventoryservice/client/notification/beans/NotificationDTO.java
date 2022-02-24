package com.fromlabs.inventory.inventoryservice.client.notification.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Event Data transfer object
 * @author Liem
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NotificationDTO implements Serializable {
    private static final long serialVersionUID = 8306004480374842254L;

    private Long id;

    private String name;

    private String description;

    private EventDTO event;

    private MessageValueObject message;

    private String type;

    private String notifyAt;

    private String createdAt;

    private String status;
}
