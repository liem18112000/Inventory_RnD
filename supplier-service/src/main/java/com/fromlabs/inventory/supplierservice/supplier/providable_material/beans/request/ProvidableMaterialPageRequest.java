/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material.beans;


import com.fromlabs.inventory.supplierservice.common.helper.BaseCustomizePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProvidableMaterialPageRequest extends BaseCustomizePageRequest {
    private Long    clientId;
    private Long    supplierId      = -1L;
    private Long    ingredientId    = -1L;
    private String  name            = "";
    private String  description     = "";
    private String  updateAt        = "";
    private float   minimumQuantity = 1F;
    private float   maximumQuantity = 32767F;
}
