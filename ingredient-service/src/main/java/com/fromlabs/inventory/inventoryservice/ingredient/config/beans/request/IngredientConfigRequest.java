/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.config.beans.request;

import lombok.Data;

/**
 * Ingredient config request
 */
@Data
public class IngredientConfigRequest {
    private Long id;
    private Long clientId;
    private Float minimumQuantity;
    private Float maximumQuantity;
}
