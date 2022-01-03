/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.mapper;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.item.ItemEntity;
import com.fromlabs.inventory.inventoryservice.item.beans.request.BatchItemsRequest;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Batch item mapper for convert batch item request to entities
 * @author Liem
 */
@UtilityClass
public class BatchItemsMapper {

    /**
     * Convert batch item request to list of entity
     * @param request   BatchItemsRequest
     * @return          list of item entity
     */
    public List<ItemEntity> toEntity(
            @NotNull final BatchItemsRequest request,
            @NotNull final IngredientService ingredientService
    ) {
        // Init variables
        var items = new ArrayList<ItemEntity>();
        final var ingredient = requireNonNull(ingredientService.getById(request.getIngredientId()));

        // Handle batch with provided list of code
        if(!request.getCodes().isEmpty()) {
            request.getCodes().forEach(code -> items
                    .add(extractInfoToEntity(ingredient, request.getImportId(), ItemMapper.toEntity(request), code)));
        }

        // Handle based on provide number of items in the batch
        else if(request.getQuantity() > 0) {
            for(int i = 0; i < request.getQuantity(); i++) {
                final var code = request.getCode().concat("_").concat(UUID.randomUUID().toString());
                items.add(extractInfoToEntity(ingredient, request.getImportId(), ItemMapper.toEntity(request), code));
            }
        }

        return items;
    }

    private ItemEntity extractInfoToEntity(
            @NotNull final IngredientEntity ingredient,
            @NotNull final Long importId,
            @NotNull ItemEntity entity,
            @NotNull @NotBlank @NotEmpty String code
    ) {
        entity.setCode(code);
        entity.setImportId(importId);
        entity.setIngredient(ingredient);
        return entity;
    }
}
