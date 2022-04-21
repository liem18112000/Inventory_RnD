/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.client.notification;

import com.fromlabs.inventory.inventoryservice.client.notification.beans.EventDTO;
import com.fromlabs.inventory.inventoryservice.client.notification.beans.NotificationDTO;
import com.fromlabs.inventory.inventoryservice.common.dto.SimpleDto;
import com.fromlabs.inventory.inventoryservice.config.ApiV1;
import io.sentry.spring.tracing.SentryTransaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Notification client to notification module
 * @author Liem
 */
@SentryTransaction(operation = "ingredient-notification-client")
@FeignClient(value = "${services.notification-service.name}")
@RequestMapping(value = "endpoint/notification/" + ApiV1.URI_API)
public interface NotificationClient {

    @GetMapping("{id:\\d+}")
    NotificationDTO getNotificationById(@PathVariable Long id);

    @PutMapping("{id:\\d+}/status/sending")
    NotificationDTO updateNotificationStatusToSending(@PathVariable Long id);

    @PutMapping("{id:\\d+}/send-message")
    NotificationDTO sendNotificationMessage(@PathVariable Long id);

    @PostMapping
    NotificationDTO saveNotification(@RequestBody NotificationDTO request);

    @PostMapping("event")
    EventDTO saveEvent(@RequestBody EventDTO request);

    @GetMapping("event/{id:\\d+}")
    EventDTO getEventById(@PathVariable Long id);

}
