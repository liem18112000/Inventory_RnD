package com.fromlabs.inventory.notificationservice.notification.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fromlabs.inventory.notificationservice.NotificationserviceApplication;
import com.fromlabs.inventory.notificationservice.common.dto.SimpleDto;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.mapper.NotificationMapper;
import com.fromlabs.inventory.notificationservice.notification.beans.validator.NotificationValidator;
import com.fromlabs.inventory.notificationservice.notification.event.EventRepository;
import com.fromlabs.inventory.notificationservice.notification.messages.template.MessageTemplateRepository;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationEntity;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationRepository;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationStatus;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationType;
import com.fromlabs.inventory.notificationservice.notification.service.MessageService;
import com.fromlabs.inventory.notificationservice.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"dev","liem-local"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = NotificationserviceApplication.class)
class NotificationServiceImplTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Test
    void given_getStatuses_when_allThingIsRight() {
        final var statuses = this.notificationService.getStatuses();
        final var actualStatusLabel = statuses.stream()
                .map(simpleDto1 -> simpleDto1.getLabel().toString())
                .collect(Collectors.toList());
        final var actualStatusValue = statuses.stream()
                .map(simpleDto -> simpleDto.getValue().toString())
                .collect(Collectors.toList());
        final var expectedStatusLabel = Arrays.stream(NotificationStatus.values())
                .map(s -> s.getStatus().toUpperCase()).collect(Collectors.toList());
        final var expectedStatusValue = Arrays.stream(NotificationStatus.values())
                .map(NotificationStatus::getStatus).collect(Collectors.toList());
        assertTrue(actualStatusLabel.containsAll(expectedStatusLabel));
        assertTrue(expectedStatusLabel.containsAll(actualStatusLabel));
        assertTrue(actualStatusValue.containsAll(expectedStatusValue));
        assertTrue(expectedStatusValue.containsAll(actualStatusValue));
    }

    @Test
    void given_getTypes_when_allThingIsRight() {
        final var types = this.notificationService.getTypes();
        final var actualLabel = types.stream()
                .map(simpleDto1 -> simpleDto1.getLabel().toString())
                .collect(Collectors.toList());
        final var actualValue = types.stream()
                .map(simpleDto -> simpleDto.getValue().toString())
                .collect(Collectors.toList());
        final var expectedLabel = Arrays.stream(NotificationType.values())
                .map(s -> s.getType().toUpperCase()).collect(Collectors.toList());
        final var expectedValue = Arrays.stream(NotificationType.values())
                .map(NotificationType::getType).collect(Collectors.toList());
        assertTrue(actualLabel.containsAll(expectedLabel));
        assertTrue(expectedLabel.containsAll(actualLabel));
        assertTrue(actualValue.containsAll(expectedValue));
        assertTrue(expectedValue.containsAll(actualValue));
    }

    @Test
    void given_getById_when_idIsNegative_then_returnNull()
            throws JsonProcessingException {
        final var entity = this.notificationService.getById(-1L);
        assertNull(entity);
    }

    @Test
    void given_getById_when_idIsNull_then_returnNull()
            throws JsonProcessingException {
        final var entity = this.notificationService.getById(null);
        assertNull(entity);
    }

    @Test
    void given_getById_when_idIsValid_then_returnDto()
            throws JsonProcessingException {
        final var actualEntity = this.notificationRepository.findAll().get(0);
        final var actualDto = assertDoesNotThrow(() ->
                this.notificationService.getById(actualEntity.getId()));
        final var expectedDto = this.notificationMapper.toDto(actualEntity);
        assertEquals(actualDto.getId(), expectedDto.getId());
        assertEquals(actualDto.getName(), expectedDto.getName());
    }

    @Test
    void given_getByIdWithException_when_idIsNegative_then_throwException() {
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.getByIdWithException(-1L),
                "Notification id is not positive");
    }

    @Test
    void given_getByIdWithException_when_idIsNull_then_throwException() {
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.getByIdWithException(null),
                "Notification id is null");
    }

    @Test
    void given_getByIdWithException_when_idIsValid_then_getDTO()
            throws JsonProcessingException {
        final var entity = this.notificationRepository.findAll().get(0);
        var expectedDto = this.notificationMapper.toDto(entity);
        var actualDto = assertDoesNotThrow(() -> this.notificationService
                .getByIdWithException(entity.getId()));
        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getName(), actualDto.getName());
    }

    @Test
    void getPageWithFilter() {
    }

    @Test
    void save() {
    }

    @Test
    void given_updateBasicInformation_when_nameIsNull_then_throwException() {
        final var dto = NotificationDTO.builder().id(1L).name(null).build();
        assertNull(dto.getName());
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateBasicInformation(dto),
                "Notification name is null or blank");
    }

    @Test
    void given_updateBasicInformation_when_nameIsBlank_then_throwException() {
        final var dto = NotificationDTO.builder().id(1L).name("").build();
        assertFalse(StringUtils.hasText(dto.getName()));
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateBasicInformation(dto),
                "Notification name is null or blank");
    }

    @Test
    void given_updateType_when_typeIsNull_then_throwException() {
        final var dto = NotificationDTO.builder().id(1L).type(null).build();
        assertNull(dto.getType());
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateType(dto),
                "Notification type is not exist");
    }

    @Test
    void given_updateType_when_typeIsBlank_then_throwException() {
        final var dto = NotificationDTO.builder().id(1L).type("").build();
        assertFalse(StringUtils.hasText(dto.getType()));
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateType(dto),
                "Notification type is not exist");
    }

    @Test
    void given_updateType_when_typeIsNotExist_then_throwException() {
        final var dto = NotificationDTO.builder().id(1L)
                .type("not_exist").build();
        assertTrue(StringUtils.hasText(dto.getType()));
        assertTrue(Arrays.stream(NotificationType.values())
                .noneMatch(s -> s.getType().equals("not_exist")));
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateType(dto),
                "Notification type is not exist");
    }

    @Test
    void updateMessage() {
    }

    @Test
    void updateStatusToSending() {
    }

    @Test
    void sendNotification() {
    }

    @Test
    void sendAllNotification() {
    }

    @Test
    void delete() {
    }
}