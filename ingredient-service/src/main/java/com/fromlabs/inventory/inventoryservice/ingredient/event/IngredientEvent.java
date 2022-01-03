/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public enum IngredientEvent {

    INGREDIENT_ITEM_DEFAULT("do_nothing", "default event as nothing happen"),

    INGREDIENT_ITEM_ADD("ingredient_item_add", "event activated on adding a single item"),
    INGREDIENT_ITEM_BATCH_ADD("ingredient_item_batch_add", "event activated on adding a batch of items"),
    INGREDIENT_ITEM_REMOVE("ingredient_item_remove", "event activated on removing a single item"),
    INGREDIENT_ITEM_MODIFY("ingredient_item_modify", "event activated on modifying a single item"),

    INGREDIENT_CREATE("ingredient_add", "event activated on creating a ingredient"),
    INGREDIENT_REMOVE("ingredient_remove", "event activated on removing a ingredient"),
    INGREDIENT_MODIFY("ingredient_modify", "event activated on modifying a ingredient"),

    INGREDIENT_CONFIG_MODIFY("ingredient_config_modify", "event activated on modifying a ingredient config");

    private final String event;
    private final String eventMessage;

    IngredientEvent(
            @NotNull @NotBlank final String event,
            @NotNull @NotBlank final String eventMessage
    ) {
        this.event = event;
        this.eventMessage = eventMessage;
    }
}
