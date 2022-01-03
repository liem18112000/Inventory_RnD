/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request;

import com.fromlabs.inventory.inventoryservice.ingredient.event.IngredientEvent;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatus;
import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryEntity;
import lombok.Data;

@Data
public class IngredientHistoryRequest{
    private final Long      id;
    private final Long      clientId;
    private final Long      ingredientId;
    private final String    name;
    private final String    actorName = IngredientHistoryEntity.DEFAULT_ACTOR_ROLE;
    private final String    actorRole = IngredientHistoryEntity.DEFAULT_ACTOR_ROLE;
    private final String    event = IngredientEvent.INGREDIENT_ITEM_DEFAULT.getEvent();
    private final String    eventStatus = IngredientEventStatus.UNKNOWN.getName();
    private final String    description;
    private final String    extraInformation;
    private final boolean   active = true;
}
