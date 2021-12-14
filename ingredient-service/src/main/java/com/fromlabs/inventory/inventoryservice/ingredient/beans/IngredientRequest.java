package com.fromlabs.inventory.inventoryservice.ingredient.beans;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Ingredient request for both category and type on save and update
 */
@Data
public class IngredientRequest {
    private Long id;
    private Long clientId;
    private String name = "";
    private String code = "";
    private String description = "";
    private String unit;
    private String unitType;
    private Float maximumQuantity;
    private Float minimumQuantity = 1F;
    private Long parentId;
    private List<Long> childrenIds = new ArrayList<>();
}
