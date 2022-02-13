/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material.specification;

import com.fromlabs.inventory.supplierservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialEntity;
import org.springframework.data.jpa.domain.Specification;

import static com.fromlabs.inventory.supplierservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.supplierservice.common.specifications.SearchCriteria.criteriaEqual;
import static java.util.Objects.isNull;

public class ProvidableMaterialSpecification {

    /**
     * Filter for has client id
     * @param clientId  Client ID
     * @return          BaseSpecification&lt;ProvidableMaterialEntity&gt;
     */
    public static BaseSpecification<ProvidableMaterialEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    /**
     * Filter for has parent id
     * @param supplier  Supplier entity
     * @return          BaseSpecification&lt;ProvidableMaterialEntity&gt;
     */
    public static BaseSpecification<ProvidableMaterialEntity> hasSuppler(SupplierEntity supplier) {
        return Spec(criteriaEqual("supplier", supplier));
    }

    /**
     * Filter for has name
     * @param name      ingredient name
     * @return          BaseSpecification&lt;ProvidableMaterialEntity&gt;
     */
    public static BaseSpecification<ProvidableMaterialEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name.trim()));
    }

    /**
     * Filter for has description
     * @param description ProvidableMaterialEntity description
     * @return            BaseSpecification&lt;ProvidableMaterialEntity&gt;
     */
    public static BaseSpecification<ProvidableMaterialEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description.trim()));
    }

    /**
     *
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;ProvidableMaterialEntity&gt;
     */
    public static BaseSpecification<ProvidableMaterialEntity> hasUpdatedAt(String updateAt) {
        return Spec(criteriaEqual("updatedAt", updateAt));
    }

    /**
     * Filter by ingredient ID
     * @param ingredientId  update timestamp
     * @return              BaseSpecification&lt;ProvidableMaterialEntity&gt;
     */
    public static BaseSpecification<ProvidableMaterialEntity> hasIngredientId(Long ingredientId) {
        return Spec(criteriaEqual("ingredientId", ingredientId));
    }

    /**
     * Filter for all ingredient
     * @param entity    SupplierEntity
     * @return          Specification&lt;SupplierEntity&gt;
     */
    public static Specification<ProvidableMaterialEntity> filter(ProvidableMaterialEntity entity, SupplierEntity supplier) {
        var spec = hasClientId(entity.getClientId())
                .and(hasName(entity.getName()))
                .and(hasDescription(entity.getDescription()))
                .and(hasUpdatedAt(entity.getUpdatedAt()));
        spec = isNull(entity.getIngredientId()) ? spec : spec.and(hasIngredientId(entity.getIngredientId()));
        return isNull(supplier) ? spec : spec.and(hasSuppler(supplier));
    }
}
