package com.fromlabs.inventory.inventoryservice.utility;

import com.fromlabs.inventory.inventoryservice.common.validator.*;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.BatchItemsRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemDeleteAllRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

import static com.fromlabs.inventory.inventoryservice.common.validator.RequestValidator.*;
import static com.fromlabs.inventory.inventoryservice.config.AppConfig.*;

/**
 * <h1>Validation Layer</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     This the second layer in the segment of non-infrastructure service layer.
 *     In detail, it play as validator and filter for valid data on the type and
 *     non-business logic
 * </p>
 *
 * <p>It is a optional layer as there may be some transaction which the validation for data
 * is not necessary or trivial</p>
 *
 * <h2>Component</h2>
 *
 * <p>RequestValidator is the core support of the layer. Developer may enhance the level of filtering
 * by subclass and add more advance logic which is not related to business logic</p>
 *
 * @see RequestValidator
 * <p>Note : the domain constrain logic will be implement in another layer : TransactionConstrain</p>
 *
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
    public RequestValidator validateTenant(
            @NotNull final Long tenantId
    ) {
        log.info("validateTenant");
        return  StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, tenantId)
                .validate();
    }

    /**
     * Validate tenant ID and ingredient id
     * @param tenantId  The client id (must be provided)
     * @param ingredientId Ingredient id
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateTenantAndIngredientId(
            @NotNull final Long tenantId,
            @NotNull final Long ingredientId
    ) {
        log.info("validateTenant");
        return  StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, tenantId)
                .criteriaIsPositiveLong("ingredientId", ingredientId)
                .validate();
    }

    /**
     * Return RequestValidator for the validation of client ID and parent ID are non-negative number
     * @param tenantId  The client id (must be provided)
     * @param id        The ID of a generic object
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateTenantAndId(
            @NotNull final Long tenantId,
            @NotNull final Long id
    ) {
        log.info("validateTenantAndId");
        return  StringRequestValidator()
                .criteriaIsPositiveLong(ID, id)
                .criteriaIsPositiveLong(TENANT_ID, tenantId)
                .validate();
    }

    /**
     * Return RequestValidator for the validation of ID are non-negative number
     * @param id        The ID of a generic object
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateId(
            @NotNull final Long id
    ) {
        log.info("validateId");
        return  StringRequestValidator()
                .criteriaIsPositiveLong(ID, id)
                .validate();
    }

    /**
     * Return RequestValidator for the validation of client ID and parent ID are non-negative number
     * @param tenantId  The client id (must be provided)
     * @param parentId  The ID of a generic parent object
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateTenantAndParentId(
            @NotNull final Long tenantId,
            @NotNull final Long parentId
    ) {
        log.info("validateTenantAndParentId");
        return  StringRequestValidator()
                .criteriaIsPositiveLong("parentId", parentId)
                .criteriaIsPositiveLong(TENANT_ID, tenantId)
                .validate();
    }

    /**
     * Return RequestValidator for the validation of all ingredient filter parameter and
     * client ID and parent ID are non-negative number (based on it is an update request or not)
     * @param request   The request for creating or updating an ingredient
     * @param isUpdate  The boolean flag for whether request is update (true) or create (false)
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateIngredient(
            @NotNull final IngredientRequest request,
            @NotNull final Boolean isUpdate
    ){
        log.info("validateIngredient");
        var validator = StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, request.getClientId())
                .criteriaRequired(NAME, request.getName())
                .criteriaRequired(CODE, request.getCode());
        return isUpdate ? validator.criteriaIsPositiveLong(ID, request.getId()).validate() : validator.validate();
    }

    /**
     * Check constraint for save item
     * @param request ItemRequest
     * @return RequestValidator
     * @see ItemRequest
     * @see RequestValidator
     */
    public RequestValidator validateSaveItem(
            @NotNull final ItemRequest request
    ) {
        log.info("validateSaveItem");
        return  StringRequestValidator().criteriaIsPositiveLong(TENANT_ID, request.getClientId())
                .criteriaIsPositiveLong("ingredientId", request.getIngredientId())
                .criteriaIsPositiveLong("importId", request.getImportId())
                .criteriaRequired("name", request.getName())
                .criteriaRequired("code", request.getCode())
                .criteriaRequired("expiredAt", request.getExpiredAt())
                .criteriaRequired("unit", request.getUnit())
                .criteriaRequired("unitType", request.getUnitType())
                .validate();
    }

    /**
     * Check constraint for save item
     * @param request BatchItemsRequest
     * @return RequestValidator
     */
    public RequestValidator validateSaveItems(
            @NotNull final BatchItemsRequest request
    ) {
        log.info("validateSaveItems");
        return  StringRequestValidator().criteriaIsPositiveLong(TENANT_ID, request.getClientId())
                .criteriaIsPositiveLong("ingredientId", request.getIngredientId())
                .criteriaIsPositiveLong("importId", request.getImportId())
                .criteriaIsPositiveLong("quantity", request.getQuantity())
                .criteriaRequired("expiredAt", request.getExpiredAt())
                .criteriaRequired("unit", request.getUnit())
                .criteriaRequired("unitType", request.getUnitType())
                .criteriaRequired("codes", request.getCodes())
                .validate();
    }

    /**
     * Check constraint for update item
     * @param request ItemRequest
     * @return RequestValidator
     * @see ItemRequest
     * @see RequestValidator
     */
    public RequestValidator validateUpdateItem(
            @NotNull final ItemRequest request
    ) {
        log.info("validateUpdateItem");
        return  StringRequestValidator().criteriaIsPositiveLong(TENANT_ID, request.getClientId())
                .criteriaRequired("name", request.getName())
                .criteriaRequired("code", request.getCode())
                .criteriaRequired("expiredAt", request.getExpiredAt())
                .validate();
    }

    /**
     * Validate inventory request
     * @param request InventoryRequest
     * @return RequestValidator
     * @see RequestValidator
     * @see InventoryRequest
     */
    public RequestValidator validateInventoryRequest(
            @NotNull final InventoryRequest request
    ) {
        log.info("validateInventoryRequest");
        return  StringRequestValidator().criteriaIsPositiveLong(TENANT_ID, request.getClientId())
                .criteriaIsPositiveLong(ID, request.getId()).criteriaRequired("name", request.getName())
                .criteriaRequired("description", request.getDescription()).validate();
    }

    /**
     * Validate delete items request
     * @param request   ItemDeleteAllRequest
     * @return          RequestValidator
     */
    public RequestValidator validateDeleteItems(
            @NotNull final ItemDeleteAllRequest request
    ) {
        return  StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, request.getClientId())
                .criteriaIsPositiveLong("ingredientId", request.getIngredientId())
                .criteriaIsPositiveFloat("quantity", request.getQuantity())
                .criteriaRequired("deleteStrategy", request.getDeleteStrategy())
                .validate();
    }
}
