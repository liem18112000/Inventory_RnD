package com.fromlabs.inventory.notificationservice.notification.beans.validator;

import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
import com.fromlabs.inventory.notificationservice.notification.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Event Data transfer object validator
 * @author Liem
 */
@Slf4j
@Component
public class EventValidator {

    public void validateSaveEvent(final EventDTO eventDTO)
            throws IllegalArgumentException {
        this.validateRequest(eventDTO);
        this.validEventIdIfPresent(eventDTO);
        this.validateEventName(eventDTO);
        this.validateEventType(eventDTO);
        this.validateEventOccur(eventDTO);
    }

    private void validEventIdIfPresent(final EventDTO eventDTO)
            throws IllegalArgumentException {
        if (Objects.nonNull(eventDTO.getId()) && eventDTO.getId() <= 0) {
            throw new IllegalArgumentException("Event id is not positive");
        }
    }

    public void validateEventId(final Long id)
            throws IllegalArgumentException {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Event id is null");
        } else if (id <= 0) {
            throw new IllegalArgumentException("Event id is not positive");
        }
    }

    protected void validateEventOccur(final EventDTO eventDTO)
            throws IllegalArgumentException {
        if (!StringUtils.hasText(eventDTO.getOccurAt())) {
            throw new IllegalArgumentException("Event occur timestamp is null or blank");
        }
    }

    protected void validateEventType(final EventDTO eventDTO)
            throws IllegalArgumentException {
        if (Arrays.stream(EventType.values()).noneMatch(
                type -> type.getType().equals(eventDTO.getEventType()))) {
            throw new IllegalArgumentException("Event type is not exist");
        }
    }

    protected void validateEventName(final EventDTO eventDTO)
            throws IllegalArgumentException {
        if (!StringUtils.hasText(eventDTO.getName())) {
            throw new IllegalArgumentException("Event name is null or blank");
        }
    }

    protected void validateRequest(final EventDTO eventDTO)
            throws IllegalArgumentException {
        if (Objects.isNull(eventDTO)) {
            throw new IllegalArgumentException("Event DTO is null");
        }
    }
}
