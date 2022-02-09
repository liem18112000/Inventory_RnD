package com.fromlabs.inventory.notificationservice.notification.beans.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import com.fromlabs.inventory.notificationservice.notification.messages.utils.EmailTemplateEngineGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Message value object mapper
 * @author Liem
 */
@Component
public class MessageValueObjectMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private EmailTemplateEngineGenerator templateModelUtil;

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
        final var model = this.toMap(message);
        final var htmlBody = this.templateModelUtil
                .generateHTMLBody(message.getBody(), model);
        helper.setSubject(message.getSubject());
        helper.setFrom(message.getFrom());
        helper.setTo(message.getTo());
        helper.setText(htmlBody, true);
        return mimeMessage;
    }

    public Map<String, Object> toMap(
            final MessageValueObject message)
            throws IllegalArgumentException {
        if (Objects.isNull(message)) {
            throw new IllegalArgumentException("messageValueObject is null");
        }

        return Map.of(
                "subject", message.getSubject(),
                "body", message.getBody(),
                "sendAt", message.getSendAt(),
                "from", message.getFrom(),
                "to", message.getTo()
        );
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
