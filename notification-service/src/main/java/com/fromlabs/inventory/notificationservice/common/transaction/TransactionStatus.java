/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.transaction;

public enum TransactionStatus {
    INITIAL, PROCESSING, COMMITTING, SUCCESS, FAILED, VIOLATED
}
