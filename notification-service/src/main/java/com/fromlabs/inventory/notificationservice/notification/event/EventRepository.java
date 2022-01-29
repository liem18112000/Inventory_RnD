package com.fromlabs.inventory.notificationservice.notification.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventRepository extends
        JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

}