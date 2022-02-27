package com.fromlabs.inventory.inventoryservice.domains.restaurant.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LowStockDetails implements Serializable {
    private String ingredientCode;
    private String ingredientName;
    private Float minQuantity;
    private Float currentQuantity;
}
