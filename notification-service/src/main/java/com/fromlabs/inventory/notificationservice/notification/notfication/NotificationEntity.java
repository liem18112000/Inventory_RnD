package com.fromlabs.inventory.notificationservice.notification.notfication;

import com.fromlabs.inventory.notificationservice.notification.event.EventEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

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
    private String status = NotificationStatus.NEW.getStatus();

    @Column(name = "is_active")
    private boolean active = true;
}
