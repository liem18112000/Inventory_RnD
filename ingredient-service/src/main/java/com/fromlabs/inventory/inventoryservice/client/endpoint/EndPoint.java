/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.client.endpoint;

import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;

import java.util.List;

/**
 * Ingredient endpoint interface for internally expose API to other service
 */
public interface EndPoint {
    /**
     * Return all ingredient category (ingredient parent) as a list
     * @param tenantId  The client id (must be provided)
     * @return          List&lt;IngredientDto&gt;
     */
    List<IngredientDto> getAllIngredientCategory(
            Long tenantId
    );

    /**
     * Return a ResponseEntity object of all ingredient type (ingredient child) as a list
     * @param tenantId  The client id (must be provided)
     * @param parentId  The id of a parent category ingredient (must be provided)
     * @return          List&lt;IngredientEntity&gt;
     */
    List<IngredientDto> getAllIngredientType(
            Long tenantId,
            Long parentId
    );

    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with code
     * @param tenantId  The client id (must be provided)
     * @param code      The unique code for identify a ingredient
     * @return          IngredientDto
     */
    IngredientDto getIngredient(
            Long tenantId,
            String code
    );

    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with ID
     * @param tenantId  The client id (must be provided)
     * @param id        The ID (unique) of an ingredient
     * @return          IngredientDto
     */
    IngredientDto getIngredientById(
            Long tenantId,
            Long id
    );
}
