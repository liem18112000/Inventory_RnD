/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event.mapper;

import com.fromlabs.inventory.inventoryservice.ingredient.event.IngredientEventEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.event.dto.IngredientEventDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ingredient event mapper
 * @author Liem
 */
@UtilityClass
public class IngredientEventMapper {

    public IngredientEventDto toDto(
            @NotNull final IngredientEventEntity event
    ) {
        return IngredientEventDto.builder()
                .id(event.getId())
                .clientId(event.getClientId())
                .name(event.getName())
                .message(event.getDescription())
                .updateAt(event.getUpdateAt())
                .active(event.isActive())
                .build();
    }

    public List<IngredientEventDto> toDto(
            @NotNull final List<IngredientEventEntity> events
    ) {
        return events.stream().map(IngredientEventMapper::toDto).collect(Collectors.toList());
    }

    public Page<IngredientEventDto> toDto(
            @NotNull final Page<IngredientEventEntity> events
    ) {
        return events.map(IngredientEventMapper::toDto);
    }

    public static IngredientEventEntity toEntity(
            @NotNull final IngredientEventDto dto
    ) {
        var entity = new IngredientEventEntity();
        entity.setId(dto.getId());
        entity.setClientId(dto.getClientId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getMessage());
        entity.setUpdateAt(dto.getUpdateAt());
        entity.setActive(dto.isActive());
        return entity;
    }
}
