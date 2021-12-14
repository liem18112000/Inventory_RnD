/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Inventory repository
 */
@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long>, JpaSpecificationExecutor<InventoryEntity> {
    InventoryEntity findByClientIdAndName(Long clientId, String name);
    InventoryEntity findByIngredient(IngredientEntity ingredient);
    List<InventoryEntity> findAllByClientId(Long clientId);
    List<InventoryEntity> findAllByClientIdAndUnitType(Long clientId, String unitType);
    Page<InventoryEntity> findAllByClientId(Long clientId, Pageable pageable);
    Page<InventoryEntity> findAll(Specification<InventoryEntity> specification, Pageable pageable);
}
