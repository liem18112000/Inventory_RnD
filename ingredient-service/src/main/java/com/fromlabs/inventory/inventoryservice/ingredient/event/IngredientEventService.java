/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event;

import com.fromlabs.inventory.inventoryservice.ingredient.event.dto.IngredientEventDto;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.dto.IngredientEventStatusDto;

import java.util.List;

/**
 * Ingredient event service interface
 * @author Liem
 */
public interface IngredientEventService {

    /**
     * Get event by name and client id
     * @param clientId Client ID
     * @param name event name
     * @return  Event
     */
    IngredientEventDto getByName(Long clientId, String name);

    /**
     * Get status by name
     * @param name status name
     * @return  Event
     */
    IngredientEventStatusDto getStatusByName(String name);

    /**
     * Get all event by client id
     * @param clientId Client id
     * @return list of event
     */
    List<IngredientEventDto> getAll(Long clientId);

    /**
     * Get all event status as list
     * @return  list of event
     */
    List<IngredientEventStatusDto> getAllStatus();
}
