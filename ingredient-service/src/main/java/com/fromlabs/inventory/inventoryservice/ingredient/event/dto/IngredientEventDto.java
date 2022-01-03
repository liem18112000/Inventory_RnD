/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.dto.IngredientEventStatusDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientEventDto {
    protected Long   id;
    protected Long   clientId;
    protected String name;
    protected String message;
    protected IngredientEventStatusDto status;
    protected String updateAt;
    protected boolean active;
}
