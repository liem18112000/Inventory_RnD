package com.fromlabs.inventory.notificationservice.notification.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fromlabs.inventory.notificationservice.common.dto.SimpleDto;
import com.fromlabs.inventory.notificationservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.BatchSendNotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.mapper.MessageValueObjectMapper;
import com.fromlabs.inventory.notificationservice.notification.beans.mapper.NotificationMapper;
import com.fromlabs.inventory.notificationservice.notification.beans.validator.NotificationValidator;
import com.fromlabs.inventory.notificationservice.notification.event.EventEntity;
import com.fromlabs.inventory.notificationservice.notification.event.EventRepository;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import com.fromlabs.inventory.notificationservice.notification.messages.template.MessageTemplateRepository;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationEntity;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationRepository;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationStatus;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationType;
import com.fromlabs.inventory.notificationservice.notification.service.MessageService;
import com.fromlabs.inventory.notificationservice.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.notificationservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.notificationservice.common.specifications.SearchCriteria.criteriaEqual;

/**
 * {@inheritDoc}
 */
@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
public class NotificationServiceImpl implements NotificationService {

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
    private MessageValueObjectMapper messageMapper;

    @Autowired
    private MessageTemplateRepository templateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<SimpleDto> getStatuses() {
       return Arrays.stream(NotificationStatus.values())
               .map(status -> new SimpleDto(status.getStatus(), status.getStatus().toUpperCase()))
               .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    public Set<SimpleDto> getTypes() {
        return Arrays.stream(NotificationType.values())
                .map(type -> new SimpleDto(type.getType(), type.getType().toUpperCase()))
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationDTO getById(final Long id)
            throws IllegalArgumentException, JsonProcessingException {
        log.info("Start get notification by id: {}", id);
        final var entity = this.notificationRepository.findById(id).orElse(null);
        final var dto = this.notificationMapper.toDto(entity);
        log.info("End get notification by id: {}", dto);
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationDTO getByIdWithException(final Long id)
            throws EntityNotFoundException, IllegalArgumentException,
            JsonProcessingException {
        log.info("Start get notification by id: {}", id);
        this.notificationValidator.validateNotificationId(id);
        final var entity = this.getRawEntityById(id);
        final var dto = this.notificationMapper.toDto(entity);
        log.info("End get notification by id: {}", dto);
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<NotificationDTO> getPageWithFilter(
            final NotificationDTO notificationDTO, final Pageable pageable)
            throws IllegalArgumentException {
        log.info("Start get page notification: {} - {}", notificationDTO, pageable);
        final var requestPageable = this.getPageableDefault(pageable);
        Page<NotificationEntity> pages;
        if (Objects.isNull(notificationDTO)) {
            pages = this.notificationRepository.findAll(requestPageable);
        } else {
            final var specification = getSpecification(notificationDTO);
            pages = this.notificationRepository.findAll(specification, requestPageable);
        }
        log.info("End get page notification");
        return pages.map(entity -> {
            try {
                return this.notificationMapper.toDto(entity);
            } catch (JsonProcessingException e) {
                log.error("Notification DTO conversion failed: {}", e.getMessage());
                return null;
            }
        });
    }

    /**
     * Get pageable default
     * @param pageable Pageable
     * @return Pageable
     */
    private Pageable getPageableDefault(final Pageable pageable) {
        return Objects.isNull(pageable) ? PageRequest.of(0, 10) : pageable;
    }

    /**
     * Get string default
     * @param string String
     * @return String
     */
    private String getStringDefault(final String string) {
        return StringUtils.hasText(string) ? string : "";
    }

    /**
     * Gte specification of notification
     * @param dto NotificationDTO
     * @return Specification
     */
    private Specification<NotificationEntity> getSpecification(final NotificationDTO dto) {
        var spec = BaseSpecification.<NotificationEntity>
                        Spec(criteriaEqual("name", getStringDefault(dto.getName())))
                .and(Spec(criteriaEqual("description", getStringDefault(dto.getDescription()))))
                .and(Spec(criteriaEqual("type", getStringDefault(dto.getType()))))
                .and(Spec(criteriaEqual("status", getStringDefault(dto.getStatus()))));
        if (Objects.nonNull(dto.getEvent()) && Objects.nonNull(dto.getEvent().getId())) {
            final var event = this.eventRepository
                    .findById(dto.getEvent().getId()).orElse(null);
            if (Objects.nonNull(event)) {
                spec.and(Spec(criteriaEqual("event", event)));
            }
        }
        return spec;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    @Override
    public NotificationDTO save(final NotificationDTO dto)
            throws IllegalArgumentException, JsonProcessingException,
            EntityNotFoundException, IllegalStateException {
        log.info("Start save notification: {}", dto);
        this.notificationValidator.validateSaveNotification(dto);
        final var entity = createOrModifyBasicInformation(dto);
        final var savedEntity = this.notificationRepository.save(entity);
        final var savedDto = this.notificationMapper.toDto(savedEntity);
        log.info("End save notification: {}", savedDto);
        return savedDto;
    }

    /**
     * Create or update basic information of notification based on request
     * @param dto NotificationDTO
     * @return NotificationEntity
     * @throws JsonProcessingException when convert to DTO failed
     */
    private NotificationEntity createOrModifyBasicInformation(
            @NotNull final NotificationDTO dto)
            throws JsonProcessingException {
        NotificationEntity entity;
        final var entityId = dto.getId();

        // Create new notification
        if (Objects.isNull(entityId)) {
            final var event = getEventEntity(dto.getEvent().getId());
            entity = this.notificationMapper.toEntity(dto, event);
            entity.updateAfterCreated();
            entity.activate();
            log.info("A new notification is created");
        }

        // Update basic information of notification
        else {
            entity = getRawEntityById(entityId);
            entity.updateBasicInformation(dto);
            log.info("An exist notification is updated");
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    @Override
    public NotificationDTO updateMessage(final NotificationDTO dto)
            throws IllegalArgumentException, EntityNotFoundException,
            JsonProcessingException, IllegalStateException {
        log.info("Start update notification message: {}", dto);
        this.notificationValidator.validateUpdateMessage(dto);
        final var entityId = dto.getId();
        final var entity = this.getRawEntityById(entityId);
        entity.updateMessage(dto.getMessage());
        final var savedEntity = this.notificationRepository.save(entity);
        final var savedDto = this.notificationMapper.toDto(savedEntity);
        log.info("End update notification message: {}", savedDto);
        return savedDto;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    @Override
    public NotificationDTO updateStatusToSending(final Long id)
            throws IllegalStateException, JsonProcessingException,
            EntityNotFoundException, IllegalArgumentException {
        log.info("Start update status to sending by id: {}", id);
        this.notificationValidator.validateNotificationId(id);
        final var entity = this.getRawEntityById(id);
        entity.updateStatusToSending();
        final var savedEntity = this.notificationRepository.save(entity);
        final var savedDto = this.notificationMapper.toDto(savedEntity);
        log.info("End update status to sending by id: {} - {}", id, savedDto);
        return savedDto;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    @Override
    public NotificationDTO sendNotification(final Long id)
            throws JsonProcessingException, IllegalArgumentException,
            IllegalStateException, MailException {
        log.info("Start send notification by id: {}", id);
        this.notificationValidator.validateNotificationId(id);
        var dto = this.getByIdWithException(id);
        if (!dto.getStatus().equals(NotificationStatus.SENDING.getStatus())) {
            throw new IllegalArgumentException(String.format(
                    "Notification message is not set to be sent: %s", dto.getStatus()));
        }
        var message = dto.getMessage();
        final var originalMessageBody = message.getBody();
        applyMessageTemplate(dto, message);
        try {
            final var messageType = dto.getType();
            try {
                final var sentMessage = sendMessageWithType(message, messageType);
                log.info("Sent notification message success: {}", sentMessage);
            } catch (MessagingException exception) {
                message.setBody(originalMessageBody);
                final var sentMessage = sentDefaultMessageWithType(message, messageType);
                log.warn("Sent notification message with HTML failed, send raw message: {}", sentMessage);
            }
            final var updatedMessageDto = this.updateAfterSendMessageSuccess(dto);
            log.info("End send notification by id: {}", updatedMessageDto);
            return updatedMessageDto;
        } catch (MailException exception) {
            log.info("Sent notification message failed: {}", message);
            final var updatedMessageDto = this.updateAfterSendMessageFailed(dto);
            log.info("End send notification by id: {}", updatedMessageDto);
            return updatedMessageDto;
        }
    }

    /**
     * Send default message with notification type.
     * This function is invoked when the primary
     * send method is failed with mail exception.
     * @param message MessageValueObject
     * @param type Notification type
     * @return MessageValueObject
     * @throws JsonProcessingException when to convert message from string to value object
     * @throws IllegalArgumentException when message type is not valid
     */
    private MessageValueObject sentDefaultMessageWithType(
            @NotNull final MessageValueObject message, @NotNull final String type)
            throws JsonProcessingException, IllegalArgumentException {
        if (type.equals(NotificationType.EMAIL.getType())) {
            log.info("Send email message");
            return this.messageService.sendDefault(message);
        } else if (type.equals(NotificationType.SMS.getType())) {
            // TODO: add sms message method
            log.info("Send SMS message");
            return new MessageValueObject();
        } else {
            log.error("Message type is not valid: {}", type);
            throw new IllegalArgumentException(
                    String.format("Message type is not valid: %s", type));
        }
    }

    /**
     * Send message with notification type.
     * @param message MessageValueObject
     * @param type Notification type
     * @return MessageValueObject
     * @throws JsonProcessingException when to convert message from string to value object
     * @throws MessagingException when message manipulation is failed
     * @throws IllegalArgumentException when message type is not valid
     */
    private MessageValueObject sendMessageWithType(
            final @NotNull MessageValueObject message, @NotNull final String type)
            throws JsonProcessingException, MessagingException, IllegalArgumentException {
        if (type.equals(NotificationType.EMAIL.getType())) {
            log.info("Send email message");
            return this.messageService.send(message);
        } else if (type.equals(NotificationType.SMS.getType())) {
            // TODO: add sms message method
            log.info("Send SMS message");
            throw new NotImplementedException("Feature is under implemented");
        } else {
            log.error("Message type is not valid: {}", type);
            throw new IllegalArgumentException(
                    String.format("Message type is not valid: %s", type));
        }
    }

    /**
     * Apply message template
     * @param dto NotificationDTO
     * @param message MessageValueObject
     */
    private void applyMessageTemplate(
            final @NotNull NotificationDTO dto,
            @NotNull MessageValueObject message) {
        final var messageTemplate =
                this.templateRepository.findByName(dto.getEvent().getEventType());
        if (messageTemplate.isPresent()) {
            log.info("Notification template message applied");
            // TODO: Try to use template engine
//            final var templateString = String.format(
//                    messageTemplate.get().getContent(),
//                    message.getBody());
            final var templateString = messageTemplate.get().getContent();
            message.setBody(templateString);
        } else {
            log.warn("Notification template message is not found");
        }
    }

    /**
     * Update notification after send message successfully
     * @param dto NotificationDTO
     * @return NotificationDTO
     * @throws EntityNotFoundException when entity is not found with dto id
     * @throws JsonProcessingException when convert entity to dto failed
     */
    private NotificationDTO updateAfterSendMessageSuccess(
            final @NotNull NotificationDTO dto)
            throws EntityNotFoundException, JsonProcessingException {
        var entity = this.getRawEntityById(dto.getId());
        entity.updateAfterSendMessage();
        final var savedEntity = this.notificationRepository.save(entity);
        return this.notificationMapper.toDto(savedEntity);
    }

    /**
     * Update notification after send message failed
     * @param dto NotificationDTO
     * @return NotificationDTO
     * @throws EntityNotFoundException when entity is not found with dto id
     * @throws JsonProcessingException when convert entity to dto failed
     */
    private NotificationDTO updateAfterSendMessageFailed(
            final @NotNull NotificationDTO dto)
            throws EntityNotFoundException, JsonProcessingException {
        var entity = this.getRawEntityById(dto.getId());
        entity.updateStatusToFailed();
        final var savedEntity = this.notificationRepository.save(entity);
        return this.notificationMapper.toDto(savedEntity);
    }

    /**
     * {@inheritDoc}
     */
    public BatchSendNotificationDTO sendAllNotification(final Integer limit) {
        var trueLimit = 10;
        if (Objects.nonNull(limit)) {
            trueLimit = limit;
        }
        log.info("Start send all notifications with limit : {}", trueLimit);
        final var unsentNotifications = this.notificationRepository
                .findAllByStatusAndActiveIsTrue(NotificationStatus.NEW.getStatus());
        var failedNotifications = new ArrayList<String>();
        final var notifications = unsentNotifications.parallelStream()
                .map(entity -> {
                    try {
                        return this.sendNotification(entity.getId());
                    } catch (Exception exception) {
                        failedNotifications.add(String.format("Notification with id - %s : %s",
                                entity.getId().toString(), exception.getMessage()));
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
        log.info("End send all notifications with success: {}, total: {}",
                notifications.size(), unsentNotifications.size());
        return BatchSendNotificationDTO.builder()
                .notifications(notifications)
                .totalCount(unsentNotifications.size())
                .successCount(notifications.size())
                .failedNotifications(failedNotifications)
                .build();
    }

    /**
     * Get raw notification entity (without convert to DTO)
     * @param entityId Entity id
     * @return NotificationEntity
     * @throws EntityNotFoundException when entity is not found by id
     */
    private NotificationEntity getRawEntityById(
            final @NotNull Long entityId)
            throws EntityNotFoundException {
        return this.notificationRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Entity is not found by id : %s", entityId.toString())));
    }

    /**
     * Gte raw event entity (without convert to DTO)
     * @param eventId Entity id
     * @return EventEntity
     * @throws EntityNotFoundException when entity is not found by id
     */
    private EventEntity getEventEntity(final @NotNull Long eventId)
            throws EntityNotFoundException {
        return this.eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Event entity is not found %s", eventId.toString())));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    @Override
    public void delete(final Long id)
            throws EntityNotFoundException, IllegalArgumentException {
        log.info("Start delete notification by id: {}", id);
        this.notificationValidator.validateNotificationId(id);
        final var entity = this.getRawEntityById(id);
        this.notificationRepository.delete(entity);
        log.info("End delete notification by id");
    }
}
