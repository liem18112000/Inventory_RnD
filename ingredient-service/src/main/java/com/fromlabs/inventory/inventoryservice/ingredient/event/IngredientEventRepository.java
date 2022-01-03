/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Ingredient event repository
 * @author Liem
 */
@Repository
public interface IngredientEventRepository
        extends JpaRepository<IngredientEventEntity, Long>, JpaSpecificationExecutor<IngredientEventEntity> {

    List<IngredientEventEntity> findAllByClientId(Long clientId);

    IngredientEventEntity findByClientIdAndName(Long clientId, String name);

    Page<IngredientEventEntity> findAllByClientId(Long clientId, Pageable pageable);
}