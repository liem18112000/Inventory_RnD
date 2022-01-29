package com.fromlabs.inventory.notificationservice.notification.service;

import com.fromlabs.inventory.notificationservice.common.dto.SimpleDto;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

/**
 * Event service
 * @author Liem
 */
public interface EventService {

    /**
     * Get event types
     * @return SimpleDto
     */
    Set<SimpleDto> getTypes();

    /**
     * Get event by id
     * @param id Event id
     * @return EventDTO
     */
    EventDTO getById(Long id) throws IllegalArgumentException;

    /**
     * Get event with exception
     * @param id Event id
     * @return EventDTO
     * @throws EntityNotFoundException entity not found with id
     */
    EventDTO getByIdWithException(Long id) throws EntityNotFoundException, IllegalArgumentException;

    /**
     * Get page with event DTO
     * @param eventDTO EventDTO
     * @param pageable Pageable
     * @return Page of event DTO
     */
    Page<EventDTO> getPageWithFilter(EventDTO eventDTO, Pageable pageable) throws IllegalArgumentException;

    /**
     * Save event by DTO
     * @param dto EventDTO
     * @return EventDTO
     * @throws IllegalArgumentException event DTO is null
     */
    EventDTO save(EventDTO dto) throws IllegalArgumentException;

    /**
     * Delete event by id
     * @param id Event id
     * @throws EntityNotFoundException entity not found with id
     */
    void delete(Long id) throws EntityNotFoundException, IllegalArgumentException;
}
