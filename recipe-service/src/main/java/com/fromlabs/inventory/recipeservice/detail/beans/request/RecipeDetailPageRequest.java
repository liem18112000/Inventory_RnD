package com.fromlabs.inventory.recipeservice.detail.beans.request;

import com.fromlabs.inventory.recipeservice.detail.RecipeDetailEntity;
import com.fromlabs.inventory.recipeservice.detail.factory.RecipeDetailEntityFactory;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.fromlabs.inventory.recipeservice.common.factory.FactoryCreateType.DEFAULT;


@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeDetailPageRequest extends RecipePageRequest {

    protected Long recipeId     = null;
    protected Long ingredientId = null;
    protected Float quantity    = 0f;

    public RecipeDetailEntity to() {
        var entity = RecipeDetailEntityFactory.create(DEFAULT);
        entity.setClientId(this.getTenantId());
        entity.setName(this.getName());
        entity.setCode(this.getCode());
        entity.setDescription(this.getDescription());
        entity.setIngredientId(this.getIngredientId());
        return entity;
    }
}
