/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material;

import com.fromlabs.inventory.supplierservice.entity.SupplierReferencedMultiEntity;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialRequest;
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
     * Update information from request
     * @param request   ProvidableMaterialRequest
     * @return          ProvidableMaterialEntity
     */
    public ProvidableMaterialEntity update(@NotNull final ProvidableMaterialRequest request) {
        this.setDescription(request.getDescription());
        this.setName(request.getName());
        this.setIngredientId(request.getIngredientId());
        this.setMinimumQuantity(request.getMinimumQuantity());
        this.setMaximumQuantity(request.getMaximumQuantity());
        this.setActive(request.isActive());
        this.setUpdatedAt(Instant.now().toString());
        return this;
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
