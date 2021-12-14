package com.fromlabs.inventory.notificationservice.client.ingredient.bean;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Ingredient Data Transfer Object
 */
@Builder(toBuilder = true)
@Data
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
