package com.fromlabs.inventory.supplierservice.utility;

import com.fromlabs.inventory.supplierservice.common.validator.RequestValidator;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportRequest;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

import static com.fromlabs.inventory.supplierservice.common.validator.RequestValidator.StringRequestValidator;
import static com.fromlabs.inventory.supplierservice.config.AppConfig.*;

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
 * <p>Note : the domain constrain logic will be implement in another layer : TransactionlOGIC</p>
 *
 */
@UtilityClass
@Slf4j
public class ControllerValidation {

    public RequestValidator validateRequired(String name, Object value) {
        log.info("validateRequired - {}: {}", name, value);
        return StringRequestValidator().criteriaRequired(name, value);
    }

    /**
     * Validate ID
     * @param id  The client id (must be provided)
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateId(Long id) {
        log.info("validateTenant");
        return StringRequestValidator().criteriaIsPositiveLong(ID, id).validate();
    }

    /**
     * Validate ID
     * @param tenantId  The client id (must be provided)
     * @param name      The supplier name
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateTenantIdAndName(Long tenantId, String name) {
        log.info("validateTenantIdAndName");
        return StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, tenantId)
                .criteriaRequired(NAME, name)
                .validate();
    }

    /**
     * Validate supplier request
     * @param request   SupplierRequest
     * @param isUpdate  true if request for update
     * @return RequestValidator
     */
    public RequestValidator validateSupplierRequest(SupplierRequest request, boolean isUpdate) {
        log.info("validateSupplierRequest - is update : {}", isUpdate);
        var validator = StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, request.getTenantId())
                .criteriaRequired(NAME, request.getName())
                .criteriaRequired(CODE, request.getCode())
                .criteriaRequired(ACTIVE, request.isActivated());
        return isUpdate ? validator.criteriaIsPositiveLong(ID, request.getId()).validate() : validator;
    }

    /**
     * Validate material request
     * @param request   ProvidableMaterialRequest
     * @param isUpdate  true if request for update
     * @return RequestValidator
     */
    public RequestValidator validateMaterialRequest(
            @NotNull final ProvidableMaterialRequest request, boolean isUpdate) {
        log.info("validateMaterialRequest - is update : {}", isUpdate);
        var validator = StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, request.getTenantId())
                .criteriaRequired(NAME, request.getName())
                .criteriaRequired(ACTIVE, request.isActive());
        return isUpdate ? validator.criteriaIsPositiveLong(ID, request.getId()).validate() : validator;
    }

    public RequestValidator validateImportRequest(
            @NotNull final ImportRequest request, final boolean isUpdate) {
        log.info("validateImport - is update : {}", isUpdate);
        var validator = StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, request.getTenantId())
                .criteriaIsPositiveLong(SUPPLIER_ID, request.getSupplierId())
                .criteriaRequired(NAME, request.getName())
                .criteriaRequired(CODE, request.getCode())
                .criteriaRequired(ACTIVE, request.isActivated());
        return isUpdate ? validator.criteriaIsPositiveLong(ID, request.getId()).validate() : validator;
    }

    public RequestValidator validateImportDetailRequest(
            @NotNull final ImportDetailRequest request, final boolean isUpdate) {
        log.info("validateImportDetail - is update : {}", isUpdate);
        var validator = StringRequestValidator()
                .criteriaIsPositiveLong(TENANT_ID, request.getClientId())
                .criteriaIsPositiveLong(IMPORT_ID, request.getImportId())
                .criteriaIsPositiveLong(INGREDIENT_ID, request.getIngredientId())
                .criteriaIsPositiveFloat(QUANTITY, request.getQuantity())
                .criteriaRequired(NAME, request.getName())
                .criteriaRequired(ACTIVE, request.isActive());
        return isUpdate ? validator.criteriaIsPositiveLong(ID, request.getId()).validate() : validator;
    }

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
     * Return RequestValidator for the validation of client ID and parent ID are non-negative number
     * @param tenantId  The client id (must be provided)
     * @param parentId  The ID of a generic parent object
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateTenantAndParentId(Long tenantId, Long parentId) {
        log.info("validateTenantAndParentId");
        return StringRequestValidator().criteriaIsPositiveLong(PARENT_ID, parentId)
                .criteriaIsPositiveLong(TENANT_ID, tenantId).validate();
    }
}
