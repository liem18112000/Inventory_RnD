package com.fromlabs.inventory.inventoryservice.domains.restaurant.beans;

import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientSuggestion {
    private IngredientDto ingredient;
    private int quantity;
}
