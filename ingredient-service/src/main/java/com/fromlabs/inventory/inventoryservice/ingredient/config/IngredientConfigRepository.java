/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.config;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Ingredient repository
 */
@Repository
public interface IngredientConfigRepository extends JpaRepository<IngredientConfigEntity, Long>, JpaSpecificationExecutor<IngredientConfigEntity> {
    IngredientConfigEntity findByClientIdAndIngredient(Long clientId, IngredientEntity entity);
    List<IngredientConfigEntity> findAllByClientId(Long clientId);
    Page<IngredientConfigEntity> findAllByClientId(Long clientId, Pageable pageable);
}
