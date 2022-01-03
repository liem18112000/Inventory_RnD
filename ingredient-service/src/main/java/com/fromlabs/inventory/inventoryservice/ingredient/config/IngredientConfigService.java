/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.config;

import com.fromlabs.inventory.inventoryservice.common.service.RestApiService;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Ingredient config service
 * @author Liem
 */
public interface IngredientConfigService
        extends RestApiService<IngredientConfigEntity, Long> {

    /**
     * Get config by ingredient
     * @param clientId  Client ID
     * @param entity    IngredientEntity
     * @return          IngredientConfigEntity
     */
    IngredientConfigEntity getByIngredient(Long clientId, IngredientEntity entity);

    /**
     * Get all by client id
     * @param clientId      Client ID
     * @return              List of config
     */
    List<IngredientConfigEntity> getAll(Long clientId);

    /**
     * Get page by client ID
     * @param clientId      Client ID
     * @param pageable      Pageable
     * @return              page of config
     */
    Page<IngredientConfigEntity> getPage(Long clientId, Pageable pageable);
}
