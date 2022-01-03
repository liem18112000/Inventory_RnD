/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.beans.request;

import com.fromlabs.inventory.inventoryservice.common.helper.BaseCustomizePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Item page request
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemPageRequest extends BaseCustomizePageRequest {
    private Long clientId;
    private String name = "";
    private String description = "";
    private String code = "";
    private String unit = "";
    private String unitType = "";
    private String expiredAt = "";
    private Long ingredientId = -1L;
    private Long importId = -1L;
    private String updateAt = "";
}
