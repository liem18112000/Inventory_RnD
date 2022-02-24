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
    private Long    clientId;
    private Long    importId;
    private Long    ingredientId;
    private String  name;
    private String  description;
    private float   quantity = 0f;
    private boolean active = true;
}
