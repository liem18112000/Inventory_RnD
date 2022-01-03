/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.beans.request;

import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryEntity;
import lombok.Data;

/**
 * Item request
 */
@Data
public class ItemRequest {
    private   Long   id;
    protected Long   clientId;
    protected Long   ingredientId;
    protected Long   importId;
    protected String name;
    protected String code;
    protected String description;
    protected String unit;
    protected String unitType;
    protected String expiredAt;
    protected String createAt;
    protected String updateAt;
    protected String actorName = IngredientHistoryEntity.DEFAULT_ACTOR_ROLE;
    protected String actorRole = IngredientHistoryEntity.DEFAULT_ACTOR_ROLE;
}
