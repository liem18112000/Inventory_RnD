/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.supplierservice.common.dto.BaseDto;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"active"})
public class SupplierDto  extends BaseDto<Long> {

    protected List<SupplierDto> children;

    @Builder(builderMethodName = "supplierBuilder")
    public SupplierDto(Long id, Long tenantId, String name, String description, String updateAt, String createAt,
                       boolean activated ,List<SupplierDto> children) {
        super(id, tenantId, name, description, updateAt, createAt, activated);
        this.children = children;
    }

    public SupplierDto() {
    }

    /**
     * Convert entity to DTO
     * @param entity    SupplierEntity
     * @return          SupplierDto
     */
    static public SupplierDto from(SupplierEntity entity) {
        var builder = SupplierDto.supplierBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createAt(entity.getUpdateAt())
                .updateAt(entity.getUpdateAt())
                .activated(entity.isActive());

        return Objects.nonNull(entity.getParent()) ? builder.build() :
                builder.children(from(entity.getChildren())).build();
    }

    /**
     * Convert list of entity to list of DTO
     * @param entities  List&lt;SupplierEntity&gt;
     * @return          List&lt;SupplierDto&gt;
     */
    static public List<SupplierDto> from(List<SupplierEntity> entities) {
        return entities.stream().map(SupplierDto::from).collect(Collectors.toList());
    }

    /**
     * Convert page of entity to list of DTO
     * @param entities  Page&lt;SupplierEntity&gt;
     * @return          Page&lt;SupplierEntity&gt;
     */
    static public Page<SupplierDto> from(Page<SupplierEntity> entities) {
        return entities.map(SupplierDto::from);
    }
}
