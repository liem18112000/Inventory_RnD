/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fromlabs.inventory.supplierservice.entity.SupplierBaseEntity;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity
@Table(name="supplier")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SupplierEntity extends SupplierBaseEntity {

    @Column(name="code")
    @NotNull(message = "Code must not be null")
    @NotBlank(message = "Code must not be blank")
    @NotEmpty(message = "Code must not be empty")
    private String code;

    @Column(name="is_group")
    @NotNull
    private boolean group;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private SupplierEntity parent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<SupplierEntity> children = new ArrayList<>();

    /**
     * Update information for an existing entity
     * @param request   SupplierRequest
     * @return          SupplierEntity
     */
    public SupplierEntity update(SupplierRequest request) {
        this.setName(request.getName());
        this.setCode(request.getCode());
        this.setDescription(request.getDescription());
        this.setUpdatedAt(Instant.now().toString());
        return this;
    }

    /**
     * Set parent
     * @param parent    SupplierEntity
     * @return          SupplierEntity
     */
    public SupplierEntity setParent(SupplierEntity parent) {
        if(nonNull(parent)) this.parent = parent;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SupplierEntity that = (SupplierEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
