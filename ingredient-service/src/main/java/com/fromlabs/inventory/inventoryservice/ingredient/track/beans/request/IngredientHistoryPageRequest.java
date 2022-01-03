/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request;

import com.fromlabs.inventory.inventoryservice.common.helper.BaseCustomizePageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientHistoryPageRequest extends BaseCustomizePageRequest {
    private Long   ingredientId     = -1L;
    private Long   clientId;
    private String name             = "";
    private String description      = "";
    private String actorName        = "";
    private String actorRole        = "";
    private String event            = "";
    private String status           = "";
    private String extraInformation = "";
    private String trackTimestamp   = "";
    private String updateAt         = "";
}
