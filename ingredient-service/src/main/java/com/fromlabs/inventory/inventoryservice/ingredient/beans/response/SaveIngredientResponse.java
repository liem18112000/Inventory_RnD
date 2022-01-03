/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.beans.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import lombok.Builder;
import lombok.Data;

/**
 * Response class for save the aggregation of ingredient type and config
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
@Data
public class SaveIngredientResponse {
    private IngredientDto ingredient;
    private IngredientConfigEntity config;
}
