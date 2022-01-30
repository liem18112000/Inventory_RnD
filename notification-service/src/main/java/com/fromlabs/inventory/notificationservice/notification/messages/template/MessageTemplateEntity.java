package com.fromlabs.inventory.notificationservice.notification.messages.template;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "MessageTemplate")
@Table(name = "template")
public class MessageTemplate {
}
