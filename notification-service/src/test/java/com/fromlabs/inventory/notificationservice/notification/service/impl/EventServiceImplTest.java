package com.fromlabs.inventory.notificationservice.notification.service.impl;

import com.fromlabs.inventory.notificationservice.NotificationserviceApplication;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.EventDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.dto.NotificationDTO;
import com.fromlabs.inventory.notificationservice.notification.beans.mapper.EventMapper;
import com.fromlabs.inventory.notificationservice.notification.beans.validator.EventValidator;
import com.fromlabs.inventory.notificationservice.notification.event.EventEntity;
import com.fromlabs.inventory.notificationservice.notification.event.EventRepository;
import com.fromlabs.inventory.notificationservice.notification.event.EventType;
import com.fromlabs.inventory.notificationservice.notification.notfication.NotificationStatus;
import com.fromlabs.inventory.notificationservice.notification.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles({"dev","liem-local"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = NotificationserviceApplication.class)
class EventServiceImplTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventValidator eventValidator;

    @Test
    void given_getTypes_when_allThingIsRight() {
        final var types = this.eventService.getTypes();
        final var actualLabel = types.stream()
                .map(simpleDto1 -> simpleDto1.getLabel().toString())
                .collect(Collectors.toList());
        final var actualValue = types.stream()
                .map(simpleDto -> simpleDto.getValue().toString())
                .collect(Collectors.toList());
        final var expectedLabel = Arrays.stream(EventType.values())
                .map(s -> s.getType().toUpperCase()).collect(Collectors.toList());
        final var expectedValue = Arrays.stream(EventType.values())
                .map(EventType::getType).collect(Collectors.toList());
        assertTrue(actualLabel.containsAll(expectedLabel));
        assertTrue(expectedLabel.containsAll(actualLabel));
        assertTrue(actualValue.containsAll(expectedValue));
        assertTrue(expectedValue.containsAll(actualValue));
    }

    @Test
    void given_getPageWithFilter_when_pageableIsNull_then_getPageDefault() {
        final var dto = EventDTO.builder()
                .name("").description("").eventType("").build();
        final var pages = this.eventService
                .getPageWithFilter(dto, null);
        assertNotNull(pages);
        assertEquals(10, pages.getSize());
        assertEquals(0, pages.getNumber());
    }

    @Test
    void given_getPageWithFilter_when_dtoIsNull_then_getPageDefault() {
        final var pageable = PageRequest.of(1, 5, Sort.Direction.DESC, "name");
        final var pages = this.eventService
                .getPageWithFilter(null, pageable);
        assertNotNull(pages);
        assertEquals(5, pages.getSize());
        assertEquals(1, pages.getNumber());
        assertEquals(pages.getPageable().getSort(), Sort.by(Sort.Direction.DESC, "name"));
    }

    @Test
    void given_getPageWithFilter_when_filterByName_then_getPageWithFilteredName() {
        final var pageable = PageRequest.of(0, 10);
        final var entity = this.eventRepository.findAll().get(0);
        final var dto = EventDTO.builder().name(entity.getName()).build();
        final var pages = this.eventService
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
        final var entity = this.eventRepository.findAll().get(0);
        final var dto = EventDTO.builder()
                .description(entity.getDescription()).build();
        final var pages = this.eventService
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
    void given_getPageWithFilter_when_filterByStatus_then_getPageWithFilteredEventType() {
        final var pageable = PageRequest.of(0, 10);
        final var entity = this.eventRepository.findAll().get(0);
        final var dto = EventDTO.builder().eventType(entity.getEventType()).build();
        final var pages = this.eventService
                .getPageWithFilter(dto, pageable);
        assertNotNull(pages);
        assertTrue(pages.getTotalElements() > 0);
        assertEquals(10, pages.getSize());
        assertEquals(0, pages.getNumber());
        assertTrue(pages.stream().allMatch(page ->
                Objects.equals(page.getEventType(), entity.getEventType()) ||
                        page.getEventType().contains(entity.getEventType())));
    }

    @Test
    void given_getById_when_idIsNull_then_returnNull() {
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> this.eventService.getById(null),
                "The given id must not be null!");
    }

    @Test
    void given_getById_when_idIsValid_then_returnDto() {
        final var entity = this.eventRepository.findAll().get(0);
        final var actualDto = assertDoesNotThrow(() ->
                this.eventService.getById(entity.getId()));
        assertEquals(actualDto.getId(), entity.getId());
        assertEquals(actualDto.getName(), entity.getName());
    }

    @Test
    void given_getByIdWithException_when_idIsNull_then_throwException() {
        assertThrows(IllegalArgumentException.class,
                () -> this.eventService.getByIdWithException(null),
                "Event id is null");
    }

    @Test
    void given_getByIdWithException_when_idIsValid_then_getDTO() {
        final var entity = this.eventRepository.findAll().get(0);
        var expectedDto = assertDoesNotThrow(
                () -> this.eventMapper.toDto(entity));
        var actualDto = assertDoesNotThrow(() ->  this.eventService
                .getByIdWithException(entity.getId()));
        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getName(), actualDto.getName());
    }

    @Test
    void given_save_when_nameIsNull_then_throwException() {
        final var entity = this.eventRepository.findAll().get(0);
        var dto = this.eventMapper.toDto(entity);
        dto.setName(null);
        assertThrows(IllegalArgumentException.class,
                () -> this.eventService.save(dto),
                "Event name is null or blank");
    }

    @Test
    void given_save_when_nameIsBlank_then_throwException() {
        final var entity = this.eventRepository.findAll().get(0);
        var dto = this.eventMapper.toDto(entity);
        dto.setName("");
        assertThrows(IllegalArgumentException.class,
                () -> this.eventService.save(dto),
                "Event name is null or blank");
    }

    @Test
    void given_save_when_eventTypeIsNull_then_throwException() {
        final var entity = this.eventRepository.findAll().get(0);
        var dto = this.eventMapper.toDto(entity);
        dto.setEventType(null);
        assertThrows(IllegalArgumentException.class,
                () -> this.eventService.save(dto),
                "Event type is not exist");
    }

    @Test
    void given_save_when_saveEntityCorrectly_then_returnWithNoException() {
        final var dto = EventDTO.builder()
                .name("Name").description("Description")
                .eventType(EventType.INGREDIENT_ITEM_LOW_STOCK.getType())
                .build();
        final var mockRepo = mock(EventRepository.class);
        final var mockService = new EventServiceImpl(mockRepo, eventMapper, eventValidator);
        assertDoesNotThrow(() -> mockService.save(dto));
    }

    @Test
    void given_save_when_updateEntityCorrectly_then_returnWithNoException() {
        var entity = new EventEntity();
        entity.setId(1L);
        entity.setName("Old name");
        entity.setDescription("Old description");
        final var dto = EventDTO.builder()
                .id(1L).name("New name").description("New description")
                .eventType(EventType.INGREDIENT_ITEM_LOW_STOCK.getType()).build();
        final var mockRepo = mock(EventRepository.class);
        when(mockRepo.findById(entity.getId())).thenReturn(Optional.of(entity));
        final var mockService = new EventServiceImpl(mockRepo, eventMapper, eventValidator);
        assertDoesNotThrow(() -> mockService.save(dto));
    }

    @Test
    void given_save_when_eventTypeIsBlank_then_throwException() {
        final var entity = this.eventRepository.findAll().get(0);
        var dto = this.eventMapper.toDto(entity);
        dto.setEventType("");
        assertThrows(IllegalArgumentException.class,
                () -> this.eventService.save(dto),
                "Event type is not exist");
    }

    @Test
    void given_save_when_eventTypeIsNotExist_then_throwException() {
        final var entity = this.eventRepository.findAll().get(0);
        var dto = this.eventMapper.toDto(entity);
        dto.setEventType("NotExist");
        assertThrows(IllegalArgumentException.class,
                () -> this.eventService.save(dto),
                "Event type is not exist");
    }

    @Test
    void given_delete_when_idIsNull_then_throwException() {
        assertThrows(IllegalArgumentException.class,
                () -> this.eventService.delete(null),
                "Event id is null");
    }

    @Test
    void given_delete_when_entityIsNotFound_then_throwException() {
        final var id = System.currentTimeMillis();
        assertThrows(EntityNotFoundException.class,
                () -> this.eventService.delete(id),
                String.format("Entity is not found by id : %s", String.valueOf(id)));
    }

    @Test
    void given_delete_when_idIsNotPositive_then_throwException() {
        assertThrows(IllegalArgumentException.class,
                () -> this.eventService.delete(0L),
                "Event id is not positive");
    }

    @Test
    void given_delete_when_allThingIsRight_then_deleteEntity() {
        var entity = new EventEntity();
        entity.setId(1L);
        final var mockRepo = mock(EventRepository.class);
        when(mockRepo.findById(entity.getId())).thenReturn(Optional.of(entity));
        final var mockService = new EventServiceImpl(mockRepo, eventMapper, eventValidator);
        assertDoesNotThrow(() -> mockService.delete(entity.getId()));
    }
}