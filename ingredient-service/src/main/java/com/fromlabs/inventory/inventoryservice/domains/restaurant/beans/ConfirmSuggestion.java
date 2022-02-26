package com.fromlabs.inventory.inventoryservice.domains.restaurant.beans;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ConfirmSuggestion {

    @Builder.Default
    private List<IngredientSuggestion> ingredientSuggestions = new ArrayList<>();

    @Builder.Default
    private List<IngredientSuggestion> ingredientRemain = new ArrayList<>();

    @Builder.Default
    private boolean lowStockAlert = false;

    @Builder.Default
    private boolean confirmSuggestion = false;

    @Builder.Default
    private int taxonQuantity = 0;

    private final String confirmAt = Instant.now().toString();
}
