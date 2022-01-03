/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Ingredient history entity repository
 */
@Repository
public interface IngredientHistoryRepository
        extends JpaRepository<IngredientHistoryEntity, Long>, JpaSpecificationExecutor<IngredientHistoryEntity> {
    List<IngredientHistoryEntity> findAllByClientIdAndIngredient(Long clientId, IngredientEntity ingredient);
    List<IngredientHistoryEntity> findAllByClientId(Long clientId);
}
