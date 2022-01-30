package com.fromlabs.inventory.notificationservice.notification.beans.validator;

import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
import com.fromlabs.inventory.notificationservice.notification.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Event Data transfer object validator
 * @author Liem
 */
@Slf4j
@Component
public class EventValidator {

    /**
     * Validate event when save
     * @param eventDTO EventDTO
     * @throws IllegalArgumentException when it is not valid
     */
    public void validateSaveEvent(final EventDTO eventDTO)
            throws IllegalArgumentException {
        this.validateRequest(eventDTO);
        this.validateEventIdIfPresent(eventDTO.getId());
        this.validateEventName(eventDTO.getName());
        this.validateEventType(eventDTO.getEventType());
    }

    /**
     * Validate event id or id is null
     * @param id event id
     * @throws IllegalArgumentException when it is not valid
     */
    private void validateEventIdIfPresent(final Long id)
            throws IllegalArgumentException {
        if (Objects.nonNull(id) && id <= 0) {
            throw new IllegalArgumentException("Event id is not positive");
        }
    }

    /**
     * Validate event id
     * @param id Event id
     * @throws IllegalArgumentException when it is not valid
     */
    public void validateEventId(final Long id)
            throws IllegalArgumentException {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Event id is null");
        } else if (id <= 0) {
            throw new IllegalArgumentException("Event id is not positive");
        }
    }

    /**
     * Validate event type
     * @param eventType event type
     * @throws IllegalArgumentException when it is not valid
     */
    protected void validateEventType(final String eventType)
            throws IllegalArgumentException {
        if (Arrays.stream(EventType.values()).noneMatch(
                type -> type.getType().equals(eventType))) {
            throw new IllegalArgumentException("Event type is not exist");
        }
    }

    /**
     * Validate event name
     * @param name Event name
     * @throws IllegalArgumentException when it is not valid
     */
    protected void validateEventName(final String name)
            throws IllegalArgumentException {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Event name is null or blank");
        }
    }

    /**
     * Validate request
     * @param eventDTO EventDTO
     * @throws IllegalArgumentException when it is not valid
     */
    protected void validateRequest(final EventDTO eventDTO)
            throws IllegalArgumentException {
        if (Objects.isNull(eventDTO)) {
            throw new IllegalArgumentException("Event DTO is null");
        }
    }
}
