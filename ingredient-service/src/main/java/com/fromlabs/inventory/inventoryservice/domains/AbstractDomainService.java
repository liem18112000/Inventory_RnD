/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.item.ItemService;

/**
 * Abstract domain service layers
 */
abstract public class AbstractDomainService {

    protected final IngredientService ingredientService;
    protected final InventoryService inventoryService;
    protected final ItemService itemService;

    /**
     * The constructor is initialized with three parameter (see in Parameters)
     * @param ingredientService The service of IngredientEntity
     * @param inventoryService  The service of InventoryEntity
     * @param itemService       The service of ItemEntity
     */
    protected AbstractDomainService(
            IngredientService   ingredientService,
            InventoryService    inventoryService,
            ItemService         itemService
    ) {
        this.ingredientService  = ingredientService;
        this.inventoryService   = inventoryService;
        this.itemService        = itemService;
    }
}
