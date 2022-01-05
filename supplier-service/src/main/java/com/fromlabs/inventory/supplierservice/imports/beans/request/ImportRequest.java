/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.beans.request;

import lombok.Data;

import java.time.Instant;

@Data
public class ImportRequest {
    protected Long id;
    protected Long tenantId;
    protected Long parentId;
    protected String name;
    protected String description;
    protected String code;
    protected boolean activated = true;
    protected String createdAt = Instant.now().toString();
}
