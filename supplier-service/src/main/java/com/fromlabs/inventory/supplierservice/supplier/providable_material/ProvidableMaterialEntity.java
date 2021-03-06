/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material;

import com.fromlabs.inventory.supplierservice.entity.SupplierReferencedMultiEntity;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.ProvidableMaterialPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.ProvidableMaterialRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

import static com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType.*;
import static com.fromlabs.inventory.supplierservice.supplier.providable_material.factory.ProvidableMaterialEntityFactory.create;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="providable_material")
public class ProvidableMaterialEntity extends SupplierReferencedMultiEntity<Long> {

    @NotNull
    @Column(name="ingredient_id")
    private Long ingredientId;

    @Max(value = 32767)
    @Min(value = 0)
    @Column(name="minimum_quantity")
    private float minimumQuantity = 1f;

    @Min(value = 1)
    @Column(name="maximum_quantity")
    private float maximumQuantity = 32767F;

    /**
     * Covert request to entity
     * @param request   ProvidableMaterialRequest
     * @return          ProvidableMaterialEntity
     */
    public static ProvidableMaterialEntity from(
            ProvidableMaterialRequest request
    ) {
        var entity = create(DEFAULT);
        entity.setIngredientId(request.getIngredientId());
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setMinimumQuantity(request.getMinimumQuantity());
        entity.setMaximumQuantity(request.getMaximumQuantity());
        entity.setActive(request.isActive());
        return entity;
    }

    /**
     * Covert request to entity
     * @param request   ProvidableMaterialRequest
     * @return          ProvidableMaterialEntity
     */
    public static ProvidableMaterialEntity from(
            ProvidableMaterialPageRequest   request,
            SupplierEntity                  supplier
    ) {
        var entity = create(EMPTY);
        entity.setSupplier(supplier);
        entity.setIngredientId(request.getIngredientId());
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    /**
     * Update information from request
     * @param entity    ProvidableMaterialEntity
     * @param request   ProvidableMaterialRequest
     * @return          ProvidableMaterialEntity
     */
    public static ProvidableMaterialEntity update(
            ProvidableMaterialEntity    entity,
            ProvidableMaterialRequest   request
    ) {
        entity.setDescription(request.getDescription());
        entity.setName(request.getName());
        entity.setMinimumQuantity(request.getMinimumQuantity());
        entity.setMaximumQuantity(request.getMaximumQuantity());
        entity.setActive(request.isActive());
        entity.setUpdateAt(Instant.now().toString());
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProvidableMaterialEntity that = (ProvidableMaterialEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
