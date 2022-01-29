package com.fromlabs.inventory.notificationservice.notification.notfication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotificationEntityRepository extends
        JpaRepository<NotificationEntity, Long>, JpaSpecificationExecutor<NotificationEntity> {

}