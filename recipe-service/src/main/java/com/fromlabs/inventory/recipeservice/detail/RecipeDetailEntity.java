package com.fromlabs.inventory.recipeservice.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fromlabs.inventory.recipeservice.detail.beans.RecipeDetailPageRequest;
import com.fromlabs.inventory.recipeservice.detail.beans.RecipeDetailRequest;
import com.fromlabs.inventory.recipeservice.detail.factory.RecipeDetailEntityFactory;
import com.fromlabs.inventory.recipeservice.entity.RecipeBaseEntity;
import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import com.fromlabs.inventory.recipeservice.recipe.beans.RecipeRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.fromlabs.inventory.recipeservice.common.factory.FactoryCreateType.EMPTY;
import static com.fromlabs.inventory.recipeservice.detail.factory.RecipeDetailEntityFactory.create;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="recipe_detail")
public class RecipeDetailEntity extends RecipeBaseEntity<Long> {

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    @ToString.Exclude
    protected RecipeEntity recipe;

    @NotNull(message = "Ingredient id should not be null")
    @Column(name="ingredient_id")
    private Long ingredientId;

    @Min(value = 0, message = "Quantity should be equal or greater than zero")
    @Column(name="quantity")
    private Float quantity = 0f;

    public static RecipeDetailEntity from(RecipeDetailPageRequest request) {
        var entity = create(EMPTY);
        entity.setIngredientId(request.getIngredientId());
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setQuantity(request.getQuantity());
        return entity;
    }

    public static RecipeDetailEntity from(RecipeDetailRequest request, RecipeEntity recipe) {
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

    public static RecipeDetailEntity update(RecipeDetailRequest request, RecipeDetailEntity entity) {
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setQuantity(request.getQuantity());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecipeDetailEntity that = (RecipeDetailEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 2135026788;
    }
}
