package com.fromlabs.inventory.recipeservice.detail.beans;

import lombok.Data;

import java.time.Instant;

@Data
public class RecipeDetailRequest {
    protected Long id;
    protected Long tenantId;
    protected Long recipeId;
    protected Long ingredientId;
    protected String name;
    protected String description;
    protected String code;
    protected Float quantity = 1f;
    protected boolean activated = true;
    protected String updateAt = Instant.now().toString();
}
