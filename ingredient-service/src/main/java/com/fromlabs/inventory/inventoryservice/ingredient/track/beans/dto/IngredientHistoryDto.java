/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track.beans.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.common.dto.BaseDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.event.dto.IngredientEventDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"active"})
public class IngredientHistoryDto extends BaseDto<Long> {
    protected String actorName;
    protected String actorRole;
    protected String trackTimestamp;
    protected IngredientDto ingredient;
    protected IngredientEventDto event;
    protected Object extraInformation;

    @Builder
    public IngredientHistoryDto(
            Long id, Long tenantId, String name, String description,
            String updateAt, String createAt, boolean isActive,
            String actorName, String actorRole, String trackTimestamp,
            IngredientEventDto event, IngredientDto ingredient,
            Object extraInformation
    ) {
        super(id, tenantId, name, description, updateAt, createAt, isActive);
        this.actorName          = actorName;
        this.actorRole          = actorRole;
        this.trackTimestamp     = trackTimestamp;
        this.event              = event;
        this.ingredient         = ingredient;
        this.extraInformation   = extraInformation;
    }
}
