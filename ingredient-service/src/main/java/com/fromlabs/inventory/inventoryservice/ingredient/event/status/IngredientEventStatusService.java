/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event.status;

import com.fromlabs.inventory.inventoryservice.ingredient.event.status.dto.IngredientEventStatusDto;

import java.util.List;

/**
 * Ingredient event status service interface
 * @author Liem
 */
public interface IngredientEventStatusService {

    /**
     * Get event status by name
     * @param name  status name
     * @return      IngredientEventStatus
     */
    IngredientEventStatusDto getByName(
            final String name
    );

    /**
     * Gte all statuses
     * @return      list of event statuses
     */
    List<IngredientEventStatusDto> getAll();
}
