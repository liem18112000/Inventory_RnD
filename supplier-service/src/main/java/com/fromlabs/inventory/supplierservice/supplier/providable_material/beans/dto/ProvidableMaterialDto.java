/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.dto;

import com.fromlabs.inventory.supplierservice.client.ingredient.bean.IngredientDto;
import com.fromlabs.inventory.supplierservice.common.dto.BaseDto;
import com.fromlabs.inventory.supplierservice.supplier.beans.dto.SupplierDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProvidableMaterialDto extends BaseDto<Long> {

    private float           minimumQuantity;
    private float           maximumQuantity;
    private IngredientDto   ingredient;
    private SupplierDto     supplier;

    @Builder(builderMethodName = "materialBuilder")
    public ProvidableMaterialDto(
            Long id, Long tenantId, String name,
            String description, String updateAt, String createAt,
            boolean activated, float min, float max,
            IngredientDto ingredient, SupplierDto supplier
    ) {
        super(id, tenantId, name, description, updateAt, createAt, activated);
        this.ingredient         = ingredient;
        this.supplier           = supplier;
        this.minimumQuantity    = min;
        this.maximumQuantity    = max;
    }

    public ProvidableMaterialDto() {
    }
}
