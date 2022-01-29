package com.fromlabs.inventory.notificationservice.notification.event;

public interface EventRepository extends org.springframework.data.jpa.repository.JpaRepository<com.fromlabs.inventory.notificationservice.notification.event.EventEntity, java.lang.Long> ,org.springframework.data.jpa.repository.JpaSpecificationExecutor<com.fromlabs.inventory.notificationservice.notification.event.EventEntity> {
}