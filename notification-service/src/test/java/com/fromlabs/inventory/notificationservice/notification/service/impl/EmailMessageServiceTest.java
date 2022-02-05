package com.fromlabs.inventory.notificationservice.notification.service.impl;

import com.fromlabs.inventory.notificationservice.NotificationserviceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"dev","liem-local"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = NotificationserviceApplication.class)
class EmailMessageServiceTest {

    @Test
    void send() {
    }

    @Test
    void sendDefault() {
    }
}