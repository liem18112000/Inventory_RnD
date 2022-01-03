/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.config;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Service
public class IngredientConfigServiceImpl implements IngredientConfigService {

    final private IngredientConfigRepository repository;

    public IngredientConfigServiceImpl(
            @NotNull final IngredientConfigRepository repository
    ) {
        this.repository = repository;
    }

    /**
     * Get config by id
     * @param id Unique Entity ID
     * @return IngredientConfigEntity
     */
    @Override
    public IngredientConfigEntity getById(
            @NotNull final Long id
    ) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Get config by ingredient
     * @param clientId  Client ID
     * @param entity    IngredientEntity
     * @return          IngredientConfigEntity
     */
    @Override
    public IngredientConfigEntity getByIngredient(
            @NotNull final Long clientId,
            @NotNull final IngredientEntity entity
    ) {
        return repository.findByClientIdAndIngredient(clientId, entity);
    }

    /**
     * Get all by client id
     * @param clientId Client ID
     * @return List of config
     */
    @Override
    public List<IngredientConfigEntity> getAll(
            @NotNull final Long clientId
    ) {
        return repository.findAllByClientId(clientId);
    }

    /**
     * Get all by filter
     * @param specification Filter
     * @return List of config
     */
    @Override
    public List<IngredientConfigEntity> getAll(
            @NotNull final Specification<IngredientConfigEntity> specification
    ) {
        return repository.findAll(specification);
    }

    /**
     * Get page by client ID
     * @param clientId Client ID
     * @param pageable Pageable
     * @return page of config
     */
    @Override
    public Page<IngredientConfigEntity> getPage(
            @NotNull final Long clientId,
            @NotNull final Pageable pageable
    ) {
        return repository.findAllByClientId(clientId, pageable);
    }

    /**
     * Get page by filter
     * @param specification Filter
     * @param pageable      Pageable
     * @return page of config
     */
    @Override
    public Page<IngredientConfigEntity> getPage(
            @NotNull final Specification<IngredientConfigEntity> specification,
            @NotNull final Pageable pageable
    ) {
        return repository.findAll(specification, pageable);
    }

    /**
     * Save config
     * @param entity IngredientConfigEntity
     * @return IngredientConfigEntity
     */
    @Override
    public IngredientConfigEntity save(
            @NotNull final IngredientConfigEntity entity
    ) {
        return repository.save(entity);
    }

    /**
     * Delete config
     * @param entity IngredientConfigEntity
     */
    @Override
    public void delete(
            @NotNull final IngredientConfigEntity entity
    ) {
        repository.delete(entity);
    }
}
