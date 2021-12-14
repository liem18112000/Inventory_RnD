/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory.beans;

import lombok.Data;

import java.time.Instant;

/**
 * Request bean of inventory request
 */
@Data
public class InventoryRequest {
    private Long id;
    private Long clientId;
    private String name;
    private String description;
    private String updateAt = Instant.now().toString();
}
