/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.beans.dto;

import com.fromlabs.inventory.supplierservice.common.dto.BaseDto;
import com.fromlabs.inventory.supplierservice.supplier.beans.dto.SupplierDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImportDto extends BaseDto<Long> {

    protected final String accessAt = Instant.now().toString();

    protected String code;

    protected SupplierDto supplier;

    @Builder(builderMethodName = "importBuilder")
    public ImportDto(Long id, Long tenantId, String name, String description,
                     String code, SupplierDto supplier,
                     String updateAt, String createAt, boolean isActive) {
        super(id, tenantId, name, description, updateAt, createAt, isActive);
        this.supplier = supplier;
        this.code = code;
    }

    public ImportDto() {
    }
}
