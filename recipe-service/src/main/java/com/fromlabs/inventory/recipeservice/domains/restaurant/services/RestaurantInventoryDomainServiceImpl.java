/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.recipeservice.domains.restaurant.services;

import com.fromlabs.inventory.recipeservice.detail.RecipeDetailService;
import com.fromlabs.inventory.recipeservice.domains.AbstractDomainService;
import com.fromlabs.inventory.recipeservice.recipe.RecipeService;
import org.springframework.stereotype.Service;

@Service
public class RestaurantInventoryDomainServiceImpl
        extends AbstractDomainService
        implements RestaurantInventoryDomainService {

    public RestaurantInventoryDomainServiceImpl(
            RecipeService recipeService,
            RecipeDetailService recipeDetailService
    ) {
        super(recipeService, recipeDetailService);
    }
}
