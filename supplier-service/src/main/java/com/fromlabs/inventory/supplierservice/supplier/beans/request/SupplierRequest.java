/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.beans;

import lombok.Data;

import java.time.Instant;

/**
 * Supplier request
 */
@Data
public class SupplierRequest {
    protected Long id;
    protected Long tenantId;
    protected Long parentId;
    protected String name;
    protected String description;
    protected String code;
    protected boolean activated = true;
    protected String createdAt = Instant.now().toString();
}
