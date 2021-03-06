package com.fromlabs.inventory.inventoryservice.common.dto;

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
    private Long clientId;
    protected String name;
    protected String description;
    protected String updateAt;
    protected String createAt;
    protected boolean isActive = true;
    final protected String accessAt = Instant.now().toString();

}
