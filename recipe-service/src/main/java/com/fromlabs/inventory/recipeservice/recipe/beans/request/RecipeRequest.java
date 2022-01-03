package com.fromlabs.inventory.recipeservice.recipe.beans.request;

import lombok.Data;

import java.time.Instant;

@Data
public class RecipeRequest {
    protected Long id;
    protected Long tenantId;
    protected Long parentId;
    protected String name;
    protected String description;
    protected String code;
    protected boolean activated = true;
    protected String createdAt = Instant.now().toString();
}
