package com.fromlabs.inventory.inventoryservice.client.supplier.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.common.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"active", "children"})
public class SupplierDto  extends BaseDto<Long> {
    private String code;
    public SupplierDto() {
    }
}

