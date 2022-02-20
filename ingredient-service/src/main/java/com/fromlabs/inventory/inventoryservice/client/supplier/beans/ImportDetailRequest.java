package com.fromlabs.inventory.inventoryservice.client.supplier.beans;

import lombok.Builder;
import lombok.Data;

/**
 * Import detail request
 * @author Liem
 */
@Builder
@Data
public class ImportDetailRequest {
    private Long    id;
    private Long    clientId;
    private Long    importId;
    private Long    ingredientId;
    private String  name;
    private String  description;

    @Builder.Default
    private float   quantity = 1f;

    @Builder.Default
    private boolean active = true;
}
