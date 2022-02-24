package com.fromlabs.inventory.inventoryservice.client.notification.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Event Data transfer object
 * @author Liem
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO implements Serializable {
    private static final long serialVersionUID = 1849110233953707096L;

    private Long id;

    private String name;

    private String description;

    private String occurAt;

    private String eventType;
}
