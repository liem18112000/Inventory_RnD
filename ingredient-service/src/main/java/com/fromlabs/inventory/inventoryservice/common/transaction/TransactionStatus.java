/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.transaction;

public enum TransactionStatus {
    INITIAL, PROCESSING, COMMITTING, SUCCESS, FAILED, VIOLATED
}
