package com.fromlabs.inventory.inventoryservice.client.notification.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Event Data transfer object
 * @author Liem
 */
@Data
@Builder
public class EventDTO implements Serializable {
    private static final long serialVersionUID = -7979032320112642876L;

    private Long id;

    private String name;

    private String description;

    private String occurAt;

    private String eventType;
}
