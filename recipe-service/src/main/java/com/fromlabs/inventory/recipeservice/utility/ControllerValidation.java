package com.fromlabs.inventory.recipeservice.utility;

import com.fromlabs.inventory.recipeservice.common.validator.RequestValidator;
import com.fromlabs.inventory.recipeservice.detail.beans.RecipeDetailPageRequest;
import com.fromlabs.inventory.recipeservice.detail.beans.RecipeDetailRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.RecipePageRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.RecipeRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.fromlabs.inventory.recipeservice.common.validator.RequestValidator.StringRequestValidator;
import static com.fromlabs.inventory.recipeservice.config.AppConfig.*;

/**
 * Controller Validation Layer
 */
@UtilityClass
@Slf4j
public class ControllerValidation {

    /**
     * Validate tenant ID
     * @param tenantId  The client id (must be provided)
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateTenant(Long tenantId) {
        log.info("validateTenant");
        return StringRequestValidator().criteriaIsPositiveLong(TENANT_ID, tenantId).validate();
    }

    /**
     * Validate recipe detail page request
     * @param request   RecipeDetailPageRequest
     * @return          RequestValidator
     */
    public RequestValidator validateGetPageRecipeDetail(
            RecipeDetailPageRequest request
    ) {
        log.info("validateGetPageRecipeDetail");
        return StringRequestValidator().criteriaIsPositiveLong(TENANT_ID, request.getTenantId())
                .criteriaIsPositiveLong(RECIPE_ID, request.getRecipeId()).validate();
    }

    /**
     * Validate tenant ID
     * @param tenantId  The client id (must be provided)
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateGetPageChild(Long tenantId, RecipePageRequest request) {
        log.info("validateGetPageChild");
        return StringRequestValidator().criteriaIsPositiveLong(TENANT_ID, tenantId)
                .criteriaIsPositiveLong(PARENT_ID, request.getParentId()).validate();
    }

    /**
     * Validate ID
     * @param id The id (must be provided)
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateId(Long id) {
        log.info("validateId");
        return StringRequestValidator().criteriaIsPositiveLong(ID, id).validate();
    }

    /**
     * Return RequestValidator for the utility of client ID and parent ID are non-negative number
     * @param tenantId  The client id (must be provided)
     * @param parentId  The ID of a generic parent object
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateTenantAndParentId(
            Long tenantId, Long parentId
    ) {
        log.info("validateTenantAndParentId");
        return StringRequestValidator().criteriaIsPositiveLong(PARENT_ID, parentId).criteriaIsPositiveLong(TENANT_ID, tenantId).validate();
    }

    /**
     * Return RequestValidator for the utility of save and update recipe from recipe request
     * @param request   RecipeRequest
     * @param isUpdate  Boolean flag for update recipe
     * @return          RequestValidator
     */
    public RequestValidator validateRecipeRequest(
            RecipeRequest   request,
            boolean         isUpdate
    ) {
        log.info("validateRecipeRequest: isUpdate-{}", isUpdate);
        var validator = StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, request.getTenantId())
                .criteriaRequired(NAME, request.getName())
                .criteriaRequired(CODE, request.getCode())
                .criteriaRequired(ACTIVATED, request.isActivated());
        return (isUpdate ? validator.criteriaIsPositiveLong(ID, request.getId()) : validator).validate();
    }

    /**
     * Return RequestValidator for the utility of save and update recipe detail from recipe request
     * @param request   RecipeDetailRequest
     * @param isUpdate  Boolean flag for update recipe
     * @return          RequestValidator
     */
    public RequestValidator validateRecipeDetailRequest(
            RecipeDetailRequest request,
            boolean isUpdate
    ) {
        log.info("validateRecipeDetailRequest: isUpdate-{}", isUpdate);
        var validator = StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, request.getTenantId())
                .criteriaRequired(NAME, request.getName())
                .criteriaRequired(CODE, request.getCode())
                .criteriaIsPositiveFloat(QUANTITY, request.getQuantity())
                .criteriaRequired(ACTIVATED, request.isActivated());
        return (isUpdate ?
                validator.criteriaIsPositiveLong(ID,        request.getId()) :
                validator.criteriaIsPositiveLong(RECIPE_ID, request.getRecipeId())
        ).validate();
    }
}
