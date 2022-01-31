package com.fromlabs.inventory.notificationservice.notification.beans.validator;

import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationValidator {

    private final MessageValidator messageValidator;

    private final EventValidator eventValidator;

    /**
     * Validate notification when save
     * @param dto NotificationDTO
     * @throws IllegalArgumentException when it is not valid
     */
    public void validateSaveNotification(final NotificationDTO dto)
            throws IllegalArgumentException {
        this.validateRequest(dto);
        this.validateNotificationIdIfPresent(dto.getId());
        this.validateNotificationName(dto.getName());
        this.validateNotificationType(dto.getType());
        this.messageValidator.validateMessage(dto.getMessage());
        this.eventValidator.validateEventId(dto.getEvent().getId());
    }

    /**
     * Validate notification when update message
     * @param dto NotificationDTO
     * @throws IllegalArgumentException when it is not valid
     */
    public void validateUpdateMessage(final NotificationDTO dto)
            throws IllegalArgumentException {
        this.validateRequest(dto);
        this.validateNotificationId(dto.getId());
        this.messageValidator.validateMessage(dto.getMessage());
    }

    /**
     * Validate event id or id is null
     * @param id event id
     * @throws IllegalArgumentException when it is not valid
     */
    private void validateNotificationIdIfPresent(final Long id)
            throws IllegalArgumentException {
        if (Objects.nonNull(id) && id <= 0) {
            throw new IllegalArgumentException("Notification id is not positive");
        }
    }

    /**
     * Validate notification id
     * @param id Notification id
     * @throws IllegalArgumentException when it is not valid
     */
    public void validateNotificationId(final Long id)
            throws IllegalArgumentException {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Notification id is null");
        } else if (id <= 0) {
            throw new IllegalArgumentException("Notification id is not positive");
        }
    }

    /**
     * Validate notification type
     * @param type Notification type
     * @throws IllegalArgumentException when it is not valid
     */
    protected void validateNotificationType(final String type)
            throws IllegalArgumentException {
        if (Arrays.stream(NotificationType.values()).noneMatch(
                t -> t.getType().equals(type))) {
            throw new IllegalArgumentException("Notification type is not exist");
        }
    }

    /**
     * Validate notification name
     * @param name Notification name
     * @throws IllegalArgumentException when it is not valid
     */
    protected void validateNotificationName(final String name)
            throws IllegalArgumentException {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Notification name is null or blank");
        }
    }

    /**
     * Validate notification request
     * @param dto NotificationDTO
     * @throws IllegalArgumentException when it is not valid
     */
    protected void validateRequest(final NotificationDTO dto)
            throws IllegalArgumentException {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("Notification DTO is null");
        }
    }
}
