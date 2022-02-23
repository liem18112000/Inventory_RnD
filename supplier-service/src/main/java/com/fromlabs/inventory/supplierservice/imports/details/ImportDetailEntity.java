/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.details;

import com.fromlabs.inventory.supplierservice.entity.ImportReferencedMultiEntity;
import com.fromlabs.inventory.supplierservice.imports.ImportEntity;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportRequest;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="import_detail")
public class ImportDetailEntity extends ImportReferencedMultiEntity {

    @NotNull
    @Column(name="ingredient_id")
    private Long ingredientId;

    @Min(value = 0)
    @Column(name="quantity")
    private Float quantity;

    /**
     * Update entity by request
     * @param request ImportRequest
     * @return ImportEntity
     */
    public ImportDetailEntity update(@NotNull final ImportDetailRequest request) {
        this.setName(request.getName());
        this.setDescription(request.getDescription());
        this.setActive(request.isActive());
        this.setUpdatedAt(Instant.now().toString());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ImportDetailEntity that = (ImportDetailEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
