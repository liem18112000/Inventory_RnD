package com.fromlabs.inventory.recipeservice.detail.mapper;

import com.fromlabs.inventory.recipeservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.recipeservice.detail.RecipeDetailEntity;
import com.fromlabs.inventory.recipeservice.detail.beans.dto.RecipeDetailDto;
import com.fromlabs.inventory.recipeservice.detail.beans.request.RecipeDetailPageRequest;
import com.fromlabs.inventory.recipeservice.detail.beans.request.RecipeDetailRequest;
import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import com.fromlabs.inventory.recipeservice.recipe.beans.dto.RecipeDto;
import com.fromlabs.inventory.recipeservice.recipe.mapper.RecipeMapper;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.recipeservice.common.factory.FactoryCreateType.EMPTY;
import static com.fromlabs.inventory.recipeservice.detail.factory.RecipeDetailEntityFactory.create;

/**
 * Recipe detail mapper
 * @author Liem
 */
@UtilityClass
public class RecipeDetailMapper {

    //<editor-fold desc="CONVERT TO DTO">

    public RecipeDetailDto toDto(
            @NotNull final RecipeDetailEntity entity,
            @NotNull final IngredientClient ingredientClient
    ) {
        return RecipeDetailDto.recipeDetailBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .code(entity.getCode())
                .recipe(Objects.isNull(entity.getRecipe()) ? null : RecipeMapper.toDto(entity.getRecipe()))
                .ingredient(ingredientClient.getIngredientById(entity.getClientId(), entity.getIngredientId()))
                .quantity(entity.getQuantity())
                .description(entity.getDescription())
                .updateAt(entity.getUpdateAt())
                .activated(entity.isActivated())
                .build();
    }

    public List<RecipeDetailDto> toDto(
            @NotNull final List<RecipeDetailEntity>    entities,
            @NotNull final IngredientClient            ingredientClient
    ) {
        return entities.stream().map(entity -> toDto(entity, ingredientClient)).collect(Collectors.toList());
    }

    public Page<RecipeDetailDto> toDto(
            @NotNull final Page<RecipeDetailEntity>    entities,
            @NotNull final IngredientClient            ingredientClient
    ) {
        return entities.map(entity -> toDto(entity, ingredientClient));
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT TO ENTITY">

    public RecipeDetailEntity toEntity(
            @NotNull final RecipeDetailPageRequest request
    ) {
        var entity = create(EMPTY);
        entity.setIngredientId(request.getIngredientId());
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setQuantity(request.getQuantity());
        return entity;
    }

    public RecipeDetailEntity toEntity(
            @NotNull final RecipeDetailRequest request,
            @NotNull final RecipeEntity recipe
    ) {
        var entity = create(EMPTY);
        entity.setRecipe(recipe);
        entity.setIngredientId(request.getIngredientId());
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setQuantity(request.getQuantity());
        entity.setDescription(request.getDescription());
        entity.setActivated(request.isActivated());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    //</editor-fold>
}
