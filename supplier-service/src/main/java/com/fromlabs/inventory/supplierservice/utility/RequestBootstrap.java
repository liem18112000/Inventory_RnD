package com.fromlabs.inventory.supplierservice.utility;

import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportPageRequest;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportRequest;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailPageRequest;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * <h1>Bootstrap layer</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     This the first layer in the segment of non-infrastructure service layer.
 *     In detail, it play as the point where define the request state
 * </p>
 *
 * <p>
 *     It is a optional layer as there may be some transaction which the boostrap
 *     of request state is not necessary
 * </p>
 *
 * <h2>Usage</h2>
 *
 * <p>
 *     The logic for bootstrapping is as simple as a piece of cake.
 *     It will carry out exactly how developer want the state of request to be
 * </p>
 */
@Slf4j
@UtilityClass
public class RequestBootstrap {

    /**
     * Bootstrap tenant for supplier request
     * @param tenantId  Tenant Id
     * @param request   SupplierRequest
     * @return          Object
     */
    public Object bootstrapTenantId(
            Long            tenantId,
            SupplierRequest request
    ) {
        request.setTenantId(tenantId);
        return logWrapper(request, "bootstrapTenantId: {}");
    }

    /**
     * Bootstrap tenant for supplier request page
     * @param tenantId  Tenant Id
     * @param request   SupplierPageRequest
     * @return          Object
     */
    public Object bootstrapTenantId(
            Long                tenantId,
            SupplierPageRequest request
    ) {
        request.setTenantId(tenantId);
        return logWrapper(request, "bootstrapTenantId: {}");
    }

    /**
     * Bootstrap tenant for material request
     * @param tenantId  Tenant Id
     * @param request   ProvidableMaterialRequest
     * @return          Object
     */
    public Object bootstrapTenantId(
            Long                      tenantId,
            ProvidableMaterialRequest request
    ) {
        request.setTenantId(tenantId);
        return logWrapper(request, "bootstrapTenantId: {}");
    }

    /**
     * Bootstrap tenant for material page request
     * @param tenantId  Tenant Id
     * @param request   ProvidableMaterialPageRequest
     * @return          Object
     */
    public Object bootstrapTenantId(
            Long                            tenantId,
            ProvidableMaterialPageRequest   request
    ) {
        request.setClientId(tenantId);
        return logWrapper(request, "bootstrapTenantId: {}");
    }

    /**
     * Bootstrap tenant for import request
     * @param tenantId  Tenant Id
     * @param request   ImportRequest
     * @return          Object
     */
    public Object bootstrapTenantId(
            Long                tenantId,
            ImportRequest       request
    ) {
        request.setTenantId(tenantId);
        return logWrapper(request, "bootstrapTenantId: {}");
    }

    /**
     * Bootstrap tenant for import request
     * @param tenantId  Tenant Id
     * @param request   ImportDetailRequest
     * @return          Object
     */
    public Object bootstrapTenantId(
            Long                tenantId,
            ImportDetailRequest       request
    ) {
        request.setClientId(tenantId);
        return logWrapper(request, "bootstrapTenantId: {}");
    }

    /**
     * Bootstrap tenant for import page request
     * @param tenantId  Tenant Id
     * @param request   ImportPageRequest
     * @return          Object
     */
    public Object bootstrapTenantId(
            Long                tenantId,
            ImportPageRequest   request
    ) {
        request.setTenantId(tenantId);
        return logWrapper(request, "bootstrapTenantId: {}");
    }

    /**
     * Bootstrap tenant for import detail page request
     * @param tenantId  Tenant Id
     * @param request   ImportDetailPageRequest
     * @return          Object
     */
    public Object bootstrapTenantId(
            Long                    tenantId,
            ImportDetailPageRequest request
    ) {
        request.setClientId(tenantId);
        return logWrapper(request, "bootstrapTenantId: {}");
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
