/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.beans.request;

import com.fromlabs.inventory.supplierservice.common.helper.BaseCustomizePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Import page request
 * @author Liem
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImportPageRequest extends BaseCustomizePageRequest {

    protected Long   tenantId;
    protected Long   supplierId     = -1L;
    protected String name           = "";
    protected String description    = "";
    protected String code           = "";
    protected String createAt       = "";
    protected String updateAt       = "";
}
