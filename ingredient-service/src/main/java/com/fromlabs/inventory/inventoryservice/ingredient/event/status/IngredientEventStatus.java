/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event.status;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Ingredient history event status
 * @author Liem
 */
public enum IngredientEventStatus {

    SUCCESS("success", "event successfully occurred"),
    PENDING("pending", "event is pending"),
    FAILED("failed", "event failed to occurred"),
    UNKNOWN("unknown","event status is unknown");

    @Getter
    private final String name;

    @Getter
    private final String message;

    IngredientEventStatus(
            @NotNull @NotBlank final String name,
            @NotNull @NotBlank final String message
    ) {
        this.name = name;
        this.message = message;
    }
}
