/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.entity;

import com.fromlabs.inventory.supplierservice.common.entity.BaseEntityWithCreatedAt;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ImportBaseEntity extends SupplierReferencedMultiEntity<Long> {

    @NotNull(message = "Import timestamp must not be null")
    @NotBlank(message = "Import timestamp must not be blank")
    @NotEmpty(message = "Import timestamp must not be empty")
    @Column(name="importAt")
    protected String importAt;

    @NotNull(message = "Code must not be null")
    @NotBlank(message = "Code must not be blank")
    @NotEmpty(message = "Code must not be empty")
    @Column(name="code")
    protected String code;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ImportBaseEntity that = (ImportBaseEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
