/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.beans.dto;

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
}
