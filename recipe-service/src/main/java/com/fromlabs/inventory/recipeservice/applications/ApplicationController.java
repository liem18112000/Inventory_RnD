package com.fromlabs.inventory.recipeservice.applications;

import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipePageRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipeRequest;
import org.springframework.http.ResponseEntity;

public interface ApplicationController {

    //<editor-fold desc="RECIPE">

    //<editor-fold desc="GROUP">

    /**
     * Get all recipe group as list
     * @param tenantId  Tenant ID
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllGroup(
            Long tenantId
    );

    /**
     * Get all recipe group with pagination
     * @param tenantId  Tenant ID
     * @param request   RecipePageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getGroupPage(
            Long tenantId,
            RecipePageRequest request
    );

    //</editor-fold>

    //<editor-fold desc="CHILD">

    /**
     * Get all recipe child as list
     * @param tenantId  Tenant Id
     * @param parentId  Parent Id
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllChild(
            Long tenantId,
            Long parentId
    );

    /**
     * Get recipe child with pagination
     * @param tenantId  Tenant ID
     * @param request   RecipePageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getChildPage(
            Long tenantId,
            RecipePageRequest request
    );

    //</editor-fold>

    //<editor-fold desc="GENERAL">

    /**
     * Get recipe child and group by id
     * @param id    Entity ID
     * @return      ResponseEntity
     */
    ResponseEntity<?> getById(
            Long id
    );

    /**
     * Get recipe group and child by code
     * @param code  Recipe code
     * @return      ResponseEntity
     */
    ResponseEntity<?> getByCode(
            String code
    );

    /**
     * Save recipe group or child by request
     * @param tenantId  Tenant ID
     * @param request   RecipeRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> save(
            Long tenantId,
            RecipeRequest request
    );

    /**
     * Update recipe by request
     * @param tenantId  Tenant ID
     * @param request   Recipe Request
     * @return          ResponseEntity
     */
    ResponseEntity<?> update(
            Long tenantId,
            RecipeRequest request
    );

    //</editor-fold>

    //</editor-fold>
}
