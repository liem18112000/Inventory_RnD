/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.beans;

import com.fromlabs.inventory.supplierservice.common.dto.BaseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImportDto extends BaseDto<Long> {


    @Builder(builderMethodName = "importBuilder")
    public ImportDto(Long id, Long tenantId, String name, String description, String updateAt, String createAt, boolean isActive) {
        super(id, tenantId, name, description, updateAt, createAt, isActive);
    }

    public ImportDto() {
    }
}
