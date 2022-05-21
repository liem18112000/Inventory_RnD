/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fromlabs.inventory.inventoryservice.ingredient.*;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.event.*;
import com.fromlabs.inventory.inventoryservice.ingredient.event.beans.dto.IngredientEventDto;
import com.fromlabs.inventory.inventoryservice.ingredient.event.mapper.IngredientEventMapper;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatus;
import com.fromlabs.inventory.inventoryservice.ingredient.mapper.IngredientMapper;
import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.dto.IngredientHistoryDto;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.enums.ExtraInformationClass;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request.*;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemRequest;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import javax.validation.constraints.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType.*;
import static com.fromlabs.inventory.inventoryservice.ingredient.track.factory.IngredientHistoryEntityFactory.create;
import static java.util.Objects.*;

/**
 * Ingredient history mapper
 * @author Liem
 */
@UtilityClass
public class IngredientHistoryMapper {

    //<editor-fold desc="CONVERT TO DTO">

    public IngredientHistoryDto toDto(
            @NotNull final IngredientHistoryEntity entity,
            @NotNull final IngredientEventService service
    ) {
        final var ingredient = entity.getIngredient();
        return IngredientHistoryDto.builder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .ingredient(getIngredientFromEntity(ingredient))
                .name(entity.getName().trim())
                .description(entity.getDescription().trim())
                .actorRole(entity.getActorRole().trim())
                .actorName(entity.getActorName().trim())
                .trackTimestamp(entity.getTrackTimestamp().trim())
                .updateAt(entity.getUpdateAt())
                .isActive(entity.isActive())
                .event(getEventDto(entity.getEvent(), entity.getEventStatus(), service))
                .extraInformation(getExtraInformationFromEntity(entity))
                .build();
    }

    public List<IngredientHistoryDto> toDto(
            @NotNull @NotEmpty final List<IngredientHistoryEntity>  entities,
            @NotNull final IngredientEventService                   service
    ) {
        return entities.stream().map(entity -> toDto(entity, service)).collect(Collectors.toList());
    }

    public Page<IngredientHistoryDto> toDto(
            @NotNull @NotEmpty Page<IngredientHistoryEntity>    entities,
            @NotNull final IngredientEventService               service
    ) {
        return entities.map(entity -> toDto(entity, service));
    }

    private IngredientEventDto getEventDto(
            final IngredientEventEntity event,
            @NotNull @NotBlank final String         status,
            @NotNull final IngredientEventService   service
    ) {
        var eventDto = IngredientEventMapper.toDto(event);
        eventDto.setStatus(service.getStatusByName(status));
        return eventDto;
    }

    private IngredientDto getIngredientFromEntity(
            @NotNull final IngredientEntity ingredient
    ) {
        return Objects.nonNull(ingredient) ? IngredientMapper.toDto(ingredient) : null;
    }

