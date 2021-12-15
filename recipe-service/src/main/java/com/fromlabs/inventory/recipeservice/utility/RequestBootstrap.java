package com.fromlabs.inventory.recipeservice.utility;

import com.fromlabs.inventory.recipeservice.detail.beans.RecipeDetailPageRequest;
import com.fromlabs.inventory.recipeservice.detail.beans.RecipeDetailRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.RecipePageRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.RecipeRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class RequestBootstrap {

    /**
     * Page bootstrap for get all recipe group and child
     * @param tenantId  Client ID
     * @param request   RecipePageRequest
     * @return Object
     */
    public Object pageBootstrap(Long tenantId, RecipePageRequest request) {
        request.setTenantId(tenantId);
        return logWrapper(request, "pageBootstrap: {}");
    }

    public Object bootstrapGetAllRecipeChildPage(Long tenantId, RecipePageRequest request) {
        request.setTenantId(tenantId);
        request.setGroup(false);
        return logWrapper(request, "pageBootstrap: {}");
    }

    /**
     * Page bootstrap for get all recipe group and child
     * @param tenantId  Client ID
     * @param request   RecipePageRequest
     * @return Object
     */
    public Object pageBootstrap(Long tenantId, RecipeDetailPageRequest request) {
        request.setTenantId(tenantId);
        return logWrapper(request, "pageBootstrap: {}");
    }

    /**
     * Boostrap recipe request for save recipe group and child
     * @param tenantId  Client ID
     * @param request   RecipeRequest
     * @return          Object
     */
    public Object recipeRequestBootstrap(Long tenantId, RecipeRequest request) {
        request.setTenantId(tenantId);
        return logWrapper(request, "recipeRequestBootstrap: {}");
    }

    /**
     * Boostrap recipe detail request for save recipe group and child
     * @param tenantId  Client ID
     * @param request   RecipeDetailRequest
     * @return          Object
     */
    public Object recipeDetailRequestBootstrap(Long tenantId, RecipeDetailRequest request) {
        request.setTenantId(tenantId);
        return logWrapper(request, "recipeDetailRequestBootstrap: {}");
    }

    /**
     * Log wrapper
     * @param result    Object
     * @param message   Log message
     * @return  boolean
     */
    private Object logWrapper(Object result, String message) {
        log.info(message, result);
        return result;
    }
}
