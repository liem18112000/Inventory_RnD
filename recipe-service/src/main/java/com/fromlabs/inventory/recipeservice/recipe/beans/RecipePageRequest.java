package com.fromlabs.inventory.recipeservice.recipe.beans;

import com.fromlabs.inventory.recipeservice.common.helper.BaseCustomizePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipePageRequest extends BaseCustomizePageRequest {
    protected Long      tenantId;
    protected Long      parentId = null;
    protected String    name = "";
    protected String    description = "";
    protected String    code = "";
    protected Boolean   group;
}
