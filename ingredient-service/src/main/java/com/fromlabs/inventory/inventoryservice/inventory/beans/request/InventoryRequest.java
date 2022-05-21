/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory.beans.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

/**
 * Request bean of inventory request
 */
@Data
@AllArgsConstructor
public class InventoryRequest {
    private Long id;
    private Long clientId;
    private String name;
    private String description;
    private final String updateAt = Instant.now().toString();
}
