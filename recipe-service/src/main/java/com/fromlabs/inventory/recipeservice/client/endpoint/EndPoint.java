/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.recipeservice.client.endpoint;

import com.fromlabs.inventory.recipeservice.detail.beans.dto.RecipeDetailDto;
import com.fromlabs.inventory.recipeservice.media.bean.MediaDto;
import com.fromlabs.inventory.recipeservice.recipe.beans.dto.RecipeDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Recipe endpoint interface for internally expose API to other service
 */
public interface EndPoint {
    /**
     * Get recipe child and group by id
     * @param id    Entity ID
     * @return      ResponseEntity
     */
    RecipeDto getById(
            Long id
    );

    /**
     * Get recipe group and child by code
     * @param code  Recipe code
     * @return      ResponseEntity
     */
    RecipeDto getByCode(
            String code
    );

    /**
     * Get all recipe group as list
     * @param tenantId  Tenant ID
     * @return          ResponseEntity
     */
    List<RecipeDto> getAllGroup(
            Long tenantId
    );

    /**
     * Get all recipe child as list
     * @param tenantId  Tenant Id
     * @param parentId  Parent Id
     * @return          ResponseEntity
     */
    List<RecipeDto> getAllChild(
            Long tenantId,
            Long parentId
    );

    /**
     * Get list of recipe detail with tenantId
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    List<RecipeDetailDto> getDetailAll(
            Long tenantId
    );

    /**
     * Get list of recipe detail with tenantId and recipe
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    List<RecipeDetailDto> getDetailAll(
            Long tenantId,
            Long recipeId
    );

    /**
     * Get recipe detail by id
     * @param id    Entity ID
     * @return      ResponseEntity
     */
    RecipeDetailDto getDetailById(
            Long id
    );

    /**
     * Get recipe detail by code
     * @param code  Recipe detail code
     * @return      ResponseEntity
     */
    RecipeDetailDto getDetailByCode(
            String code
    );

    /**
     * Check recipe detail exist by ingredient
     * @param ingredientId Ingredient ID
     * @return true if existed. Otherwise, false
     */
    boolean existByIngredient(
            Long ingredientId
    );

    MediaDto getMedia(
            Long id
    );
}
