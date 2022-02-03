package com.fromlabs.inventory.notificationservice.notification.service.impl;

import com.fromlabs.inventory.notificationservice.common.dto.SimpleDto;
import com.fromlabs.inventory.notificationservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.mapper.EventMapper;
import com.fromlabs.inventory.notificationservice.notification.beans.validator.EventValidator;
import com.fromlabs.inventory.notificationservice.notification.event.EventEntity;
import com.fromlabs.inventory.notificationservice.notification.event.EventRepository;
import com.fromlabs.inventory.notificationservice.notification.event.EventType;
import com.fromlabs.inventory.notificationservice.notification.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.notificationservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.notificationservice.common.specifications.SearchCriteria.criteriaEqual;

/**
 * {@inheritDoc}
 */
@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final EventValidator eventValidator;

    /**
     * {@inheritDoc}
     */
    public Set<SimpleDto> getTypes() {
        log.info("Start get event type");
        final var types = Arrays.stream(EventType.values())
                .map(type -> new SimpleDto(type.getType(), type.getType().toUpperCase()))
                .collect(Collectors.toSet());
        log.info("End get event type");
        return types;
    }

    /**
     * {@inheritDoc}
     */
    public Page<EventDTO> getPageWithFilter(
            final EventDTO eventDTO, final Pageable pageable)
            throws IllegalArgumentException {
        log.info("Start get page event: {} - {}", eventDTO, pageable);
        final var requestPageable = this.getPageableDefault(pageable);
        Page<EventEntity> pages;
        if (Objects.isNull(eventDTO)) {
            pages = this.eventRepository.findAll(requestPageable);
        } else {
            final var specification = getSpecification(eventDTO);
            pages = this.eventRepository.findAll(specification, requestPageable);
        }
        log.info("End get page event");
        return pages.map(this.eventMapper::toDto);
    }

    private Pageable getPageableDefault(final Pageable pageable) {
        return Objects.isNull(pageable) ? PageRequest.of(0, 10) : pageable;
    }

    private String getStringDefault(final String string) {
        return StringUtils.hasText(string) ? string : "";
    }

    private Specification<EventEntity> getSpecification(final EventDTO eventDTO) {
        return BaseSpecification.<EventEntity>Spec(criteriaEqual("name", getStringDefault(eventDTO.getName())))
                .and(Spec(criteriaEqual("eventType", getStringDefault(eventDTO.getEventType()))))
                .and(Spec(criteriaEqual("description", getStringDefault(eventDTO.getDescription()))))
                .and(Spec(criteriaEqual("occurAt", getStringDefault(eventDTO.getOccurAt()))));
    }

    /**
     * {@inheritDoc}
     */
    public EventDTO getById(final Long id) throws IllegalArgumentException{
        log.info("Start get event by id: {}", id);
        final var entity = this.eventRepository.findById(id).orElse(null);
        final var dto = this.eventMapper.toDto(entity);
        log.info("End get event by id: {}", dto);
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    public EventDTO getByIdWithException(final Long id)
            throws EntityNotFoundException, IllegalArgumentException {
        log.info("Start get event by id: {}", id);
        this.eventValidator.validateEventId(id);
        final var entity = getRawEntityById(id);
        final var dto = this.eventMapper.toDto(entity);
        log.info("End get event by id: {}", dto);
        return dto;
    }

    private EventEntity getRawEntityById(
            final @NotNull Long id) throws EntityNotFoundException {
        return this.eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Entity is not found by id : %s", id.toString())));
    }

    /**
     * {@inheritDoc}
     */
    public EventDTO save(final EventDTO dto)
            throws IllegalArgumentException, EntityNotFoundException {
        log.info("Start save event: {}", dto);
        this.eventValidator.validateSaveEvent(dto);
        final var entity = this.createOrModifyBasicInformation(dto);
        final var savedEntity = this.eventRepository.save(entity);
        final var savedDto = this.eventMapper.toDto(savedEntity);
        log.info("End save event: {}", savedDto);
        return savedDto;
    }

    private EventEntity createOrModifyBasicInformation(
            @NotNull final EventDTO dto) throws EntityNotFoundException {
        EventEntity entity;
        final var entityId = dto.getId();
        if (Objects.isNull(entityId)) {
            entity = this.eventMapper.toEntity(dto);
            entity.updateAfterCreated();
            entity.activate();
            log.info("A new event is created");
        } else {
            entity = getRawEntityById(entityId);
            entity.updateBasicInformation(dto);
            log.info("An exist event is updated");
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final Long id)
            throws EntityNotFoundException, IllegalArgumentException {
        log.info("Start delete event by id: {}", id);
        this.eventValidator.validateEventId(id);
        final var entity = getRawEntityById(id);
        this.eventRepository.delete(entity);
        log.info("End delete event by id");
    }
}
