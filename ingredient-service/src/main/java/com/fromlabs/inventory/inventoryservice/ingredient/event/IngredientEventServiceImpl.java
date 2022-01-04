/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event;

import com.fromlabs.inventory.inventoryservice.ingredient.event.beans.dto.IngredientEventDto;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatusService;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.dto.IngredientEventStatusDto;
import com.fromlabs.inventory.inventoryservice.ingredient.event.mapper.IngredientEventMapper;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * Ingredient event service
 * @author Liem
 */
@Service
public class IngredientEventServiceImpl implements IngredientEventService {

    protected final IngredientEventStatusService statusService;

    protected final IngredientEventRepository eventRepository;

    public IngredientEventServiceImpl(
            final IngredientEventStatusService statusService,
            final IngredientEventRepository eventRepository
    ) {
        this.statusService = statusService;
        this.eventRepository = eventRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IngredientEventDto getByName(
            @NotNull final Long clientId,
            @NotNull final String name
    ) {
        final var event = eventRepository.findByClientIdAndName(clientId, name);
        return Objects.isNull(event) ? null : IngredientEventMapper.toDto(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IngredientEventStatusDto getStatusByName(
            @NotNull @NotBlank final String name
    ) {
        return statusService.getByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IngredientEventDto> getAll(
            @NotNull final Long clientId
    ) {
        return IngredientEventMapper.toDto(eventRepository.findAllByClientId(clientId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IngredientEventStatusDto> getAllStatus() {
        return statusService.getAll();
    }
}
