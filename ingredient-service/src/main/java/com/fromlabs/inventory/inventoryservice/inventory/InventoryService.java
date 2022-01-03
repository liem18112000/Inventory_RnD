/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory;

import com.fromlabs.inventory.inventoryservice.common.service.RestApiService;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface of InventoryEntity service
 */
public interface InventoryService extends RestApiService<InventoryEntity, Long> {
    /**
     * Get InventoryEntity by client ID and name
     * @param clientId  Long
     * @param name      String
     * @return InventoryEntity
     */
    InventoryEntity getByName(Long clientId, String name);

    /**
     * Get inventory by ingredient
     * @param ingredient IngredientEntity
     * @return InventoryEntity
     */
    InventoryEntity getByIngredient(IngredientEntity ingredient);

    /**
     * Get all inventory by client ID
     * @param clientId Long
     * @return List of InventoryEntity
     */
    List<InventoryEntity> getAll(Long clientId);

    /**
     * Get all inventory by unitType
     * @param clientId  Long
     * @param unitType  unitType
     * @return List of InventoryEntity
     */
    List<InventoryEntity> getAll(Long clientId, String unitType);

    /**
     * Get all inventory with pagination
     * @param clientId  Long
     * @param pageable  Pageable
     * @return Page of InventoryEntity
     */
    Page<InventoryEntity> getPage(Long clientId, Pageable pageable);
}
