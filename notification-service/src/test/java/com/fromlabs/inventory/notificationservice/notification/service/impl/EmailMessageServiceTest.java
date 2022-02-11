package com.fromlabs.inventory.notificationservice.notification.service.impl;

import com.fromlabs.inventory.notificationservice.NotificationserviceApplication;
import com.fromlabs.inventory.notificationservice.notification.beans.mapper.MessageValueObjectMapper;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles({"dev","liem-local"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = NotificationserviceApplication.class)
class EmailMessageServiceTest {

    @Autowired
    private EmailMessageService emailMessageService;

    @Mock
    private JavaMailSender mockSender;

    @Autowired
    private MessageValueObjectMapper messageValueObjectMapper;

    @Test
    void given_send_when_messageObjectIsNull_then_throwException() {
        assertThrows(Exception.class,
                () -> this.emailMessageService.send(null),
                "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    void given_send_when_allThingIsRight_then_sendMessageWithNoException() {
        final var messageObject = MessageValueObject
                .messageBuilder().subject("Subject").body("Body")
                .from("From").to("To").build();
        final var mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mockSender.createMimeMessage()).thenReturn(mimeMessage);
        final var mockService = new EmailMessageService(
                this.mockSender, this.messageValueObjectMapper);
        final var actualMessage = assertDoesNotThrow(
                () -> mockService.send(messageObject));
        assertNotNull(actualMessage.getSendAt());
    }

    @Test
    void given_sendDefault_when_messageObjectIsNull_then_throwException() {
        assertThrows(Exception.class,
                () -> this.emailMessageService.sendDefault(null),
                "{javax.validation.constraints.NotNull.message}");
    }

    @Test
    void given_sendDefault_when_allThingIsRight_then_sendMessageWithNoException() {
        final var messageObject = MessageValueObject
                .messageBuilder().subject("Subject").body("Body")
                .from("From").to("To").build();
        final var mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mockSender.createMimeMessage()).thenReturn(mimeMessage);
        final var mockService = new EmailMessageService(
                this.mockSender, this.messageValueObjectMapper);
        final var actualMessage = assertDoesNotThrow(
                () -> mockService.sendDefault(messageObject));
        assertNotNull(actualMessage.getSendAt());
    }
}