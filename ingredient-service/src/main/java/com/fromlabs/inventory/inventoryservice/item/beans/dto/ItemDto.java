/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.beans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import lombok.Builder;
import lombok.Data;

/**
 * Item Dto
 */
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private Long id;
    private Long clientId;
    private IngredientDto ingredient;
    private Long importId;
    private String name;
    private String code;
    private String description;
    private String unit;
    private String unitType;
    private String expiredAt;

}
