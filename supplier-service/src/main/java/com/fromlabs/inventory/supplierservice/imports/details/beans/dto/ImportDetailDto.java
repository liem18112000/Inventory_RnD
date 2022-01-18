/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.details.beans.dto;

import com.fromlabs.inventory.supplierservice.client.ingredient.bean.IngredientDto;
import com.fromlabs.inventory.supplierservice.common.dto.BaseDto;
import com.fromlabs.inventory.supplierservice.imports.beans.dto.ImportDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Import detail DTO
 * @author Liem
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImportDetailDto extends BaseDto<Long> implements Serializable {

    private static final long serialVersionUID = 2706613567738025862L;

    private IngredientDto ingredient;

    private ImportDto imports;

    @Builder(builderMethodName = "importDetailBuilder")
    public ImportDetailDto(
            Long id, Long tenantId, String name,
            String description, String updateAt, String createAt,
            boolean activated, IngredientDto ingredient, ImportDto imports
    ) {
        super(id, tenantId, name, description, updateAt, createAt, activated);
        this.ingredient = ingredient;;
        this.imports    = imports;
    }

    public ImportDetailDto() {
    }
}
