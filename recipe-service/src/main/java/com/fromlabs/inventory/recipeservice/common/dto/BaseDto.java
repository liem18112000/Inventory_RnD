package com.fromlabs.inventory.recipeservice.common.dto;

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
    protected String updateAt;
    protected String createAt;
    protected boolean activated = true;
    final protected String accessAt = Instant.now().toString();
}
