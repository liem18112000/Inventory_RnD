package com.fromlabs.inventory.notificationservice.client.endpoint;

import com.fromlabs.inventory.notificationservice.config.ApiV1;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.BatchSendNotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.service.EventService;
import com.fromlabs.inventory.notificationservice.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

/**
 * Notification endpoint
 * @author Liem
 */
@Slf4j
@RestController
@RequestMapping(value = "endpoint/${application.base-url}/" + ApiV1.URI_API)
public class EndPoint {

    private final NotificationService notificationService;

    private final EventService eventService;

    public EndPoint(NotificationService notificationService, EventService eventService) {
        this.notificationService = notificationService;
        this.eventService = eventService;
    }

    //<editor-fold desc="EVENT">

    @GetMapping("event/{id:\\d+}")
    public EventDTO getEventById(@PathVariable Long id) {
        try {
            return this.eventService.getById(id);
        } catch (Exception exception) {
            this.handleException(exception);
            return null;
        }
    }

    @PostMapping("event")
    public EventDTO saveEvent(@RequestBody EventDTO request) {
        try {
            return this.eventService.save(request);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PutMapping("event/{id}")
    public EventDTO updateEvent(
            @PathVariable Long id, @RequestBody EventDTO request) {
        try {
            request.setId(id);
            return this.eventService.save(request);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @DeleteMapping("event/{id}")
    public void deleteEvent(@PathVariable Long id) {
        try {
            this.eventService.delete(id);
        } catch (Exception exception) {
            this.handleException(exception);
        }
    }

    //</editor-fold>

    //<editor-fold desc="NOTIFICATION">

    @GetMapping("{id:\\d+}")
    public NotificationDTO getNotificationById(@PathVariable Long id) {
        try {
            return this.notificationService.getById(id);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PostMapping
    public NotificationDTO saveNotification(@RequestBody NotificationDTO request) {
        try {
            return this.notificationService.save(request);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PutMapping("all/send-message")
    public BatchSendNotificationDTO sendNotificationMessage(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        try {
            return this.notificationService.sendAllNotification(limit);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PutMapping("{id:\\d+}/send-message")
    public NotificationDTO sendNotificationMessage(@PathVariable Long id) {
        try {
            return this.notificationService.sendNotification(id);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PutMapping("{id:\\d+}/message")
    public NotificationDTO updateNotificationMessage(
            @PathVariable Long id, @RequestBody NotificationDTO request) {
        try {
            request.setId(id);
            return this.notificationService.updateMessage(request);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PutMapping("{id:\\d+}")
    public NotificationDTO updateNotification(
            @PathVariable Long id, @RequestBody NotificationDTO request) {
        try {
            request.setId(id);
            return this.notificationService.save(request);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @DeleteMapping("{id:\\d+}")
    public void deleteNotification(@PathVariable Long id) {
        try {
            this.eventService.delete(id);
        } catch (Exception exception) {
            this.handleException(exception);
        }
    }

    //</editor-fold>

    private <T> T handleException(Exception exception) throws ResponseStatusException {
        HttpStatus status = INTERNAL_SERVER_ERROR;
        if (exception instanceof IllegalArgumentException) {
            status = BAD_REQUEST;
        } else if (exception instanceof EntityNotFoundException) {
            status = NOT_FOUND;
        } else {
            exception.printStackTrace();
        }

        throw new ResponseStatusException(status, exception.getMessage());
    }

}
