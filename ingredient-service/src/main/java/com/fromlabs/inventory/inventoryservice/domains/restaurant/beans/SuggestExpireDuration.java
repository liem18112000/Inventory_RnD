/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.beans;

import java.time.LocalDateTime;

public class SuggestExpireDuration {
    public static final LocalDateTime SHORT_EXPIRATION  = LocalDateTime.now().plusMinutes(5L);
    public static final LocalDateTime MEDIUM_EXPIRATION = LocalDateTime.now().plusMinutes(15L);
    public static final LocalDateTime LONG_EXPIRATION   = LocalDateTime.now().plusMinutes(60L);
}
