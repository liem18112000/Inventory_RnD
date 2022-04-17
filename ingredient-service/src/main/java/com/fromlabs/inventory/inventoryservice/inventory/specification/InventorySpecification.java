/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory.specification;

import com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.inventoryservice.common.specifications.SearchCriteria.*;

/**
 * Inventory specification for filter
 */
public class InventorySpecification {

    public static BaseSpecification<InventoryEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    public static BaseSpecification<InventoryEntity> hasIngredient(IngredientEntity ingredient) {
        return Spec(criteriaEqual("ingredient", ingredient));
    }

    public static BaseSpecification<InventoryEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name));
    }

    public static BaseSpecification<InventoryEntity> hasUnit(String unit) {
        return Spec(criteriaStrictlyEqual("unit", unit, true));
    }

    public static BaseSpecification<InventoryEntity> hasUnitType(String unitType) {
        return Spec(criteriaStrictlyEqual("unitType", unitType, true));
    }

    public static BaseSpecification<InventoryEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description));
    }

    public static BaseSpecification<InventoryEntity> hasQuantity(Float quantity) {
        return Spec(criteriaGreaterThanOrEqual("quantity", quantity));
    }

    public static BaseSpecification<InventoryEntity> hasReserved(Float quantity) {
        return Spec(criteriaGreaterThanOrEqual("reserved", quantity));
    }

    /**
     * Filter for has updated at
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;InventoryEntity&gt;
     */
    public static BaseSpecification<InventoryEntity> hasUpdatedAt(String updateAt) {
        return Spec(criteriaEqual("updateAt", updateAt));
    }

    public static BaseSpecification<InventoryEntity> hasUpdateFrom(String updateAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("updateAt", updateAt));
    }

    public static Specification<InventoryEntity> filter(InventoryEntity entity, IngredientEntity ingredient) {
        var spec = hasClientId(entity.getClientId())
                .and(hasName(entity.getName()))
                .and(hasUnit(entity.getUnit()))
                .and(hasDescription(entity.getDescription()))
                .and(hasUnitType(entity.getUnitType()))
                .and(hasQuantity(entity.getQuantity()))
                .and(hasReserved(entity.getReserved()))
                .and(StringUtils.hasText(entity.getUpdateAt()) ?
                        hasUpdateFrom(entity.getUpdateAt()) :
                        hasUpdatedAt(entity.getUpdateAt()));
        return Objects.nonNull(ingredient) ? spec.and(hasIngredient(ingredient)) : spec;
    }
}
