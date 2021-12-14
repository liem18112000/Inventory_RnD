/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.template.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h1>Template process cache configuration</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>The configuration provide the default Template Process Cache Manager with the
 * implementation of HasMap</p>
 *
 * <p>A more advance form for thread safe solution, the second bean with be applied with the
 * implementation of the ConcurrentHashMap</p>
 *
 * @see <a href="https://crunchify.com/hashmap-vs-concurrenthashmap-vs-synchronizedmap-how-a-hashmap-can-be-synchronized-in-java/">
 *     HashMap Vs. ConcurrentHashMap Vs. SynchronizedMap : How a HashMap can be Synchronized</a>
 * <h2>Usages</h2>
 *
 * <p>The bean should be selected by using annotation Qualifier with the specific name</p>
 * <ul>
 *     <li>HashMapManagerBean</li>
 *     <li>ConcurrentHashMapManagerBean</li>
 * </ul>
 * <p>If no specifier is provide, the bean of HashMap will be injected</p>
 */
@Configuration
@Slf4j
public class TemplateProcessCacheConfiguration {

    public static final String HASH_MAP_MANAGER                 = "HashMapManagerBean";
    public static final String CONCURRENT_HASH_MAP_MANAGER      = "ConcurrentHashMapManagerBean";

    private final String CUSTOM_CONFIG  = "Template process cache manager - {} is injected";

    @Primary
    @Bean(name = HASH_MAP_MANAGER)
    TemplateProcessCacheManger getHashMapCacheManager() {
        log.info("For template process cache manager not provided. Default manager - {} is injected", HASH_MAP_MANAGER);
        return new TemplateProcessMapCacheMangerImpl(new HashMap<>());
    }

    @Bean(name = CONCURRENT_HASH_MAP_MANAGER)
    TemplateProcessCacheManger getConcurrentHashMapCacheManager() {
        log.info(CUSTOM_CONFIG, CONCURRENT_HASH_MAP_MANAGER);
        return new TemplateProcessMapCacheMangerImpl(new ConcurrentHashMap<>());
    }

}
