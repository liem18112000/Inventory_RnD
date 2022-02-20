package com.fromlabs.inventory.inventoryservice.client.supplier.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fromlabs.inventory.inventoryservice.common.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({"active", "accessAt"})
public class ImportDto extends BaseDto<Long> {

    private String code;

    private SupplierDto supplier;

    public ImportDto() {
    }
}
