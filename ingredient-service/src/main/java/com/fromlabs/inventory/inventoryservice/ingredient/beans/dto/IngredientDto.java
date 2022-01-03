/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.beans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Ingredient Data Transfer Object
 */
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDto {
    private Long id;
    private Long clientId;
    private String name;
    private String code;
    private String description;
    private List<IngredientDto> children;
    private String unit;
    private String unitType;
    private Float quantity;
    private String createAt;
    private String updateAt;
}
