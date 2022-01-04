/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request;

import lombok.Data;

@Data
public class IngredientHistoryRequest{
    private final Long      id;
    private final Long      clientId;
    private final Long      ingredientId;
    private final String    name;
    private final String    actorName;
    private final String    actorRole;
    private final String    event;
    private final String    eventStatus;
    private final String    description;
    private final String    extraInformation;
    private final boolean   active = true;
}
