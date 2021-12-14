/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of Inventory service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InventoryServiceImpl implements InventoryService {

    final private InventoryRepository repository;

    /**
     * Constructor
     * @param repository InventoryRepository
     */
    public InventoryServiceImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Get inventory by clientId and name
     * @param clientId  Long
     * @param name      String
     * @return InventoryEntity
     * @see InventoryEntity
     */
    public InventoryEntity get(Long clientId, String name) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    /**
     * Get InventoryEntity by ID
     * @param id Long
     * @return InventoryEntity
     * @see InventoryEntity
     */
    public InventoryEntity get(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    /**
     * Get inventory by ingredient
     *
     * @param ingredient IngredientEntity
     * @return InventoryEntity
     * @see IngredientEntity
     * @see InventoryEntity
     */
    public InventoryEntity get(IngredientEntity ingredient) {
        return this.repository.findByIngredient(ingredient);
    }

    /**
     * Get all by client ID as list
     * @param clientId Long
     * @return List&lt;InventoryEntity&gt;
     * @see InventoryEntity
     */
    public List<InventoryEntity> getAll(Long clientId) {
        return this.repository.findAllByClientId(clientId);
    }

    /**
     * Get all inventory by client ID and unit type as list
     * @param clientId  Long
     * @param unitType  unitType
     * @return List&lt;InventoryEntity&gt;
     * @see InventoryEntity
     */
    public List<InventoryEntity> getAll(Long clientId, String unitType) {
        return this.repository.findAllByClientIdAndUnitType(clientId, unitType);
    }

    /**
     * Get all item with pagination
     * @param clientId  Long
     * @param pageable  Pageable
     * @return Page&lt;InventoryEntity&gt;
     * @see Pageable
     * @see InventoryEntity
     */
    public Page<InventoryEntity> getPage(Long clientId, Pageable pageable) {
        return this.repository.findAllByClientId(clientId, pageable);
    }

    /**
     * Save an inventory
     * @param entity    InventoryEntity
     * @return InventoryEntity
     */
    public InventoryEntity save(InventoryEntity entity) {
        return repository.save(entity);
    }

    /**
     * Delete an inventory
     * @param entity InventoryEntity
     * @see InventoryEntity
     */
    public void delete(InventoryEntity entity) {
        repository.delete(entity);
    }

    /**
     * Get all inventory with pagination by
     * @param specification Specification
     * @param pageable Pageable
     * @return Page$lt;InventoryEntity&gt;
     * @see InventoryEntity
     * @see Specification
     * @see Pageable
     */
    public Page<InventoryEntity> getPage(Specification<InventoryEntity> specification, Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }
}
