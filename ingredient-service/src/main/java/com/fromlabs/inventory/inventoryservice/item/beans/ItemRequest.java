/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.beans;

import lombok.Data;

import javax.persistence.Column;

/**
 * Item request
 */
@Data
public class ItemRequest {
    private Long id;
    private Long clientId;
    private Long ingredientId;
    private Long importId;
    private String name;
    private String code;
    private String description;
    private String unit;
    private String unitType;
    private String expiredAt;
    private String createAt;
    private String updateAt;
}
