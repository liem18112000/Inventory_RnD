/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track;

import com.fromlabs.inventory.inventoryservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.inventoryservice.common.service.RestApiService;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;

import java.util.List;

/**
 * Ingredient track service
 * @author Liem
 */
public interface IngredientHistoryService
        extends RestApiService<IngredientHistoryEntity, Long> {

    /**
     * Get history by id with exception
     * @param id    Unique ID
     * @return      IngredientHistoryEntity
     * @throws      ObjectNotFoundException
     *              throw if history is not found with id
     */
    IngredientHistoryEntity getByIdWithException(
            Long id
    ) throws ObjectNotFoundException;

    /**
     * Get list of history by ingredient
     * @param tenantId      Tenant ID
     * @param ingredient    IngredientEntity
     * @return              list of ingredient history
     */
    List<IngredientHistoryEntity> getByIngredient(
            Long tenantId,
            IngredientEntity ingredient
    );

    /**
     * Get list of all history
     * @param tenantId      Tenant ID
     * @return              list of ingredient history
     */
    List<IngredientHistoryEntity> getAll(
            Long tenantId
    );
}
