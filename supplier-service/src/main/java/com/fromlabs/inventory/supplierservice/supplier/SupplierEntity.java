/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fromlabs.inventory.supplierservice.entity.SupplierBaseEntity;
import com.fromlabs.inventory.supplierservice.supplier.beans.SupplierPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.SupplierRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.NumberUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType.*;
import static com.fromlabs.inventory.supplierservice.supplier.factory.SupplierEntityFactory.create;
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
     * Convert request to entity
     * @param request   SupplierRequest
     * @return          SupplierEntity
     */
    public static SupplierEntity from(SupplierRequest request) {
        var entity = create(DEFAULT);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setGroup(Objects.isNull(request.getParentId()));
        entity.setCreateAt(request.getCreatedAt());
        return entity;
    }

    /**
     * Convert request to entity
     * @param request   SupplierRequest
     * @return          SupplierEntity
     */
    public static SupplierEntity from(SupplierPageRequest request) {
        var entity = create(EMPTY);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setGroup(Objects.isNull(request.getParentId()) || request.getParentId() <= 0);
        entity.setCreateAt(request.getCreateAt());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    /**
     * Update information for an existing entity
     * @param request   SupplierRequest
     * @param entity    SupplierEntity
     * @return          SupplierEntity
     */
    public static SupplierEntity update(SupplierRequest request, SupplierEntity entity) {
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setUpdateAt(request.getCreatedAt());
        return entity;
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
