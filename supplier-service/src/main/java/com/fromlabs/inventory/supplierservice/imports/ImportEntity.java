/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fromlabs.inventory.supplierservice.common.entity.BaseEntityWithCreatedAt;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="import")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ImportEntity extends BaseEntityWithCreatedAt<Long> {

    @Column(name="code")
    @NotNull(message = "Code must not be null")
    @NotBlank(message = "Code must not be blank")
    @NotEmpty(message = "Code must not be empty")
    private String code;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ToString.Exclude
    protected SupplierEntity ingredient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ImportEntity that = (ImportEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
