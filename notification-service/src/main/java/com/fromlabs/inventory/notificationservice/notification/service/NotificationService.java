package com.fromlabs.inventory.notificationservice.notification.service;

import com.fromlabs.inventory.notificationservice.common.dto.SimpleDto;

import java.util.Set;

public interface NotificationService {

    /**
     * Get notification stauts
     * @return Set of SimpleDto
     */
    Set<SimpleDto> getStatuses();

    /**
     * Get notification types
     * @return Set of SimpleDto
     */
    Set<SimpleDto> getTypes();

}
