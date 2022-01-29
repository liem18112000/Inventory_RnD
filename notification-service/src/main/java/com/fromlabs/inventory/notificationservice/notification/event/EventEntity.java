package com.fromlabs.inventory.notificationservice.notification.event;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

/**
 * Event entity
 * @author Liem
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name="event")
@Entity(name = "EventEntity")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name should not be blank")
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "event_type")
    @NotBlank(message = "Event type should ot be blank")
    private String eventType;

    @Column(name = "occur_at")
    private String occurAt = Instant.now().toString();

    @Column(name = "is_active")
    private boolean active = true;

}
