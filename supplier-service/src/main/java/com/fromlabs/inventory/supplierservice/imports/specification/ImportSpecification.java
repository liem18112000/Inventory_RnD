/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.specification;

import com.fromlabs.inventory.supplierservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.supplierservice.imports.ImportEntity;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.constraints.NotNull;

import java.util.Objects;

import static com.fromlabs.inventory.supplierservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.supplierservice.common.specifications.SearchCriteria.criteriaEqual;

/**
 * Import entity specification
 * @author Liem
 */
public class ImportSpecification {

    /**
     * Filter for has client id
     * @param clientId  Client ID
     * @return          BaseSpecification&lt;ImportEntity&gt;
     */
    public static BaseSpecification<ImportEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    /**
     * Filter for has parent id
     * @param supplier  Supplier entity
     * @return          BaseSpecification&lt;ImportEntity&gt;
     */
    public static BaseSpecification<ImportEntity> hasSupplier(SupplierEntity supplier) {
        return Spec(criteriaEqual("supplier", supplier));
    }

    /**
     * Filter for has code
     * @param code      Supplier code
     * @return          BaseSpecification&lt;ImportEntity&gt;
     */
    public static BaseSpecification<ImportEntity> hasCode(String code) {
        return Spec(criteriaEqual("code", code.trim()));
    }

    /**
     * Filter for name
     * @param name      Supplier name
     * @return          BaseSpecification&lt;ImportEntity&gt;
     */
    public static BaseSpecification<ImportEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name.trim()));
    }

    /**
     * Filter for has description
     * @param description Supplier description
     * @return            BaseSpecification&lt;ImportEntity&gt;
     */
    public static BaseSpecification<ImportEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description.trim()));
    }

    /**
     * Filter for has created at
     * @param createdAt create timestamp
     * @return          BaseSpecification&lt;ImportEntity&gt;
     */
    public static BaseSpecification<ImportEntity> hasCreateAt(String createdAt) {
        return Spec(criteriaEqual("createdAt", createdAt));
    }

    /**
     *
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;ImportEntity&gt;
     */
    public static BaseSpecification<ImportEntity> hasUpdatedAt(String updateAt) {
        return Spec(criteriaEqual("updatedAt", updateAt));
    }

    /**
     * Filter for all import entity
     * @param entity    ImportEntity
     * @return          Specification&lt;ImportEntity&gt;
     */
    public static Specification<ImportEntity> filter(
            @NotNull final ImportEntity entity, final SupplierEntity supplier) {
        var spec = hasClientId(entity.getClientId())
                .and(hasName(entity.getName()))
                .and(hasCode(entity.getCode()))
                .and(hasDescription(entity.getDescription()))
                .and(hasCreateAt(entity.getCreatedAt()))
                .and(hasUpdatedAt(entity.getUpdatedAt()));
        return Objects.nonNull(supplier) ? spec.and(hasSupplier(supplier)) : spec;
    }

}
