package com.fromlabs.inventory.notificationservice.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fromlabs.inventory.notificationservice.common.dto.SimpleDto;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.BatchSendNotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

public interface NotificationService {

    /**
     * Get notification statuses
     * @return Set of SimpleDto
     */
    Set<SimpleDto> getStatuses();

    /**
     * Get notification types
     * @return Set of SimpleDto
     */
    Set<SimpleDto> getTypes();

    /**
     * Get notification by id
     * @param id Event id
     * @return EventDTO
     */
    NotificationDTO getById(Long id)
            throws IllegalArgumentException, JsonProcessingException;

    /**
     * Get notification with exception
     * @param id notification id
     * @return EventDTO
     * @throws EntityNotFoundException entity not found with id
     */
    NotificationDTO getByIdWithException(Long id)
            throws EntityNotFoundException, IllegalArgumentException, JsonProcessingException;

    /**
     * Get page with notification DTO
     * @param notificationDTO NotificationDTO
     * @param pageable Pageable
     * @return Page of notification DTO
     */
    Page<NotificationDTO> getPageWithFilter(
            NotificationDTO notificationDTO, Pageable pageable)
            throws IllegalArgumentException;

    /**
     * Save notification by DTO
     * @param dto NotificationDTO
     * @return NotificationDTO
     * @throws IllegalArgumentException NotificationDTO is null
     */
    NotificationDTO save(NotificationDTO dto)
            throws IllegalArgumentException, JsonProcessingException,
            EntityNotFoundException, IllegalStateException;

    /**
     * Update notification message by DTO
     * @param dto NotificationDTO
     * @return NotificationDTO
     */
    NotificationDTO updateMessage(NotificationDTO dto)
            throws IllegalArgumentException, EntityNotFoundException,
            JsonProcessingException, IllegalStateException;

    /**
     * Send notification by id
     * @param id Notification id
     * @return NotificationDTO
     */
    NotificationDTO sendNotification(Long id) throws JsonProcessingException;

    /**
     * Send all notification message with limit (default is 10)
     * @param limit Limit for send message
     * @return BatchSendNotificationDTO
     */
    BatchSendNotificationDTO sendAllNotification(Integer limit);

    /**
     * Delete notification by id
     * @param id notification id
     * @throws EntityNotFoundException entity not found with id
     */
    void delete(Long id)
            throws EntityNotFoundException, IllegalArgumentException;

}
