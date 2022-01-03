package com.fromlabs.inventory.recipeservice.recipe;

import com.fromlabs.inventory.recipeservice.common.service.RestApiService;

import java.util.List;

/**
 * Recipe entity service
 * @author Liem
 */
public interface RecipeService extends RestApiService<RecipeEntity, Long> {

    /**
     * Get recipe by code
     * @param code Recipe unique codee
     * @return RecipeEntity
     */
    RecipeEntity getByCode(String code);

    /**
     * Get recipe entity by client di and name
     * @param clientId  Client ID
     * @param name Recipe name
     * @return RecipeEntity
     */
    RecipeEntity getByName(Long clientId, String name);

    /**
     * Get all recipe entity by client id
     * @param clientId Client ID
     * @return List of recipe entity
     */
    List<RecipeEntity> getAll(Long clientId);

    /**
     * Get all recipe list by client ID and parent ID
     * @param clientId Client ID
     * @param parentId Parent ID
     * @return List of recipe entity
     */
    List<RecipeEntity> getAll(Long clientId, Long parentId);
}
