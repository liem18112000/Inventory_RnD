/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.details.beans.request;

import lombok.Data;

/**
 * Import detail request
 * @author Liem
 */
@Data
public class ImportDetailRequest {
    private Long    id;
    private String  name;
    private String  description;
    private boolean active = true;
}
