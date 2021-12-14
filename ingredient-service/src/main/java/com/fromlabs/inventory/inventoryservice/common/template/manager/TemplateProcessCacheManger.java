/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.template.manager;

import com.fromlabs.inventory.inventoryservice.common.template.TemplateProcess;

import java.io.Serializable;

/**
 * <h1>Template process cache manager</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     The main point of the cache manage is to track the template process build
 *     which are stateful (contain a variable for keeping the process request state).
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
public interface TemplateProcessCacheManger {
    /**
     * Cache template process referencing existence of provided key.
     * If process is exist by key do nothing.
     * If not cache the process with provided key
     * @param key               Unique Process Key
     * @param templateProcess   TemplateProcess
     * @return                  TemplateProcessCacheManger
     */
    TemplateProcessCacheManger cacheTemplateProcess(
            Serializable key,
            TemplateProcess templateProcess
    );

    /**
     * Cache template process referencing existence of provided key.
     * If process is exist by key do nothing.
     * If not cache the process with provided key
     * After cache, run process of success or else return null
     * @param key               Unique Process Key
     * @param templateProcess   TemplateProcess
     * @return                  Object
     */
    Object cacheTemplateProcessThenRun(
            Serializable key,
            TemplateProcess templateProcess
    );

    /**
     * Cache template process without knowing provided key exist or not.
     * If key is existed, manager will override the process with the newly-provided
     * If key is not exist, manager cache the process with provided key
     * @param key               Unique Process Key
     * @param templateProcess   TemplateProcess
     * @return                  TemplateProcessCacheManger
     */
    TemplateProcessCacheManger forceCacheTemplateProcess(
            Serializable key,
            TemplateProcess templateProcess
    );

    /**
     * Cache template process without knowing provided key exist or not.
     * If key is existed, manager will override the process with the newly-provided
     * If key is not exist, manager cache the process with provided key
     * After cache, run process of success or else return null
     * @param key               Unique Process Key
     * @param templateProcess   TemplateProcess
     * @return                  Object
     */
    Object forceCacheTemplateProcessThenRun(
            Serializable key,
            TemplateProcess templateProcess
    );

    /**
     * Get template process by key.
     * Return a template if key is exist.
     * Otherwise, return null
     * @param key               Unique Process Key
     * @return                  TemplateProcess
     */
    TemplateProcess getTemplateProcessByKey(Serializable key);

    /**
     * Get template process by key.
     * Run template process if key is exist.
     * Otherwise, return null
     * @param key               Unique Process Key
     * @return                  Object
     */
    Object getTemplateProcessByKeyThenRun(Serializable key);

    /**
     * Check process is exited by key
     * @param key               Unique Process Key
     * @return                  true if provided key exist or else false
     */
    boolean checkProcessKeyExist(Serializable key);

    /**
     * Check process exist by key
     * @param key               Unique Process Key
     * @return                  true if process with provided key is not null or else false
     */
    boolean checkProcessExistByKey(Serializable key);

    /**
     * Traverse to see whether a process value is existed or not
     * @param templateProcess   TemplateProcess
     * @return                  true is found otherwise false
     */
    boolean checkProcessExistByValue(TemplateProcess templateProcess);

    /**
     * Clear template process cache
     */
    void clearCache();
}
