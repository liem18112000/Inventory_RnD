package com.fromlabs.inventory.notificationservice.utility;

import com.fromlabs.inventory.notificationservice.common.validator.RequestValidator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.fromlabs.inventory.notificationservice.common.validator.RequestValidator.StringRequestValidator;
import static com.fromlabs.inventory.notificationservice.config.AppConfig.*;

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
     * Return RequestValidator for the validation of client ID and parent ID are non-negative number
     * @param tenantId  The client id (must be provided)
     * @param id        The ID of a generic object
     * @return RequestValidator
     * @see RequestValidator
     */
    public RequestValidator validateTenantAndId(Long tenantId, Long id) {
        log.info("validateTenantAndId");
        return StringRequestValidator().criteriaIsPositiveLong(ID, id).criteriaIsPositiveLong(TENANT_ID, tenantId).validate();
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
        return StringRequestValidator().criteriaIsPositiveLong("parentId", parentId).criteriaIsPositiveLong(TENANT_ID, tenantId).validate();
    }
}
