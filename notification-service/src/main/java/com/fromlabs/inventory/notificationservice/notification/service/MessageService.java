package com.fromlabs.inventory.notificationservice.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import org.springframework.mail.MailException;

import javax.mail.MessagingException;
import java.time.format.DateTimeParseException;

/**
 * Message service
 * @author Liem
 */
public interface MessageService {

    /**
     * Send message
     * @param message MessageValueObject
     */
    MessageValueObject send(MessageValueObject message)
            throws DateTimeParseException, IllegalArgumentException,
            JsonProcessingException, MailException, MessagingException;

    MessageValueObject sendDefault(MessageValueObject message)
            throws DateTimeParseException, IllegalArgumentException,
            JsonProcessingException, MailException;
}
