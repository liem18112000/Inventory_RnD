/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.specification;

import com.fromlabs.inventory.supplierservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import static com.fromlabs.inventory.supplierservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.supplierservice.common.specifications.SearchCriteria.*;
import static java.util.Objects.isNull;

/**
 * Specification for SupplierEntity
 */
public class SupplierSpecification {

    /**
     * Filter for has client id
     * @param clientId  Client ID
     * @return          BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    /**
     * Filter for has parent id
     * @param parent    Parent Supplier entity
     * @return          BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasParent(SupplierEntity parent) {
        return Spec(criteriaEqual("parent", parent));
    }

    /**
     * Filter for has code
     * @param code      Supplier code
     * @return          BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasCode(String code) {
        return Spec(criteriaEqual("code", code.trim()));
    }

    /**
     * Filter for name
     * @param name      Supplier name
     * @return          BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name.trim()));
    }

    /**
     * Filter for has description
     * @param description Supplier description
     * @return            BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description.trim()));
    }

    /**
     * Filter for has category flag
     * @param group     is group boolean flag
     * @return          BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasGroup(boolean group) {
        return Spec(criteriaEqual("group", group));
    }

    /**
     * Filter for has created at
     * @param createdAt create timestamp
     * @return          BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasCreateAt(String createdAt) {
        return Spec(criteriaEqual("createdAt", createdAt));
    }

    /**
     *
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasUpdatedAt(String updateAt) {
        return Spec(criteriaEqual("updatedAt", updateAt));
    }

    /**
     * Filter for has created from
     * @param createdAt create timestamp
     * @return          BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasCreateFrom(String createdAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("createdAt", createdAt));
    }

    /**
     * Filter for has updated from
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;SupplierEntity&gt;
     */
    public static BaseSpecification<SupplierEntity> hasUpdatedFrom(String updateAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("updatedAt", updateAt));
    }

    /**
     * Filter for all supplier
     * @param entity    SupplierEntity
     * @return          Specification&lt;SupplierEntity&gt;
     */
    public static Specification<SupplierEntity> filter(SupplierEntity entity, SupplierEntity parent) {
        var spec = hasClientId(entity.getClientId())
                .and(hasName(entity.getName()))
                .and(hasCode(entity.getCode()))
                .and(hasDescription(entity.getDescription()))
                .and(hasGroup(entity.isGroup()))
                .and(StringUtils.hasText(entity.getCreatedAt()) ?
                        hasCreateFrom(entity.getCreatedAt()) :
                        hasCreateAt(entity.getCreatedAt()))
                .and(StringUtils.hasText(entity.getUpdatedAt()) ?
                        hasUpdatedFrom(entity.getUpdatedAt()) :
                        hasUpdatedAt(entity.getUpdatedAt()));
        return isNull(parent) ? spec : spec.and(hasParent(parent));
    }
}
