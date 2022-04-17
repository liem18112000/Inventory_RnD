/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.details.specification;

import com.fromlabs.inventory.supplierservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.supplierservice.imports.ImportEntity;
import com.fromlabs.inventory.supplierservice.imports.details.ImportDetailEntity;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailPageRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;

import static com.fromlabs.inventory.supplierservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.supplierservice.common.specifications.SearchCriteria.criteriaEqual;
import static com.fromlabs.inventory.supplierservice.common.specifications.SearchCriteria.criteriaTimestampGreaterThanOrEqual;
import static java.util.Objects.isNull;

/**
 * Import detail specification
 * @author Liem
 */
@UtilityClass
public class ImportDetailSpecification {

    /**
     * Filter for has client id
     * @param clientId  Client ID
     * @return          BaseSpecification&lt;ImportDetailEntity&gt;
     */
    public static BaseSpecification<ImportDetailEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    /**
     * Filter for import entity
     * @param importEntity ImportEntity
     * @return   BaseSpecification&lt;ImportDetailEntity&gt;
     */
    public static BaseSpecification<ImportDetailEntity> hasImport(ImportEntity importEntity) {
        return Spec(criteriaEqual("importEntity", importEntity));
    }

    /**
     * Filter for has name
     * @param name      ingredient name
     * @return          BaseSpecification&lt;ProvidableMaterialEntity&gt;
     */
    public static BaseSpecification<ImportDetailEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name.trim()));
    }

    /**
     * Filter for has description
     * @param description ProvidableMaterialEntity description
     * @return            BaseSpecification&lt;ImportDetailEntity&gt;
     */
    public static BaseSpecification<ImportDetailEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description.trim()));
    }

    /**
     * Filer for updated timestamp
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;ImportDetailEntity&gt;
     */
    public static BaseSpecification<ImportDetailEntity> hasUpdatedAt(String updateAt) {
        return Spec(criteriaEqual("updatedAt", updateAt));
    }

    /**
     * Filer for created timestamp
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;ImportDetailEntity&gt;
     */
    public static BaseSpecification<ImportDetailEntity> hasCreatedAt(String updateAt) {
        return Spec(criteriaEqual("createdAt", updateAt));
    }

    /**
     * Filter for has created from
     * @param createdAt create timestamp
     * @return          BaseSpecification&lt;ImportDetailEntity&gt;
     */
    public static BaseSpecification<ImportDetailEntity> hasCreatedFrom(String createdAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("createdAt", createdAt));
    }

    /**
     * Filter for has updated from
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;ImportDetailEntity&gt;
     */
    public static BaseSpecification<ImportDetailEntity> hasUpdatedFrom(String updateAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("updatedAt", updateAt));
    }

    /**
     *
     * @param ingredientId  ingredient id
     * @return              BaseSpecification&lt;ImportDetailEntity&gt;
     */
    public static BaseSpecification<ImportDetailEntity> hasIngredientId(Long ingredientId) {
        return Spec(criteriaEqual("ingredientId", ingredientId));
    }

    /**
     * Filter for all ingredient
     * @param request           SupplierEntity
     * @param importEntity      ImportEntity
     * @return          Specification&lt;SupplierEntity&gt;
     */
    public static Specification<ImportDetailEntity> filter(
            @NotNull final ImportDetailPageRequest request, ImportEntity importEntity) {
        var spec = hasClientId(request.getClientId())
                .and(hasName(request.getName()))
                .and(hasDescription(request.getDescription()))
                .and(StringUtils.hasText(request.getCreateAt()) ?
                        hasCreatedFrom(request.getCreateAt()) :
                        hasCreatedAt(request.getCreateAt()))
                .and(StringUtils.hasText(request.getUpdateAt()) ?
                        hasUpdatedFrom(request.getUpdateAt()) :
                        hasUpdatedAt(request.getUpdateAt()));
        spec = isNull(request.getIngredientId()) ? spec : spec.and(hasIngredientId(request.getIngredientId()));
        return isNull(importEntity) ? spec : spec.and(hasImport(importEntity));
    }
}
