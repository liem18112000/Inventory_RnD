package com.fromlabs.inventory.notificationservice.notification.beans.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Message value object mapper
 * @author Liem
 */
@Component
public class MessageValueObjectMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Convert message to JSON-like string
     * @param object MessageValueObject
     * @return JSON string
     * @throws JsonProcessingException when it failed to covert object to JSON string
     */
    public String toJSON(final @NotNull MessageValueObject object)
            throws JsonProcessingException {
        return this.mapper.writeValueAsString(object);
    }

    public SimpleMailMessage toSimpleMailMessage(
            final @NotNull MessageValueObject message) {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(message.getFrom());
        mailMessage.setTo(message.getTo());
        mailMessage.setText(message.getBody());
        mailMessage.setSubject(message.getSubject());
        return mailMessage;
    }

    public MimeMessage toMimeMessage(
            final MimeMessage mimeMessage,
            final @NotNull MessageValueObject message)
            throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage, true, "UTF-8");
        helper.setFrom(message.getFrom());
        helper.setTo(message.getTo());
        helper.setText(message.getBody(), true);
        helper.setSubject(message.getSubject());
        return mimeMessage;
    }

    /**
     * Convert message to object
     * @param json JSON string
     * @return MessageValueObject
     * @throws JsonProcessingException when it failed to covert JSON string to object
     */
    public MessageValueObject toObject(final @NotBlank String json)
            throws JsonProcessingException {
        return this.mapper.readValue(json, MessageValueObject.class);
    }
}
