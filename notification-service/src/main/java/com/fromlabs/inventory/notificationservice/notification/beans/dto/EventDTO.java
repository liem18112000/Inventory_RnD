package com.fromlabs.inventory.notificationservice.notification.beans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class EventDTO implements Serializable {
    private static final long serialVersionUID = -6202782253214232226L;

    private Long id;

    private String name;

    private String description;

    @Builder.Default
    private String occurAt = Instant.now().toString();

    private String eventType;

    @Builder.Default
    private boolean active = true;

    private final String accessAt = Instant.now().toString();
}
