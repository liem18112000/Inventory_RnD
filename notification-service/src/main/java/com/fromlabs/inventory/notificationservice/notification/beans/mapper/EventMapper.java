package com.fromlabs.inventory.notificationservice.notification.beans.mapper;

import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
import com.fromlabs.inventory.notificationservice.notification.event.EventEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Event mapper
 * @author Liem
 */
@Slf4j
@Component
public class EventMapper {

    /**
     * Convert to entity
     * @param entity EventEntity
     * @return EventDTO
     */
    public EventDTO toDto(final EventEntity entity) {

        if (Objects.isNull(entity)) {
            log.warn("Return null DTO as entity is null");
            return null;
        }

        return EventDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .eventType(entity.getEventType())
                .occurAt(entity.getOccurAt())
                .active(entity.isActive())
                .build();
    }

    /**
     * Convert to entity
     * @param dto EventDTO
     * @return EventEntity
     */
    public EventEntity toEntity(final EventDTO dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("DTO is null");
        }

        var entity = new EventEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setEventType(dto.getEventType());
        entity.setOccurAt(dto.getOccurAt());
        entity.setActive(dto.isActive());
        return entity;
    }
}
