/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.config.beans.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.common.dto.BaseDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientConfigDto extends BaseDto<Long> {

    @Builder(builderMethodName = "configBuilder")
    public IngredientConfigDto(
            Long id, Long clientId, String name,
            String description, String updateAt, String createAt,
            boolean isActive, IngredientDto ingredient,
            Float minimumQuantity, Float maximumQuantity
    ) {
        super(id, clientId, name, description, updateAt, createAt, isActive);
        this.ingredient         = ingredient;
        this.minimumQuantity    = minimumQuantity;
        this.maximumQuantity    = maximumQuantity;
    }

    private IngredientDto ingredient;

    private Float minimumQuantity;

    private Float maximumQuantity;

}
