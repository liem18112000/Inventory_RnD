/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.beans;

import com.fromlabs.inventory.inventoryservice.item.strategy.DeleteStrategy;
import lombok.Data;

import java.util.List;

/**
 * Item delete all request
 */
@Data
public class ItemDeleteAllRequest {
    private Long clientId;
    private Long ingredientId;
    private long quantity = 1;
    private DeleteStrategy deleteStrategy = DeleteStrategy.ByOldest;
}
