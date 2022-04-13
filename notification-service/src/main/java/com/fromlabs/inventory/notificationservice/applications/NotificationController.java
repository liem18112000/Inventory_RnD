package com.fromlabs.inventory.notificationservice.applications;

import com.fromlabs.inventory.notificationservice.config.ApiV1;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.service.EventService;
import com.fromlabs.inventory.notificationservice.notification.service.NotificationService;
import io.sentry.spring.tracing.SentryTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

/**
 * SupplierController is REST controller which responsible for
 * all C.R.U.D operations of ...
 * @see <a href="https://spring.io/guides/tutorials/rest/">REST Controller example : Building REST services with Spring</a>
 */
@Slf4j
@RestController
@SentryTransaction(operation = "notification-service")
@RequestMapping(value = "notification/" + ApiV1.URI_API, produces = ApiV1.MIME_API)
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
        final var eventTypes = this.eventService.getTypes();
        final var resource = CollectionModel.of(eventTypes)
                .add(linkTo(methodOn(this.getClass()).getEventTypes()).withSelfRel());
        return ok(resource);
    }

    @GetMapping("event/{id:\\d+}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        try {
            final var event = this.eventService.getByIdWithException(id);
            final var resource = generateResource(event);
            return ok(resource);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PostMapping("event/page")
    public ResponseEntity<?> getPageEvent(
            @RequestBody(required = false) EventDTO dto, Pageable pageable) {
        try {
            final var eventPages = this.eventService
                    .getPageWithFilter(dto, pageable)
                    .map(this::generateResource);
            final RepresentationModel<?> resource = RepresentationModel.of(eventPages)
                    .add(linkTo(methodOn(this.getClass()).getPageEvent(dto, pageable)).withSelfRel())
                    .add(linkTo(methodOn(this.getClass()).getEventTypes()).withRel("event_types"));
            return ok(resource);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PostMapping("event")
    public ResponseEntity<?> saveEvent(@RequestBody EventDTO request) {
        try {
            final var event = this.eventService.save(request);
            final var resource = this.generateResource(event);
            return status(CREATED).body(resource);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PutMapping("event/{id:\\d+}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long id, @RequestBody EventDTO request) {
        try {
            request.setId(id);
            final var event = this.eventService.save(request);
            final var resource = this.generateResource(event);
            return ok(resource);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @DeleteMapping("event/{id:\\d+}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            this.eventService.delete(id);
            return noContent().build();
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    //</editor-fold>

    //<editor-fold desc="NOTIFICATION">

    @GetMapping("statuses")
    public ResponseEntity<?> getNotificationStatuses() {
        final var statuses = this.notificationService.getStatuses();
        final var resource = CollectionModel.of(statuses)
                .add(linkTo(methodOn(this.getClass()).getNotificationStatuses()).withSelfRel());
        return ok(resource);
    }

    @GetMapping("types")
    public ResponseEntity<?> getNotificationTypes() {
        final var types = this.notificationService.getTypes();
        final var resource = CollectionModel.of(types)
                .add(linkTo(methodOn(this.getClass()).getNotificationTypes()).withSelfRel());
        return ok(resource);
    }

    @GetMapping("event/{id:\\d+}/notifications")
    public ResponseEntity<?> getNotificationsOfEvent(@PathVariable Long id) {
        try {
            final var event = EventDTO.builder().id(id).build();
            final var dto = NotificationDTO.builder().event(event).build();
            final var list = this.notificationService
                    .getAllWithFilter(dto)
                    .stream().map(this::generateResource)
                    .collect(Collectors.toList());
            final var resource = generateResource(id, list);
            return ok(resource);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @GetMapping("{id:\\d+}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long id) {
        try {
            final var notification = this.notificationService.getByIdWithException(id);
            final var resource = generateResource(notification);
            return ok(resource);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @PostMapping("page")
    public ResponseEntity<?> getPageNotification(
            @RequestBody(required = false) NotificationDTO dto, Pageable pageable) {
        try {
            final var pages = this.notificationService
                    .getPageWithFilter(dto, pageable)
                    .map(this::generateResource);
            final RepresentationModel<?> resource = RepresentationModel.of(pages)
                    .add(linkTo(methodOn(this.getClass()).getPageNotification(dto, pageable)).withSelfRel())
                    .add(linkTo(methodOn(this.getClass()).getNotificationStatuses()).withRel("statuses"))
                    .add(linkTo(methodOn(this.getClass()).getNotificationTypes()).withRel("types"));
            return ok(resource);
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    //</editor-fold>

    private Object generateResource(Long id, List<?> list) {
        return CollectionModel.of(list)
                .add(linkTo(methodOn(this.getClass()).getNotificationsOfEvent(id)).withSelfRel())
                .add(linkTo(methodOn(this.getClass()).getEventTypes()).withRel("event_types"))
                .add(linkTo(methodOn(this.getClass()).getEventById(id)).withRel("event"));
    }

    private Object generateResource(EventDTO event) {
        return RepresentationModel.of(event)
                .add(linkTo(methodOn(this.getClass()).getEventById(event.getId())).withSelfRel())
                .add(linkTo(methodOn(this.getClass()).getEventTypes()).withRel("event_types"));
    }

    private Object generateResource(NotificationDTO notification) {
        return RepresentationModel.of(notification)
                .add(linkTo(methodOn(this.getClass()).getNotificationById(notification.getId())).withSelfRel())
                .add(linkTo(methodOn(this.getClass()).getEventById(notification.getEvent().getId())).withRel("event"))
                .add(linkTo(methodOn(this.getClass()).getNotificationStatuses()).withRel("statuses"))
                .add(linkTo(methodOn(this.getClass()).getNotificationTypes()).withRel("types"));
    }

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
