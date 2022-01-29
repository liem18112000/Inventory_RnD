package com.fromlabs.inventory.notificationservice.notification.service.impl;

import com.fromlabs.inventory.notificationservice.common.dto.SimpleDto;
import com.fromlabs.inventory.notificationservice.notification.event.EventType;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationStatus;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationType;
import com.fromlabs.inventory.notificationservice.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
public class NotificationServiceImpl implements NotificationService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<SimpleDto> getStatuses() {
       return Arrays.stream(NotificationStatus.values())
               .map(status -> new SimpleDto(status.getStatus(), status.getStatus().toUpperCase()))
               .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    public Set<SimpleDto> getTypes() {
        return Arrays.stream(NotificationType.values())
                .map(type -> new SimpleDto(type.getType(), type.getType().toUpperCase()))
                .collect(Collectors.toSet());
    }
}
