/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.template.manager;

import com.fromlabs.inventory.inventoryservice.common.template.TemplateProcess;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * <h1>Abstract Map-based template process cache manager</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     The main point of the cache manage is to track the template process build
 *     which are stateful (contain a variable for keeping the process request state).
 * </p>
 * <p>
 *     The manager will apply memory cache with the default support form Map class
 *     and its subclass.
 * </p>
 *
 * @see <a href="https://medium.com/analytics-vidhya/how-to-implement-cache-in-java-d9aa5e9577f2">
 *     How to implement cache in Java</a>
 *
 * <h2>Why template process cache mager</h2>
 * <ul>
 *     <li>It is the optional support for keeping the template process built from the
 *     Template process builders
 *     @see TemplateProcess
 *     </li>
 *     <li>In many case of the process has the same request data state, manager will get the relevant
 *     template process which had been built and cached in previous call. If the request data cached
 *     or the key is not found in cache memory, the cache poeration will be activated</li>
 *     <li>The invention of the manager is the effort to apply a smarter means of managing all
 *     built tempate process in the whole system</li>
 *     <li>If the template process is not used, it is not reccomend to apply the manager</li>
 * </ul>
 *
 * <h2>Usage</h2>
 *
 * <h3>cacheTemplateProcess</h3>
 *
 * <p>
 *     Cache template process referencing existence of provided key.
 *     If process is exist by key do nothing.
 *     If not cache the process with provided key
 * </p>
 *
 * <h3>forceCacheTemplateProcess</h3>
 *
 * <p>
 * Cache template process without knowing provided key exist or not.
 * If key is existed, manager will override the process with the newly-provided
 * If key is not exist, manager cache the process with provided key
 * </p>
 *
 * <h3>getTemplateProcessByKey</h3>
 *
 * <p>
 * Get template process by key.
 * Run template process if key is exist.
 * Otherwise, return null
 * </p>
 */
public abstract class AbstractTemplateProcessMapCacheManger implements TemplateProcessCacheManger, Serializable {

    protected Map<Serializable, TemplateProcess> hashMapCache;

    protected void checkCacheIsNonNull() {
        assert Objects.nonNull(this.hashMapCache);
    }

    protected void checkKeyIsNonNull(Serializable key) {
        assert Objects.nonNull(key);
    }

    protected void checkProcessIsNonNull(TemplateProcess process) {
        assert Objects.nonNull(process);
    }
}