    private Object getExtraInformationFromEntity(
            @NotNull final IngredientHistoryEntity entity
    ){
        try {
            final var clazz = Arrays.stream(ExtraInformationClass.values())
                    .filter(i -> i.getClassName().equals(entity.getExtraInformationClass()))
                    .findFirst();
            return  clazz.isPresent() ?
                    new ObjectMapper().readValue(entity.getExtraInformation(), clazz.get().getClazz()) :
                    null;
        } catch (Exception e) {
            return null;
        }
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT TO ENTITY">

    /**
     * Convert request to entity
     * @param request       IngredientHistoryRequest
     * @param service       IngredientService
     * @param event         IngredientEvent
     * @param eventStatus   IngredientEventStatus
     * @return              IngredientHistoryEntity
     */
    @SneakyThrows
    static public IngredientHistoryEntity toEntity(
            @NotNull final ItemRequest request,
            @NotNull final IngredientService service,
            @NotNull final IngredientEventEntity event,
            @NotNull final IngredientEventStatus eventStatus
    ) {
        var entity = create(DEFAULT);
        entity.setClientId(request.getClientId());
        entity.setIngredient(getIngredient(request, service));
        entity.setName(getNameFromRequest(request));
        entity.setActorName(request.getActorName());
        entity.setEvent(event);
        entity.setEventStatus(eventStatus.getName());
        entity.setExtraInformationClass(request.getClass().getSimpleName());
        entity.setActorRole(request.getActorRole());
        entity.setExtraInformation(new ObjectMapper().writeValueAsString(request));
        entity.setDescription(extractDescription(event.getDescription(), eventStatus));
        entity.setActive(true);
        return entity;
    }

    /**
     * Convert ingredient request to history ingredient entity
     * @param request       IngredientRequest
     * @param service       IngredientService
     * @param event         IngredientEvent
     * @param eventStatus   IngredientEventStatus
     * @return              IngredientHistoryEntity
     */
    @SneakyThrows
    static public IngredientHistoryEntity toEntity(
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService service,
            @NotNull final IngredientEventEntity event,
            @NotNull final IngredientEventStatus eventStatus
    ) {
        var entity = create(DEFAULT);
        entity.setClientId(request.getClientId());
        entity.setIngredient(service.getById(request.getId()));
        entity.setName(request.getName());
        entity.setActorName(request.getActorName());
        entity.setEvent(event);
        entity.setEventStatus(eventStatus.getName());
        entity.setExtraInformationClass(request.getClass().getSimpleName());
        entity.setActorRole(request.getActorRole());
        entity.setExtraInformation(new ObjectMapper().writeValueAsString(request));
        entity.setDescription(extractDescription(event.getDescription(), eventStatus));
        entity.setActive(true);
        return entity;
    }

    /**
     * Convert page request to entity
     * @param request   IngredientHistoryPageRequest
     * @param event     IngredientEventDto
     * @param service   IngredientService
     * @return          IngredientHistoryEntity
     */
    public IngredientHistoryEntity toEntity(
            @NotNull final IngredientHistoryPageRequest request,
            @NotNull final IngredientEventDto event,
            @NotNull final IngredientService service
    ) {
        var entity = create(EMPTY);
        entity.setClientId(request.getClientId());
        entity.setIngredient(getIngredient(request, service));
        entity.setName(request.getName());
        entity.setEvent(IngredientEventMapper.toEntity(event));
        entity.setTrackTimestamp(request.getTrackTimestamp());
        entity.setExtraInformation(request.getExtraInformation());
        entity.setActorName(request.getActorName());
        entity.setActorRole(request.getActorRole());
        entity.setDescription(request.getDescription());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    /**
     * Extract description from event and event status
     * @param eventMessage  String of Event message
     * @param eventStatus   IngredientEventStatus
     * @return              String
     */
    private String extractDescription(
            @NotNull @NotBlank final String eventMessage,
            @NotNull final IngredientEventStatus eventStatus
    ) {
        return eventMessage.trim().concat("-").concat(eventStatus.getMessage()).trim();
    }

    /**
     * Get request name
     * @param request   IngredientHistoryRequest
     * @return          Return the default name as actorName + actorRole if name is null
     */
    private String getNameFromRequest(
            @NotNull final ItemRequest request
    ) {
        return  StringUtils.hasLength(request.getName()) ?
                request.getName() : request.getActorName().concat("_").concat(request.getActorRole());
    }

    /**
     * Get ingredient by id from request
     * @param request   IngredientHistoryRequest
     * @param service   IngredientService
     * @return          IngredientEntity
     */
    private IngredientEntity getIngredient(
            @NotNull final ItemRequest request,
            @NotNull final IngredientService service
    ) {
        return  nonNull(request.getIngredientId()) ?
                service.getById(requireNonNull(request.getIngredientId())) : null;
    }

    /**
     * Get ingredient by id from request
     * @param request   IngredientHistoryRequest
     * @param service   IngredientService
     * @return          IngredientEntity
     */
    private IngredientEntity getIngredient(
            @NotNull final IngredientHistoryPageRequest request,
            @NotNull final IngredientService            service
    ) {
        return  nonNull(request.getIngredientId()) ?
                service.getById(requireNonNull(request.getIngredientId())) : null;
    }

    //</editor-fold>
}
