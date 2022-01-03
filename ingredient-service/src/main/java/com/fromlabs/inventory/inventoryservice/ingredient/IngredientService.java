/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient;

import com.fromlabs.inventory.inventoryservice.common.service.RestApiService;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Ingredient service
 */
public interface IngredientService extends RestApiService<IngredientEntity, Long> {

    //<editor-fold desc="INGREDIENT">

    IngredientEntity getByName(Long clientId, String name);
    IngredientEntity getByCode(String code);

    Page<IngredientEntity> getPage(Long clientId, Pageable pageable);
    List<IngredientEntity> getAll(Long clientId);

    List<IngredientEntity> getAllChild(Long clientId);

    Page<IngredientEntity> getPage(Long clientId, Long parentId, Pageable pageable);
    List<IngredientEntity> getAll(Long clientId, Long parentId);

    //</editor-fold>

    //<editor-fold desc="INGREDIENT CONFIG">

    IngredientConfigEntity getConfig(Long id);
    IngredientConfigEntity getConfig(Long clientId, IngredientEntity entity);

    List<IngredientConfigEntity> getAllConfig(Long clientId);
    Page<IngredientConfigEntity> getPageConfig(Long clientId, Pageable pageable);

    Page<IngredientConfigEntity> getPageConfig(Specification<IngredientConfigEntity> specification, Pageable pageable);
    List<IngredientConfigEntity> getAllConfig(Specification<IngredientConfigEntity> specification);

    IngredientConfigEntity saveConfig(IngredientConfigEntity entity);
    void deleteConfig(IngredientConfigEntity entity);

    //</editor-fold>
}
