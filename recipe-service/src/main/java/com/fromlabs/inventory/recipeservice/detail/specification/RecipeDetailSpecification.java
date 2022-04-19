package com.fromlabs.inventory.recipeservice.detail.specification;

import com.fromlabs.inventory.recipeservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.recipeservice.detail.RecipeDetailEntity;
import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import static com.fromlabs.inventory.recipeservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.recipeservice.common.specifications.SearchCriteria.*;
import static java.util.Objects.isNull;

public class RecipeDetailSpecification {

    public static BaseSpecification<RecipeDetailEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    public static BaseSpecification<RecipeDetailEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name.trim()));
    }

    public static BaseSpecification<RecipeDetailEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description.trim()));
    }

    public static BaseSpecification<RecipeDetailEntity> hasQuantity(Float quantity) {
        return Spec(criteriaGreaterThanOrEqual("quantity", quantity));
    }

    public static BaseSpecification<RecipeDetailEntity> hasIngredientId(Long ingredientId) {
        return Spec(criteriaEqual("ingredientId", ingredientId));
    }

    public static BaseSpecification<RecipeDetailEntity> hasCode(String code) {
        return Spec(criteriaEqual("code", code.trim()));
    }

    public static BaseSpecification<RecipeDetailEntity> hasRecipe(RecipeEntity recipe) {
        return Spec(criteriaEqual("recipe", recipe));
    }

    public static BaseSpecification<RecipeDetailEntity> hasUpdateAt(String updateAt) {
        return Spec(criteriaEqual("updateAt", updateAt.trim()));
    }

    public static BaseSpecification<RecipeDetailEntity> hasUpdateFrom(String updateAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("updateAt", updateAt.trim()));
    }

    public static Specification<RecipeDetailEntity> filter(RecipeDetailEntity entity, RecipeEntity recipe) {
        var spec =  hasClientId(entity.getClientId())
                .and(hasCode(entity.getCode()))
                .and(hasName(entity.getName()))
                .and(hasDescription(entity.getDescription()))
                .and(hasQuantity(entity.getQuantity()))
                .and(StringUtils.hasText(entity.getUpdateAt()) ?
                        hasUpdateFrom(entity.getUpdateAt()) :
                        hasUpdateAt(entity.getUpdateAt()));;
        spec = isNull(entity.getIngredientId()) ? spec : spec.and(hasIngredientId(entity.getIngredientId()));
        return isNull(recipe) ? spec : spec.and(hasRecipe(recipe));
    }
}
