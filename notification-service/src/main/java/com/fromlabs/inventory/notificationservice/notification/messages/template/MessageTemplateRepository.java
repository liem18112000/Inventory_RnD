package com.fromlabs.inventory.notificationservice.notification.messages.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Message template repository
 * @author Liem
 */
@Repository
public interface MessageTemplateRepository
        extends JpaRepository<MessageTemplateEntity, Long> {
    Optional<MessageTemplateEntity> findByName(String name);
}