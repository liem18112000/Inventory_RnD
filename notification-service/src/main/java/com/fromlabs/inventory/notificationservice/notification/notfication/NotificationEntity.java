package com.fromlabs.inventory.notificationservice.notification.notfication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.validator.MessageValidator;
import com.fromlabs.inventory.notificationservice.notification.event.EventEntity;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

import static com.fromlabs.inventory.notificationservice.notification.notfication.NotificationStatus.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Event entity
 * @author Liem
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name="notification")
@Entity(name = "NotificationEntity")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    private String type = NotificationType.EMAIL.getType();

    @Column(name = "notify_at")
    private String notifyAt;

    @Column(name = "created_at")
    private String createdAt = Instant.now().toString();

    @Column(name = "status")
    private String status = NEW.getStatus();

    @Column(name = "is_active")
    private boolean active = true;

    public void updateBasicInformation(
            final @NotNull NotificationDTO dto) throws IllegalStateException {
        if (nonNull(this.getStatus()) && !this.status.equals(NEW.getStatus())) {
            throw new IllegalStateException(
                    "Cannot update information of not newly-created notification");
        }
        this.setName(dto.getName());
        this.setDescription(dto.getDescription());
    }

    public void updateAfterCreated() throws IllegalStateException {
        if (isNull(this.status)) {
            this.setStatus(NEW.getStatus());
            this.setCreatedAt(Instant.now().toString());
        } else {
            throw new IllegalStateException(
                    String.format("Cannot change notification status as it is in '%s' status",
                            this.status));
        }
    }

    public void deactivate() {
        this.setActive(false);
    }

    public void activate() {
        this.setActive(true);
    }

    public void updateStatusToSending() {
        this.setStatus(SENDING.getStatus());
    }

    public void updateAfterSendMessage()
            throws IllegalStateException {
        if (this.getStatus().equals(COMPLETE.getStatus())) {
            throw new IllegalStateException("Notification is re-send");
        }

        if (Objects.nonNull(this.getNotifyAt())) {
            throw new IllegalStateException("Notification notify timestamp is already assigned");
        }
        this.setNotifyAt(Instant.now().toString());
        this.setStatus(COMPLETE.getStatus());
    }

    public void updateStatusToFailed() {
        this.setStatus(FAILED.getStatus());
    }

    public void updateMessage(final @NotNull MessageValueObject message)
            throws IllegalStateException, JsonProcessingException {
        if (nonNull(this.getStatus()) && !this.status.equals(NEW.getStatus())) {
            throw new IllegalStateException("Cannot update message of not newly-created notification");
        }
        final var mapper = new ObjectMapper();
        final var stringMessage = mapper.writeValueAsString(message);
        this.setMessage(stringMessage);
    }
}
