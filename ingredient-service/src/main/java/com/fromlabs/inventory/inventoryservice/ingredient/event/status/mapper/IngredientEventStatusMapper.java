/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event.status.mapper;

import com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatus;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.dto.IngredientEventStatusDto;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IngredientEventStatusMapper {

    public IngredientEventStatusDto toDto(
            @NonNull final IngredientEventStatus status
    ) {
        return IngredientEventStatusDto.builder()
                .name(status.getName())
                .message(status.getMessage())
                .build();
    }
}
