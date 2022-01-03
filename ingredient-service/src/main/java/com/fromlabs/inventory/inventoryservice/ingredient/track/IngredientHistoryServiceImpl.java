/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track;

import com.fromlabs.inventory.inventoryservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Ingredient history service implementation
 * @author Liem
 */
@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
public class IngredientHistoryServiceImpl implements IngredientHistoryService {

    //<editor-fold desc="SETUP">

    private final IngredientHistoryRepository repository;

    /**
     * Constructor
     * @param repository    IngredientHistoryRepository
     */
    public IngredientHistoryServiceImpl(
            IngredientHistoryRepository repository
    ) {
        this.repository = repository;
    }

    //</editor-fold>

    /**
     * Get history by id
     * @param id    Unique ID
     * @return      IngredientHistoryEntity
     */
    public IngredientHistoryEntity getById(
            @NotNull final Long id
    ){
        return this.repository.findById(id).orElse(null);
    }

    /**
     * Get page of ingredient history with filter
     *
     * @param specification filter criteria
     * @param pageable      Pageable
     * @return page of ingredient history
     */
    @Override
    public Page<IngredientHistoryEntity> getPage(
            @NotNull final Specification<IngredientHistoryEntity>  specification,
            @NotNull final Pageable                                pageable
    ) {
        return this.repository.findAll(specification, pageable);
    }

    /**
     * Get list of ingredient history with filter
     *
     * @param specification filter criteria
     * @return list of ingredient history
     */
    @Override
    public List<IngredientHistoryEntity> getAll(
            @NotNull final Specification<IngredientHistoryEntity>  specification
    ) {
        return this.repository.findAll(specification);
    }

    /**
     * Get history by id with exception
     * @param id Unique ID
     * @return IngredientHistoryEntity
     * @throws ObjectNotFoundException throw if history is not found with id
     */
    public IngredientHistoryEntity getByIdWithException(
            @NotNull final Long id
    ) throws ObjectNotFoundException {
        return this.repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("History is not found with id : "
                        .concat(String.valueOf(id))));
    }

    /**
     * Get list of history by ingredient
     * @param tenantId   Tenant ID
     * @param ingredient IngredientEntity
     * @return list of ingredient history
     */
    @Override
    public List<IngredientHistoryEntity> getByIngredient(
            @NotNull final Long tenantId,
            @NotNull final IngredientEntity ingredient
    ) {
        return this.repository.findAllByClientIdAndIngredient(tenantId, ingredient);
    }

    /**
     * Get list of all history
     * @param tenantId Tenant ID
     * @return list of ingredient history
     */
    public List<IngredientHistoryEntity> getAll(
            @NotNull final Long tenantId
    ) {
        return this.repository.findAllByClientId(tenantId);
    }

    /**
     * Save ingredient history
     *
     * @param entity IngredientHistoryEntity
     * @return IngredientHistoryEntity
     */
    @Override
    public IngredientHistoryEntity save(
            @NotNull IngredientHistoryEntity entity
    ) {
        return this.repository.save(entity);
    }

    /**
     * Delete ingredient history
     *
     * @param entity IngredientHistoryEntity
     */
    @Override
    public void delete(
            @NotNull IngredientHistoryEntity entity
    ) {
        this.repository.delete(entity);
    }
}
