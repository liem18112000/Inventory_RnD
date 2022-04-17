/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.specification;

import com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.inventoryservice.common.specifications.SearchCriteria.*;
import static java.util.Objects.isNull;

/**
 * Ingredient specification for filter
 */
public class IngredientSpecification {

    /**
     * Filter for has client id
     * @param clientId  Client ID
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    /**
     * Filter for has parent id
     * @param parent    Parent Ingredient entity
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasParent(IngredientEntity parent) {
        return Spec(criteriaEqual("parent", parent));
    }

    /**
     * Filter for has code
     * @param code      ingredient code
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasCode(String code) {
        return Spec(criteriaEqual("code", code));
    }

    /**
     * Filter for has name
     * @param name      ingredient name
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name));
    }

    /**
     * Filter for has unit
     * @param unit      ingredient unit
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasUnit(String unit) {
        return Spec(criteriaStrictlyEqual("unit", unit, true));
    }

    /**
     * Filter for has unit type
     * @param unitType  ingredient unit type
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasUnitType(String unitType) {
        return Spec(criteriaStrictlyEqual("unitType", unitType, true));
    }

    /**
     * Filter for has description
     * @param description ingredient description
     * @return            BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description));
    }

    /**
     * Filter for has category flag
     * @param category  is category boolean flag
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasCategory(boolean category) {
        return Spec(criteriaEqual("category", category));
    }

    /**
     * Filter for has created at
     * @param createdAt create timestamp
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasCreateAt(String createdAt) {
        return Spec(criteriaEqual("createAt", createdAt));
    }

    /**
     *
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public static BaseSpecification<IngredientEntity> hasUpdatedAt(String updateAt) {
        return Spec(criteriaEqual("updateAt", updateAt));
    }

    public static BaseSpecification<IngredientEntity> hasUpdateFrom(String updateAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("updateAt", updateAt));
    }

    public static BaseSpecification<IngredientEntity> hasCreateFrom(String updateAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("createAt", updateAt));
    }

    /**
     * Filter for all ingredient
     * @param entity    IngredientEntity
     * @return          Specification&lt;IngredientEntity&gt;
     */
    public static Specification<IngredientEntity> filter(IngredientEntity entity, IngredientEntity parent) {
        var spec = hasClientId(entity.getClientId())
                .and(hasName(entity.getName()))
                .and(hasCode(entity.getCode()))
                .and(hasUnit(entity.getUnit()))
                .and(hasDescription(entity.getDescription()))
                .and(hasUnitType(entity.getUnitType()))
                .and(hasCategory(entity.isCategory()))
                .and(StringUtils.hasText(entity.getUpdateAt()) ?
                        hasUpdateFrom(entity.getUpdateAt()) : hasUpdatedAt(entity.getUpdateAt()))
                .and(StringUtils.hasText(entity.getCreateAt()) ?
                        hasCreateFrom(entity.getCreateAt()) : hasCreateAt(entity.getCreateAt()))
                ;
        return isNull(parent) ? spec : spec.and(hasParent(parent));
    }
}
