package com.fromlabs.inventory.notificationservice.notification.event;

import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
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

    public void updateAfterCreated() {
        this.setOccurAt(Instant.now().toString());
    }

    public void deactivate() {
        this.setActive(false);
    }

    public void activate() {
        this.setActive(true);
    }

    public void updateBasicInformation(EventDTO dto) {
        this.setName(dto.getName());
        this.setEventType(dto.getEventType());
        this.setDescription(dto.getDescription());
    }
}
