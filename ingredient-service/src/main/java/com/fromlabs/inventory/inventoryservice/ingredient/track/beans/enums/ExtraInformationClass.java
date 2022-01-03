/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track.beans.enums;

import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.*;
import lombok.Getter;

public enum ExtraInformationClass {

    ITEM_REQUEST(ItemRequest.class, ItemRequest.class.getSimpleName()),
    BATCH_ITEM_REQUEST(BatchItemsRequest.class, BatchItemsRequest.class.getSimpleName()),
    INGREDIENT_REQUEST(IngredientRequest.class, IngredientRequest.class.getSimpleName());

    ExtraInformationClass(Class<?> clazz, String className) {
        this.clazz = clazz;
        this.className = className;
    }

    @Getter
    private final Class<?> clazz;

    @Getter
    private final String className;
}
