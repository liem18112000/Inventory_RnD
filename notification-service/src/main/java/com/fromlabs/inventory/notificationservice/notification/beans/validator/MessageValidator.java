package com.fromlabs.inventory.notificationservice.notification.beans.validator;

import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Message value object validator
 * @author Liem
 */
@Component
public class MessageValidator {

    /**
     * Validate message
     * @param message MessageValueObject
     * @throws IllegalArgumentException when it is not valid
     */
    public void validateMessage(MessageValueObject message)
            throws IllegalArgumentException {

        if (Objects.isNull(message)) {
            throw new IllegalArgumentException("Message is null");
        }

        if (!StringUtils.hasText(message.getSubject())) {
            throw new IllegalArgumentException("Message subject is null or blank");
        }

        if (!StringUtils.hasText(message.getBody())) {
            throw new IllegalArgumentException("Message body is null or blank");
        }

        if (!StringUtils.hasText(message.getTo())) {
            throw new IllegalArgumentException("Message to is null or blank");
        }

        if (!StringUtils.hasText(message.getFrom())) {
            throw new IllegalArgumentException("Message from is null or blank");
        }
    }
}
