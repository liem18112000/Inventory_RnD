/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.beans.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

/**
 * Request for adding batch of item
 * @author Liem
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchItemsRequest extends ItemRequest {
    protected int quantity          = 0;
    protected Set<String> codes     = new HashSet<>();
}
