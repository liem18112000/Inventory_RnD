package com.fromlabs.inventory.recipeservice.detail.beans;

import com.fromlabs.inventory.recipeservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.recipeservice.client.ingredient.beans.IngredientDto;
import com.fromlabs.inventory.recipeservice.common.dto.BaseDto;
import com.fromlabs.inventory.recipeservice.detail.RecipeDetailEntity;
import com.fromlabs.inventory.recipeservice.recipe.beans.RecipeDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public RecipeDetailDto() {
        super();
    }

    static public RecipeDetailDto from(
            RecipeDetailEntity  entity,
            IngredientClient    ingredientClient
    ) {
        return RecipeDetailDto.recipeDetailBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .code(entity.getCode())
                .recipe(Objects.isNull(entity.getRecipe()) ? null : RecipeDto.from(entity.getRecipe()))
                .ingredient(ingredientClient.getIngredientById(entity.getClientId(), entity.getIngredientId()))
                .quantity(entity.getQuantity())
                .description(entity.getDescription())
                .updateAt(entity.getUpdateAt())
                .activated(entity.isActivated())
                .build();
    }

    static public List<RecipeDetailDto> from(
            List<RecipeDetailEntity>    entities,
            IngredientClient            ingredientClient
    ) {
        return entities.stream().map(entity -> from(entity, ingredientClient)).collect(Collectors.toList());
    }

    static public Page<RecipeDetailDto> from(
            Page<RecipeDetailEntity>    entities,
            IngredientClient            ingredientClient
    ) {
        return entities.map(entity -> from(entity, ingredientClient));
    }
}
