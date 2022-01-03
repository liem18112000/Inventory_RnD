package com.fromlabs.inventory.inventoryservice.client.recipe.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fromlabs.inventory.inventoryservice.common.dto.BaseDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeDetailDto extends BaseDto<Long> {

    @NotNull
    @NotBlank
    private String code;

    @NotNull
    private IngredientDto ingredient;

    @Min(value = 0, message = "Quantity must be greater or equal zero")
    private Float quantity;

    @JsonIgnore
    private RecipeDto recipe;
}
