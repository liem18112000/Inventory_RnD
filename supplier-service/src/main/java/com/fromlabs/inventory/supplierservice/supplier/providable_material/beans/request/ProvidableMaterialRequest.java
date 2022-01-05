/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request;

import lombok.Data;

@Data
public class ProvidableMaterialRequest {
    private Long    id;
    private Long    clientId;
    private Long    ingredientId;
    private Long    supplierId;
    private String  name;
    private String  description;
    private float   minimumQuantity = 1f;
    private float   maximumQuantity = 32767F;
    private boolean isActive        = true;
}
