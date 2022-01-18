/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.details.beans.request;

import com.fromlabs.inventory.supplierservice.common.helper.BaseCustomizePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Import detail page request
 * @author Liem
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImportDetailPageRequest extends BaseCustomizePageRequest {
    private Long    clientId;
    private Long    importId        = -1L;
    private Long    ingredientId;
    private String  name            = "";
    private String  description     = "";
    private String  updateAt        = "";
    private String  createAt        = "";
}
