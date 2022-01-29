package com.fromlabs.inventory.notificationservice.applications;

import com.fromlabs.inventory.notificationservice.config.ApiV1;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
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
import static org.springframework.http.ResponseEntity.ok;

/**
 * SupplierController is REST controller which responsible for
 * all C.R.U.D operations of ...
 * @see <a href="https://spring.io/guides/tutorials/rest/">REST Controller example : Building REST services with Spring</a>
 */
@Slf4j
@RestController
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API, produces = ApiV1.MIME_API)
public class NotificationController {

    private final NotificationService notificationService;

    private final EventService eventService;

    //<editor-fold desc="SETUP">

    public static final String SERVICE_PATH = "/notification/";

    public NotificationController(NotificationService notificationService, EventService eventService) {
        this.notificationService = notificationService;
        this.eventService = eventService;
    }

    //</editor-fold>

    //<editor-fold desc="EVENT">

    @GetMapping("event/types")
    public ResponseEntity<?> getEventTypes() {
        return ok(this.eventService.getTypes());
    }

    @GetMapping("event/{id:\\d+}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        try {
            return ok(this.eventService.getByIdWithException(id));
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @GetMapping("event/page")
    public ResponseEntity<?> getPageEvent(
            @RequestBody(required = false) EventDTO dto, Pageable pageable) {
        try {
            return ok(this.eventService.getPageWithFilter(dto, pageable));
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    //</editor-fold>

    //<editor-fold desc="NOTIFICATION">

    @GetMapping("statuses")
    public ResponseEntity<?> getNotificationStatuses() {
        return ok(this.notificationService.getStatuses());
    }

    @GetMapping("types")
    public ResponseEntity<?> getNotificationTypes() {
        return ok(this.notificationService.getTypes());
    }

    //</editor-fold>

    private ResponseEntity<?> handleException(Exception exception) throws ResponseStatusException {
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
