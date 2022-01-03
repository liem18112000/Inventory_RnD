package com.fromlabs.inventory.recipeservice.detail;

import com.fromlabs.inventory.recipeservice.common.service.RestApiService;
import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Recipe detail service
 * @author Liem
 */
public interface RecipeDetailService extends RestApiService<RecipeDetailEntity, Long> {

    /**
     * Get entity by unique code
     * @param code  Unique code
     * @return      RecipeDetailEntity
     */
    RecipeDetailEntity getByCode(String code);

    /**
     * Get entity by client id and name
     * @param clientId  Client ID
     * @param name      Entity name
     * @return          RecipeDetailEntity
     */
    RecipeDetailEntity getByName(Long clientId, String name);
}
