package com.fromlabs.inventory.notificationservice.notification.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fromlabs.inventory.notificationservice.NotificationserviceApplication;
import com.fromlabs.inventory.notificationservice.common.dto.SimpleDto;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.mapper.MessageValueObjectMapper;
import com.fromlabs.inventory.notificationservice.notification.beans.mapper.NotificationMapper;
import com.fromlabs.inventory.notificationservice.notification.beans.validator.NotificationValidator;
import com.fromlabs.inventory.notificationservice.notification.event.EventEntity;
import com.fromlabs.inventory.notificationservice.notification.event.EventRepository;
import com.fromlabs.inventory.notificationservice.notification.event.EventType;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import com.fromlabs.inventory.notificationservice.notification.messages.template.MessageTemplateEntity;
import com.fromlabs.inventory.notificationservice.notification.messages.template.MessageTemplateRepository;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationEntity;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationRepository;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationStatus;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationType;
import com.fromlabs.inventory.notificationservice.notification.service.MessageService;
import com.fromlabs.inventory.notificationservice.notification.service.NotificationService;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import javax.xml.transform.Templates;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Autowired
    private NotificationValidator notificationValidator;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageTemplateRepository templateRepository;

    @Autowired
    private MessageValueObjectMapper messageMapper;

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
    void given_getById_when_idIsNull_then_returnNull() {
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> this.notificationService.getById(null),
                "The given id must not be null!");
    }

    @Test
    void given_getById_when_idIsValid_then_returnDto() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var actualDto = assertDoesNotThrow(() ->
                this.notificationService.getById(entity.getId()));
        assertEquals(actualDto.getId(), entity.getId());
        assertEquals(actualDto.getName(), entity.getName());
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
    void given_getByIdWithException_when_idIsValid_then_getDTO() {
        final var entity = this.notificationRepository.findAll().get(0);
        var expectedDto = assertDoesNotThrow(
                () -> this.notificationMapper.toDto(entity));
        var actualDto = assertDoesNotThrow(() ->  this.notificationService
                .getByIdWithException(entity.getId()));
        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getName(), actualDto.getName());
    }

    @Test
    void given_getPageWithFilter_when_pageableIsNull_then_getPageDefault() {
        final var dto = NotificationDTO.builder()
                .name("").description("").type("").status("").build();
        final var pages = this.notificationService
                .getPageWithFilter(dto, null);
        assertNotNull(pages);
        assertEquals(10, pages.getSize());
        assertEquals(0, pages.getNumber());
    }

    @Test
    void given_getPageWithFilter_when_dtoIsNull_then_getPageDefault() {
        final var pageable = PageRequest.of(1, 5, Sort.Direction.DESC, "name");
        final var pages = this.notificationService
                .getPageWithFilter(null, pageable);
        assertNotNull(pages);
        assertEquals(5, pages.getSize());
        assertEquals(1, pages.getNumber());
        assertEquals(pages.getPageable().getSort(), Sort.by(Sort.Direction.DESC, "name"));
    }

    @Test
    void given_getPageWithFilter_when_filterByName_then_getPageWithFilteredName() {
        final var pageable = PageRequest.of(0, 10);
        final var entity = this.notificationRepository.findAll().get(0);
        final var dto = NotificationDTO.builder().name(entity.getName()).build();
        final var pages = this.notificationService
                .getPageWithFilter(dto, pageable);
        assertNotNull(pages);
        assertTrue(pages.getTotalElements() > 0);
        assertEquals(10, pages.getSize());
        assertEquals(0, pages.getNumber());
        assertTrue(pages.stream().allMatch(page ->
                Objects.equals(page.getName(), entity.getName()) ||
                page.getName().contains(entity.getName())));
    }

    @Test
    void given_getPageWithFilter_when_filterByDescription_then_getPageWithFilteredDescription() {
        final var pageable = PageRequest.of(0, 10);
        final var entity = this.notificationRepository.findAll().get(0);
        final var dto = NotificationDTO.builder()
                .description(entity.getDescription()).build();
        final var pages = this.notificationService
                .getPageWithFilter(dto, pageable);
        assertNotNull(pages);
        assertTrue(pages.getTotalElements() > 0);
        assertEquals(10, pages.getSize());
        assertEquals(0, pages.getNumber());
        assertTrue(pages.stream().allMatch(page ->
                Objects.equals(page.getDescription(), entity.getDescription()) ||
                page.getDescription().contains(entity.getDescription())));
    }

    @Test
    void given_getPageWithFilter_when_filterByStatus_then_getPageWithFilteredStatus() {
        final var pageable = PageRequest.of(0, 10);
        final var entity = this.notificationRepository.findAll().get(0);
        final var dto = NotificationDTO.builder().status(entity.getStatus()).build();
        final var pages = this.notificationService
                .getPageWithFilter(dto, pageable);
        assertNotNull(pages);
        assertTrue(pages.getTotalElements() > 0);
        assertEquals(10, pages.getSize());
        assertEquals(0, pages.getNumber());
        assertTrue(pages.stream().allMatch(page ->
                Objects.equals(page.getStatus(), entity.getStatus()) ||
                        page.getStatus().contains(entity.getStatus())));
    }

    @Test
    void given_getPageWithFilter_when_filterByEvent_then_getPageWithFilteredEvent() {
        final var pageable = PageRequest.of(0, 10);
        final var entity = this.notificationRepository.findAll().get(0);
            final var dto = NotificationDTO.builder()
                    .event(EventDTO.builder().id(entity.getEvent().getId()).build()).build();
        final var pages = this.notificationService
                .getPageWithFilter(dto, pageable);
        assertNotNull(pages);
        assertTrue(pages.getTotalElements() > 0);
        assertEquals(10, pages.getSize());
        assertEquals(0, pages.getNumber());
        assertTrue(pages.stream().allMatch(page ->
                Objects.equals(page.getEvent().getId(), entity.getEvent().getId())));
    }

    @Test
    void given_getPageWithFilter_when_filterByType_then_getPageWithFilteredType() {
        final var pageable = PageRequest.of(0, 10);
        final var entity = this.notificationRepository.findAll().get(0);
        final var dto = NotificationDTO.builder().type(entity.getType()).build();
        final var pages = this.notificationService
                .getPageWithFilter(dto, pageable);
        assertNotNull(pages);
        assertTrue(pages.getTotalElements() > 0);
        assertEquals(10, pages.getSize());
        assertEquals(0, pages.getNumber());
        assertTrue(pages.stream().allMatch(page ->
                Objects.equals(page.getType(), entity.getType()) ||
                        page.getType().contains(entity.getType())));
    }

    @Test
    void given_save_when_requestIsNull_then_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.save(null),
                "Notification DTO is null");
    }

    @Test
    void given_save_when_requestNameIsNull_then_thenThrowException() {
        final var request = NotificationDTO.builder()
                .name(null).description("desc").event(EventDTO.builder().id(1L).build())
                .message(MessageValueObject.messageBuilder().build())
                .type(NotificationType.EMAIL.getType()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.save(request),
                "Notification name is null or blank");
    }

    @Test
    void given_save_when_requestMessageIsNull_then_thenThrowException() {
        final var request = NotificationDTO.builder()
                .name("name").description("desc").event(EventDTO.builder().id(1L).build())
                .message(null).type(NotificationType.EMAIL.getType()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.save(request),
                "Message is null");
    }

    @Test
    void given_save_when_requestEventIdIsNull_then_thenThrowException() {
        final var request = NotificationDTO.builder()
                .name("name").description("desc").event(EventDTO.builder().build())
                .message(MessageValueObject.messageBuilder().build())
                .type(NotificationType.EMAIL.getType()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.save(request),
                "Event id is null");
    }

    @Test
    void given_save_when_requestEventIdTypeNot_then_thenThrowException() {
        final var request = NotificationDTO.builder()
                .name("name").description("desc").event(EventDTO.builder().build())
                .message(MessageValueObject.messageBuilder().build())
                .type(null).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.save(request),
                "Notification type is not exist");
    }

    @Test
    void given_save_when_requestIsValid_then_saveWithNoException() {
        final var mockRepo = mock(NotificationRepository.class);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("From").to("To").build();
        final var event = EventDTO.builder().id(1L).build();
        final var type = NotificationType.EMAIL.getType();
        final var request = NotificationDTO.builder()
                .name("name").description("desc").event(event)
                .message(messageObject).type(type).build();
        final var mockService = new NotificationServiceImpl(
                mockRepo, notificationMapper, notificationValidator,
                eventRepository, messageService, templateRepository);
        assertDoesNotThrow(() -> mockService.save(request));
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
    void given_updateMessage_when_allThingIsRight_then_updateMessageWithNoException() {
        final var mockRepo = mock(NotificationRepository.class);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("From").to("To").build();
        var entity = new NotificationEntity();
        entity.setId(1L);
        when(mockRepo.findById(entity.getId())).thenReturn(Optional.of(entity));
        final var mockService = new NotificationServiceImpl(
                mockRepo, notificationMapper, notificationValidator,
                eventRepository, messageService, templateRepository);
        final var dto = NotificationDTO.builder()
                .message(messageObject).id(entity.getId()).build();
        assertDoesNotThrow(() -> mockService.updateMessage(dto));
    }

    @Test
    void given_updateMessage_when_messageIsNull_then_throwException() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var dto = NotificationDTO.builder()
                .message(null).id(entity.getId()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateMessage(dto),
                "Message is null");
    }

    @Test
    void given_updateMessage_when_messageSubjectIsNull_then_throwException() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject(null).body("Body").from("From").to("To").build();
        final var dto = NotificationDTO.builder()
                .message(messageObject).id(entity.getId()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateMessage(dto),
                "Message subject is null or blank");
    }

    @Test
    void given_updateMessage_when_messageSubjectIsBlank_then_throwException() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("").body("Body").from("From").to("To").build();
        final var dto = NotificationDTO.builder()
                .message(messageObject).id(entity.getId()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateMessage(dto),
                "Message subject is null or blank");
    }

    @Test
    void given_updateMessage_when_messageBodyIsNull_then_throwException() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body(null).from("From").to("To").build();
        final var dto = NotificationDTO.builder()
                .message(messageObject).id(entity.getId()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateMessage(dto),
                "Message body is null or blank");
    }

    @Test
    void given_updateMessage_when_messageBodyIsBlank_then_throwException() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("").from("From").to("To").build();
        final var dto = NotificationDTO.builder()
                .message(messageObject).id(entity.getId()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateMessage(dto),
                "Message body is null or blank");
    }

    @Test
    void given_updateMessage_when_messageFromIsNull_then_throwException() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from(null).to("To").build();
        final var dto = NotificationDTO.builder()
                .message(messageObject).id(entity.getId()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateMessage(dto),
                "Message from is null or blank");
    }

    @Test
    void given_updateMessage_when_messageFromIsBlank_then_throwException() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("").to("To").build();
        final var dto = NotificationDTO.builder()
                .message(messageObject).id(entity.getId()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateMessage(dto),
                "Message from is null or blank");
    }

    @Test
    void given_updateMessage_when_messageToIsNull_then_throwException() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("From").to(null).build();
        final var dto = NotificationDTO.builder()
                .message(messageObject).id(entity.getId()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateMessage(dto),
                "Message to is null or blank");
    }

    @Test
    void given_updateMessage_when_messageToIsBlank_then_throwException() {
        final var entity = this.notificationRepository.findAll().get(0);
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("From").to("").build();
        final var dto = NotificationDTO.builder()
                .message(messageObject).id(entity.getId()).build();
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.updateMessage(dto),
                "Message to is null or blank");
    }

    @Test
    void given_updateStatusToSending_when_allThingIsRight_then_updateStatusWithNoException() {
        final var mockRepo = mock(NotificationRepository.class);
        var entity = new NotificationEntity();
        entity.setId(1L);
        entity.setStatus(NotificationStatus.NEW.getStatus());
        when(mockRepo.findById(entity.getId())).thenReturn(Optional.of(entity));
        final var mockService = new NotificationServiceImpl(
                mockRepo, notificationMapper, notificationValidator,
                eventRepository, messageService, templateRepository);
        assertDoesNotThrow(() -> mockService.updateStatusToSending(entity.getId()));
    }

    @Test
    void given_updateStatusToSending_when_statusIsCompleted_then_updateStatusWithNoException() {
        final var mockRepo = mock(NotificationRepository.class);
        var entity = new NotificationEntity();
        entity.setId(1L);
        entity.setStatus(NotificationStatus.COMPLETE.getStatus());
        when(mockRepo.findById(entity.getId())).thenReturn(Optional.of(entity));
        final var mockService = new NotificationServiceImpl(
                mockRepo, notificationMapper, notificationValidator,
                eventRepository, messageService, templateRepository);
        assertThrows(IllegalStateException.class,
                () -> mockService.updateStatusToSending(entity.getId()),
                "Notification has been sent successfully");
    }

    @Test
    void given_updateStatusToSending_when_statusIsFailed_then_updateStatusWithNoException() {
        final var mockRepo = mock(NotificationRepository.class);
        var entity = new NotificationEntity();
        entity.setId(1L);
        entity.setStatus(NotificationStatus.FAILED.getStatus());
        when(mockRepo.findById(entity.getId())).thenReturn(Optional.of(entity));
        final var mockService = new NotificationServiceImpl(
                mockRepo, notificationMapper, notificationValidator,
                eventRepository, messageService, templateRepository);
        assertThrows(IllegalStateException.class,
                () -> mockService.updateStatusToSending(entity.getId()),
                "Notification has been sent failed");
    }

    @Test
    void given_sendNotification_when_allThingIsRightAndTypeIsEmail_then_sendEmailNotificationWithNoException() {
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("From").to("To").build();
        var entity = new NotificationEntity();
        entity.setId(1L);
        entity.setType(NotificationType.EMAIL.getType());
        entity.setStatus(NotificationStatus.SENDING.getStatus());
        entity.setMessage(assertDoesNotThrow(() -> this.messageMapper.toJSON(messageObject)));
        var event = new EventEntity();
        event.setId(3L);
        event.setName("Event");
        event.setEventType(EventType.INGREDIENT_ITEM_LOW_STOCK.name());
        entity.setEvent(event);
        final var mockNotificationRepo = mock(NotificationRepository.class);
        when(mockNotificationRepo.findById(entity.getId())).thenReturn(Optional.of(entity));

        var messageTemplate = new MessageTemplateEntity();
        messageTemplate.setContent("HTML Mail content");
        messageTemplate.setId(2L);
        messageTemplate.setName(event.getEventType());
        final var mockTemplateRepo = mock(MessageTemplateRepository.class);
        when(mockTemplateRepo.findByName(messageTemplate.getName()))
                .thenReturn(Optional.of(messageTemplate));

        final var mockMailSender = mock(JavaMailSender.class);
        final var mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mockMailSender.createMimeMessage()).thenReturn(mimeMessage);
        final var mockMessageService = new EmailMessageService(mockMailSender, this.messageMapper);
        final var mockService = new NotificationServiceImpl(
                mockNotificationRepo, notificationMapper, notificationValidator,
                eventRepository, mockMessageService, mockTemplateRepo);

        assertDoesNotThrow(() -> mockService.sendNotification(entity.getId()));
    }

    @Test
    void given_sendNotification_when_notificationTypeNotExist_then_throwException() {
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("From").to("To").build();
        var entity = new NotificationEntity();
        entity.setId(1L);
        entity.setType("Unknown");
        entity.setStatus(NotificationStatus.SENDING.getStatus());
        entity.setMessage(assertDoesNotThrow(() -> this.messageMapper.toJSON(messageObject)));
        var event = new EventEntity();
        event.setId(3L);
        event.setName("Event");
        event.setEventType(EventType.INGREDIENT_ITEM_LOW_STOCK.name());
        entity.setEvent(event);
        final var mockNotificationRepo = mock(NotificationRepository.class);
        when(mockNotificationRepo.findById(entity.getId())).thenReturn(Optional.of(entity));

        var messageTemplate = new MessageTemplateEntity();
        messageTemplate.setContent("HTML Mail content");
        messageTemplate.setId(2L);
        messageTemplate.setName(event.getEventType());
        final var mockTemplateRepo = mock(MessageTemplateRepository.class);
        when(mockTemplateRepo.findByName(messageTemplate.getName()))
                .thenReturn(Optional.of(messageTemplate));

        final var mockMailSender = mock(JavaMailSender.class);
        final var mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mockMailSender.createMimeMessage()).thenReturn(mimeMessage);
        final var mockMessageService = new EmailMessageService(mockMailSender, this.messageMapper);
        final var mockService = new NotificationServiceImpl(
                mockNotificationRepo, notificationMapper, notificationValidator,
                eventRepository, mockMessageService, mockTemplateRepo);

        assertThrows(IllegalArgumentException.class,
                () -> mockService.sendNotification(entity.getId()),
                String.format("Message type is not valid: %s", entity.getType()));
    }

    @Test
    void given_sendNotification_when_notificationTypeIsSms_then_throwException() {
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("From").to("To").build();
        var entity = new NotificationEntity();
        entity.setId(1L);
        entity.setType(NotificationType.SMS.getType());
        entity.setStatus(NotificationStatus.SENDING.getStatus());
        entity.setMessage(assertDoesNotThrow(() -> this.messageMapper.toJSON(messageObject)));
        var event = new EventEntity();
        event.setId(3L);
        event.setName("Event");
        event.setEventType(EventType.INGREDIENT_ITEM_LOW_STOCK.name());
        entity.setEvent(event);
        final var mockNotificationRepo = mock(NotificationRepository.class);
        when(mockNotificationRepo.findById(entity.getId())).thenReturn(Optional.of(entity));

        var messageTemplate = new MessageTemplateEntity();
        messageTemplate.setContent("HTML Mail content");
        messageTemplate.setId(2L);
        messageTemplate.setName(event.getEventType());
        final var mockTemplateRepo = mock(MessageTemplateRepository.class);
        when(mockTemplateRepo.findByName(messageTemplate.getName()))
                .thenReturn(Optional.of(messageTemplate));

        final var mockMailSender = mock(JavaMailSender.class);
        final var mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mockMailSender.createMimeMessage()).thenReturn(mimeMessage);
        final var mockMessageService = new EmailMessageService(mockMailSender, this.messageMapper);
        final var mockService = new NotificationServiceImpl(
                mockNotificationRepo, notificationMapper, notificationValidator,
                eventRepository, mockMessageService, mockTemplateRepo);

        assertThrows(NotImplementedException.class,
                () -> mockService.sendNotification(entity.getId()),
                "Feature is under implemented");
    }

    @Test
    void given_sendNotification_when_notApplyTemplate_then_continueSendTemplate() {
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("From").to("To").build();
        var entity = new NotificationEntity();
        entity.setId(1L);
        entity.setType(NotificationType.EMAIL.getType());
        entity.setStatus(NotificationStatus.SENDING.getStatus());
        entity.setMessage(assertDoesNotThrow(() -> this.messageMapper.toJSON(messageObject)));
        var event = new EventEntity();
        event.setId(3L);
        event.setName("Event");
        event.setEventType(EventType.INGREDIENT_ITEM_LOW_STOCK.name());
        entity.setEvent(event);
        final var mockNotificationRepo = mock(NotificationRepository.class);
        when(mockNotificationRepo.findById(entity.getId())).thenReturn(Optional.of(entity));

        var messageTemplate = new MessageTemplateEntity();
        messageTemplate.setContent("HTML Mail content");
        messageTemplate.setId(2L);
        messageTemplate.setName(event.getEventType());
        final var mockTemplateRepo = mock(MessageTemplateRepository.class);
        when(mockTemplateRepo.findByName(messageTemplate.getName()))
                .thenReturn(Optional.empty());

        final var mockMailSender = mock(JavaMailSender.class);
        final var mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mockMailSender.createMimeMessage()).thenReturn(mimeMessage);
        final var mockMessageService = new EmailMessageService(mockMailSender, this.messageMapper);
        final var mockService = new NotificationServiceImpl(
                mockNotificationRepo, notificationMapper, notificationValidator,
                eventRepository, mockMessageService, mockTemplateRepo);

        assertDoesNotThrow(() -> mockService.sendNotification(entity.getId()));
    }

    @Test
    void given_sendNotification_when_mailExceptionOccur_then_updateAfterSendMessageFailed()
            throws MessagingException, JsonProcessingException {
        final var messageObject = MessageValueObject.messageBuilder()
                .subject("Subject").body("Body").from("From").to("To").build();
        var entity = new NotificationEntity();
        entity.setId(1L);
        entity.setType(NotificationType.EMAIL.getType());
        entity.setStatus(NotificationStatus.SENDING.getStatus());
        entity.setMessage(assertDoesNotThrow(() -> this.messageMapper.toJSON(messageObject)));
        var event = new EventEntity();
        event.setId(3L);
        event.setName("Event");
        event.setEventType(EventType.INGREDIENT_ITEM_LOW_STOCK.name());
        entity.setEvent(event);
        final var mockNotificationRepo = mock(NotificationRepository.class);
        when(mockNotificationRepo.findById(entity.getId())).thenReturn(Optional.of(entity));

        var messageTemplate = new MessageTemplateEntity();
        messageTemplate.setContent("HTML Mail content");
        messageTemplate.setId(2L);
        messageTemplate.setName(event.getEventType());
        final var mockTemplateRepo = mock(MessageTemplateRepository.class);
        when(mockTemplateRepo.findByName(messageTemplate.getName()))
                .thenReturn(Optional.empty());

        final var mockMailSender = mock(JavaMailSender.class);
        final var mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mockMailSender.createMimeMessage()).thenReturn(mimeMessage);
        final var mockMessageService = new EmailMessageService(mockMailSender, this.messageMapper);
        when(mockMessageService.send(messageObject)).thenThrow(MailException.class);
        final var mockService = new NotificationServiceImpl(
                mockNotificationRepo, notificationMapper, notificationValidator,
                eventRepository, mockMessageService, mockTemplateRepo);

        assertDoesNotThrow(() -> mockService.sendNotification(entity.getId()));
    }

    @Test
    void sendAllNotification() {
    }

    @Test
    void given_delete_when_entityIsNotExist_then_throwException() {
        final var entityId = System.currentTimeMillis();
        assertThrows(EntityNotFoundException.class,
                () -> this.notificationService.delete(entityId),
                String.format("Entity is not found by id : %s", String.valueOf(entityId)));
    }

    @Test
    void given_delete_when_idIsNegative_then_throwException() {
        final var entityId = -1L;
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.delete(entityId),
                "Notification id is not positive");
    }

    @Test
    void given_delete_when_idIsNull_then_throwException() {
        assertThrows(IllegalArgumentException.class,
                () -> this.notificationService.delete(null),
                "Notification id is null");
    }

    @Test
    void given_delete_when_allThingIsRight_then_deleteEntityById() {
        final var mockRepo = mock(NotificationRepository.class);
        final var mockService = new NotificationServiceImpl(
                mockRepo, notificationMapper, notificationValidator,
                eventRepository, messageService, templateRepository);
        final var entity = this.notificationRepository.findAll().get(0);
        when(mockRepo.findById(entity.getId())).thenReturn(Optional.of(entity));
        assertDoesNotThrow(() -> mockService.delete(entity.getId()));
    }
}