package com.fromlabs.inventory.notificationservice.notification.notfication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NotificationRepository extends
        JpaRepository<NotificationEntity, Long>, JpaSpecificationExecutor<NotificationEntity> {
    List<NotificationEntity> findAllByStatusAndAndNotifyAtIsNullAndActiveIsTrue(String status);
}