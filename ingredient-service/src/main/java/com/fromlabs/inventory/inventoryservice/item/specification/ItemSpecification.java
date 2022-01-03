/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.specification;

import com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.item.ItemEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.inventoryservice.common.specifications.SearchCriteria.criteriaEqual;

/**
 * Item specification for filter
 */
public class ItemSpecification {

    public static BaseSpecification<ItemEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    public static BaseSpecification<ItemEntity> hasIngredient(IngredientEntity ingredient) {
        return Spec(criteriaEqual("ingredient", ingredient));
    }

    public static BaseSpecification<ItemEntity> hasCode(String code) {
        return Spec(criteriaEqual("code", code));
    }

    public static BaseSpecification<ItemEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name));
    }

    public static BaseSpecification<ItemEntity> hasUnit(String unit) {
        return Spec(criteriaEqual("unit", unit));
    }

    public static BaseSpecification<ItemEntity> hasUnitType(String unitType) {
        return Spec(criteriaEqual("unitType", unitType));
    }

    public static BaseSpecification<ItemEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description));
    }

    /**
     * Filter for has updated at
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;ItemEntity&gt;
     */
    public static BaseSpecification<ItemEntity> hasUpdatedAt(String updateAt) {
        return Spec(criteriaEqual("updateAt", updateAt));
    }

    public static Specification<ItemEntity> filter(ItemEntity entity, IngredientEntity ingredient) {
        var spec = hasClientId(entity.getClientId())
                .and(hasName(entity.getName()))
                .and(hasUnit(entity.getUnit()))
                .and(hasCode(entity.getCode()))
                .and(hasDescription(entity.getDescription()))
                .and(hasUnitType(entity.getUnitType()))
                .and(hasUpdatedAt(entity.getUpdateAt()));
        return Objects.nonNull(ingredient) ? spec.and(hasIngredient(ingredient)) : spec;
    }
}
