package com.fromlabs.inventory.recipeservice.recipe.specification;

import com.fromlabs.inventory.recipeservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.fromlabs.inventory.recipeservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.recipeservice.common.specifications.SearchCriteria.criteriaEqual;
import static com.fromlabs.inventory.recipeservice.common.specifications.SearchCriteria.criteriaTimestampGreaterThanOrEqual;

public class RecipeSpecification {

    public static BaseSpecification<RecipeEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    public static BaseSpecification<RecipeEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name.trim()));
    }

    public static BaseSpecification<RecipeEntity> hasCode(String code) {
        return Spec(criteriaEqual("code", code.trim()));
    }

    public static BaseSpecification<RecipeEntity> hasParent(RecipeEntity parent) {
        return Spec(criteriaEqual("parent", parent));
    }

    public static BaseSpecification<RecipeEntity> hasGroup(boolean isGroup) {
        return Spec(criteriaEqual("group", isGroup));
    }

    public static BaseSpecification<RecipeEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description.trim()));
    }

    public static BaseSpecification<RecipeEntity> hasUpdateAt(String updateAt) {
        return Spec(criteriaEqual("updateAt", updateAt.trim()));
    }

    public static BaseSpecification<RecipeEntity> hasUpdateFrom(String updateAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("updateAt", updateAt.trim()));
    }

    public static Specification<RecipeEntity> filter(RecipeEntity entity, RecipeEntity parent) {
         var spec = hasClientId(entity.getClientId())
                 .and(hasName(entity.getName()))
                 .and(hasDescription(entity.getDescription()))
                 .and(hasCode(entity.getCode()))
                 .and(StringUtils.hasText(entity.getUpdateAt()) ?
                         hasUpdateFrom(entity.getUpdateAt()) :
                         hasUpdateAt(entity.getUpdateAt()));
        return Objects.nonNull(parent) ? spec.and(hasParent(parent)) : spec.and(hasGroup(entity.isGroup()));
    }
}