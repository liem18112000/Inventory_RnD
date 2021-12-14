/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Interface of InventoryEntity service
 */
public interface InventoryService {
    /**
     * Get InventoryEntity by client ID and name
     * @param clientId  Long
     * @param name      String
     * @return InventoryEntity
     * @see InventoryEntity
     */
    InventoryEntity get(Long clientId, String name);

    /**
     * Get InventoryEntity by ID
     * @param id Inventory ID
     * @return InventoryEntity
     * @see InventoryEntity
     */
    InventoryEntity get(Long id);

    /**
     * Get inventory by ingredient
     * @param ingredient IngredientEntity
     * @return InventoryEntity
     * @see IngredientEntity
     * @see InventoryEntity
     */
    InventoryEntity get(IngredientEntity ingredient);

    /**
     * Get all inventory by client ID
     * @param clientId Long
     * @return List&lt;InventoryEntity&gt;
     * @see InventoryEntity
     */
    List<InventoryEntity> getAll(Long clientId);

    /**
     * Get all inventory by unitType
     * @param clientId  Long
     * @param unitType  unitType
     * @return List&lt;InventoryEntity&gt;
     * @see InventoryEntity
     * @see String
     */
    List<InventoryEntity> getAll(Long clientId, String unitType);

    /**
     * Get all inventory with pagination
     * @param clientId  Long
     * @param pageable  Pageable
     * @return Page&lt;InventoryEntity&gt;
     */
    Page<InventoryEntity> getPage(Long clientId, Pageable pageable);

    /**
     * Save an InventoryEntity
     * @param entity    InventoryEntity
     * @return InventoryEntity
     * @see InventoryEntity
     */
    InventoryEntity save(InventoryEntity entity);

    /**
     * Delete an InventoryEntity
     * @param entity InventoryEntity
     * @see InventoryEntity
     */
    void delete(InventoryEntity entity);

    /**
     * Get all inventory with pagination by
     * @param specification Specification
     * @param pageable Pageable
     * @return Page$lt;InventoryEntity&gt;
     * @see InventoryEntity
     * @see Specification
     * @see Pageable
     */
    Page<InventoryEntity> getPage(Specification<InventoryEntity> specification, Pageable pageable);
}
