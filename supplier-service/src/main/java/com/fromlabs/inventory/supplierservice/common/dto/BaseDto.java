package com.fromlabs.inventory.supplierservice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto<ID extends Serializable> {
    protected ID id;
    private Long tenantId;
    protected String name;
    protected String description;
    protected String updatedAt;
    protected String createdAt;
    protected boolean isActive = true;
    final protected String accessAt = Instant.now().toString();

}
