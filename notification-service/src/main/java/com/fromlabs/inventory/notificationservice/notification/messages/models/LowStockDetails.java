package com.fromlabs.inventory.notificationservice.notification.messages.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LowStockDetails implements Serializable {
    private String ingredientCode;
    private String ingredientName;
    private Float minQuantity;
    private Float currentQuantity;
}
