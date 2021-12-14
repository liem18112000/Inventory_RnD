/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.recipeservice.domains;

import com.fromlabs.inventory.recipeservice.detail.RecipeDetailService;
import com.fromlabs.inventory.recipeservice.recipe.RecipeService;

abstract public class AbstractDomainService {

    protected final RecipeService recipeService;
    protected final RecipeDetailService recipeDetailService;

    /**
     * The constructor is initialized with two parameter (see in Parameters)
     * @param recipeService The service of RecipeEntity
     * @param recipeDetailService  The service of RecipeDetailEntity
     */
    protected AbstractDomainService(
            RecipeService           recipeService,
            RecipeDetailService     recipeDetailService
    ) {
        this.recipeService          = recipeService;
        this.recipeDetailService    = recipeDetailService;
    }
}
