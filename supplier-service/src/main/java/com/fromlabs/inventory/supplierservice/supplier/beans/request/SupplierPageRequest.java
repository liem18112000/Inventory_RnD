/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.beans.request;

import com.fromlabs.inventory.supplierservice.common.helper.BaseCustomizePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Page request of supplier
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierPageRequest extends BaseCustomizePageRequest {
    protected Long   tenantId;
    protected Long   parentId       = -1L;
    protected String name           = "";
    protected String description    = "";
    protected String code           = "";
    protected String createAt       = "";
    protected String updateAt       = "";
}
