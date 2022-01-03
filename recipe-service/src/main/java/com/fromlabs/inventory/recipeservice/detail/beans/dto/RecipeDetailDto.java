package com.fromlabs.inventory.recipeservice.detail.beans.dto;

import com.fromlabs.inventory.recipeservice.client.ingredient.beans.IngredientDto;
import com.fromlabs.inventory.recipeservice.common.dto.BaseDto;
import com.fromlabs.inventory.recipeservice.recipe.beans.dto.RecipeDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeDetailDto extends BaseDto<Long> implements Serializable {

    private String code;
    private IngredientDto ingredient;
    private float quantity;
    private RecipeDto recipe;

    @Builder(builderMethodName = "recipeDetailBuilder")
    public RecipeDetailDto(Long id, Long tenantId, String name, String code, String description,
                           float quantity, RecipeDto recipe,
                           IngredientDto ingredient, String updateAt, String createAt, boolean activated) {
        super(id, tenantId, name, description, updateAt, createAt, activated);
        this.ingredient     = ingredient;
        this.recipe         = recipe;
        this.quantity       = quantity;
        this.code           = code;
    }
}
