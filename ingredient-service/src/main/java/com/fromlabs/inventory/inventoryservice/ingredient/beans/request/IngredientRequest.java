/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.beans.request;

import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.fromlabs.inventory.inventoryservice.ingredient.beans.unit.IngredientUnit.GENERIC;

/**
 * Ingredient request for both category and type on save and update
 */
@Data
@NoArgsConstructor
public class IngredientRequest {
    protected Long id;
    protected Long parentId;
    protected Long clientId;
    protected String name             = "";
    protected String code             = "";
    protected String description      = "";
    protected String unit             = GENERIC;
    protected String unitType         = GENERIC;
    protected Float  maximumQuantity  = 10000F;
    protected Float  minimumQuantity  = 1F;
    protected List<Long> childrenIds  = new ArrayList<>();
    protected String actorName = IngredientHistoryEntity.DEFAULT_ACTOR_ROLE;
    protected String actorRole = IngredientHistoryEntity.DEFAULT_ACTOR_ROLE;
}
