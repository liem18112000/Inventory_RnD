/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory.beans.request;

import com.fromlabs.inventory.inventoryservice.common.helper.BaseCustomizePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Inventory page request
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InventoryPageRequest extends BaseCustomizePageRequest {
    private Long clientId;
    private Long ingredientId = -1L;
    private String name = "";
    private String description = "";
    private float quantity = 0;
    private String unit = "";
    private String unitType = "";
    private String updateAt = "";
}
