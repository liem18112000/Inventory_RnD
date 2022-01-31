package com.fromlabs.inventory.notificationservice.notification.beans.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.event.EventEntity;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Notification mapper
 * @author Liem
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationMapper {

    private final EventMapper eventMapper;

    private final MessageValueObjectMapper messageValueObjectMapper;

    /**
     * Convert to DTO
     * @param entity NotificationEntity
     * @return NotificationDTO
     * @throws JsonProcessingException convert message failed
     */
    public NotificationDTO toDto(final NotificationEntity entity)
            throws JsonProcessingException {
        if (Objects.isNull(entity)) {
            return null;
        }

        return NotificationDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .event(this.eventMapper.toDto(entity.getEvent()))
                .message(this.messageValueObjectMapper.toObject(entity.getMessage()))
                .type(entity.getType())
                .notifyAt(entity.getNotifyAt())
                .status(entity.getStatus())
                .active(entity.isActive())
                .build();
    }

    /**
     * Convert to entity
     * @param dto NotificationDTO
     * @return NotificationEntity
     * @throws JsonProcessingException convert message failed
     */
    public NotificationEntity toEntity(
            final NotificationDTO dto, final EventEntity event)
            throws JsonProcessingException {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("Dto is null");
        }

        var entity = new NotificationEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setEvent(event);
        entity.setMessage(this.messageValueObjectMapper.toJSON(dto.getMessage()));
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setNotifyAt(dto.getNotifyAt());
        entity.setActive(dto.isActive());
        return entity;
    }
}
