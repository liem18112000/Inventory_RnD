/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.config.mapper;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.config.beans.dto.IngredientConfigDto;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

/**
 * Ingredient config mapper
 * @author Liem
 */
@UtilityClass
public class IngredientConfigMapper {

    //<editor-fold desc="CONVERT TO ENTITY">

    /**
     * Convert from request to entity
     * @param request       IngredientRequest
     * @param ingredient    IngredientEntity
     * @return  IngredientConfigEntity
     */
     public IngredientConfigEntity toEntity(
             @NotNull final IngredientRequest   request,
             @NotNull final IngredientEntity    ingredient
     ) {
        var config = new IngredientConfigEntity();
        config.setClientId(request.getClientId());
        config.setName(request.getName());
        config.setDescription(request.getDescription());
        config.setIngredient(ingredient);
        if(Objects.nonNull(request.getMinimumQuantity())) config.setMinimumQuantity(request.getMinimumQuantity());
        if(Objects.nonNull(request.getMaximumQuantity())) config.setMaximumQuantity(request.getMaximumQuantity());
        config.setUpdateAt(Instant.now().toString());
        return config;
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT TO DTO">

    /**
     * Convert entity to dto
     * @param entity        IngredientConfigEntity
     * @param ingredientDto IngredientDto
     * @return              IngredientConfigDto
     */
    public IngredientConfigDto toDto(
            @NotNull final IngredientConfigEntity entity,
            @NotNull final IngredientDto ingredientDto
    ) {
         return IngredientConfigDto.configBuilder()
                 .id(entity.getId())
                 .clientId(entity.getClientId())
                 .ingredient(ingredientDto)
                 .name(entity.getName())
                 .description(entity.getDescription())
                 .minimumQuantity(entity.getMinimumQuantity())
                 .maximumQuantity(entity.getMaximumQuantity())
                 .updateAt(entity.getUpdateAt())
                 .isActive(entity.isActive())
                 .build();
    }

    //</editor-fold>
}
