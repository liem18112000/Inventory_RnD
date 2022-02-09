package com.fromlabs.inventory.notificationservice.notification.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fromlabs.inventory.notificationservice.notification.beans.mapper.MessageValueObjectMapper;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import com.fromlabs.inventory.notificationservice.notification.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * {@inheritDoc}
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailMessageService implements MessageService {

    private final JavaMailSender sender;

    private final MessageValueObjectMapper mapperMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageValueObject send(
            final @NotNull MessageValueObject message)
            throws DateTimeParseException, IllegalArgumentException,
            JsonProcessingException, MailException, MessagingException {
        var mailMessage = this.mapperMapper.toMimeMessage(
                sender.createMimeMessage(), message);
        var sentMessage = this.objectMapper.readValue(
                this.objectMapper.writeValueAsString(message), MessageValueObject.class);
        final var currentDate = Instant.now();
        sentMessage.setSendAt(currentDate.toString());
        mailMessage.setSentDate(Date.from(currentDate));
        this.sender.send(mailMessage);
        return sentMessage;
    }

    @Override
    public MessageValueObject sendDefault(
            final @NotNull MessageValueObject message)
            throws DateTimeParseException, IllegalArgumentException,
            JsonProcessingException, MailException {
        var mailMessage = new SimpleMailMessage();
        var sentMessage = this.objectMapper.readValue(
                this.objectMapper.writeValueAsString(message), MessageValueObject.class);
        final var currentDate = Instant.now();
        sentMessage.setSendAt(currentDate.toString());
        mailMessage.setSentDate(Date.from(currentDate));
        this.sender.send(mailMessage);
        return sentMessage;
    }


}
