/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory.beans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import lombok.Builder;
import lombok.Data;

/**
 * Inventory DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
@Data
public class InventoryDto {
    private Long id;
    private Long clientId;
    private IngredientDto ingredient;
    private String name;
    private String description;
    private Float quantity;
    private Float reserved;
    private Float available;
    private String unit;
    private String unitType;
}
