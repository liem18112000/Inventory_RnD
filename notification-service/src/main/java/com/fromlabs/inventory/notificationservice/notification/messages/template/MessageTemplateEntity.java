package com.fromlabs.inventory.notificationservice.notification.messages.template;

import lombok.*;

import javax.persistence.*;

/**
 * Message template entity
 * @author Liem
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "MessageTemplateEntity")
@Table(name = "template")
public class MessageTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "data")
    private String data;
}
