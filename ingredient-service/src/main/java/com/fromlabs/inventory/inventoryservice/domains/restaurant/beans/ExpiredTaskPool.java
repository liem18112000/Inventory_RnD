/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.beans;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@UtilityClass
public class ExpiredTaskPool {

    private ConcurrentHashMap<Object, Object> pools = new ConcurrentHashMap<>();

    public Object get(Object key) {
        if(!pools.containsKey(key)) return null;
        return pools.get(key);
    }

    public void set(Object key, Object value) {
        if(pools.containsKey(key)) {
            pools.replace(key, value);
        }
        pools.put(key, value);
    }

    public long getSize() {
        return pools.size();
    }

    public void refresh() {
        pools = new ConcurrentHashMap<>();
    }
}
