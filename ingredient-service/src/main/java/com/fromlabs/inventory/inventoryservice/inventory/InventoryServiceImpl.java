/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
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
     */
    public InventoryEntity getByName(
            @NotNull final Long clientId,
            @NotNull final String name
    ) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    /**
     * Get InventoryEntity by ID
     * @param id Long
     * @return InventoryEntity
     * @see InventoryEntity
     */
    public InventoryEntity getById(
            @NotNull final Long id
    ) {
        return this.repository.findById(id).orElse(null);
    }

    /**
     * Get inventory by ingredient
     * @param ingredient IngredientEntity
     * @return InventoryEntity
     */
    public InventoryEntity getByIngredient(
            @NotNull final IngredientEntity ingredient
    ) {
        return this.repository.findByIngredient(ingredient);
    }

    /**
     * Get all by client ID as list
     * @param clientId Long
     * @return List of InventoryEntity
     */
    public List<InventoryEntity> getAll(
            @NotNull final Long clientId
    ) {
        return this.repository.findAllByClientId(clientId);
    }

    /**
     * Get all inventory by client ID and unit type as list
     * @param clientId  Long
     * @param unitType  unitType
     * @return List of InventoryEntity
     * @see InventoryEntity
     */
    public List<InventoryEntity> getAll(
            @NotNull final Long clientId,
            @NotNull final String unitType
    ) {
        return this.repository.findAllByClientIdAndUnitType(clientId, unitType);
    }

    /**
     * Get all item with pagination
     * @param clientId  Long
     * @param pageable  Pageable
     * @return Page of InventoryEntity
     */
    public Page<InventoryEntity> getPage(
            @NotNull final Long clientId,
            @NotNull final Pageable pageable
    ) {
        return this.repository.findAllByClientId(clientId, pageable);
    }

    /**
     * Save an inventory
     * @param entity    InventoryEntity
     * @return InventoryEntity
     */
    public InventoryEntity save(
            @NotNull final InventoryEntity entity
    ) {
        return repository.save(entity);
    }

    /**
     * Delete an inventory
     * @param entity InventoryEntity
     */
    public void delete(
            @NotNull final InventoryEntity entity
    ) {
        repository.delete(entity);
    }

    /**
     * Get all inventory with pagination by
     * @param specification Specification
     * @param pageable Pageable
     * @return Page of InventoryEntity
     */
    public Page<InventoryEntity> getPage(
            @NotNull final Specification<InventoryEntity> specification,
            @NotNull final Pageable pageable
    ) {
        return this.repository.findAll(specification, pageable);
    }

    /**
     * Get all inventory by filter
     * @param specification Specification
     * @return List of InventoryEntity
     */
    public List<InventoryEntity> getAll(
            @NotNull final Specification<InventoryEntity> specification
    ) {
        return this.repository.findAll(specification);
    }
}
