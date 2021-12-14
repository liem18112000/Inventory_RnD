/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.template.manager;

import com.fromlabs.inventory.inventoryservice.common.template.TemplateProcess;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
 *
 * <h2>Exaple</h2>
 *
 * <h3>Example 1</h3>
 *
 * <pre>
 *     log.info(path(HttpMethod.POST, "category/page"));
 *     final var key = generateProcessKey(PROCESS_KEY_GET_PAGE_INGREDIENT_CATE, tenantId);
 *     return (ResponseEntity) (isProcessCanBeGetFromCache(setTenantBoostrap(tenantId, request), key, processCache) ?
 *                 processCache.getTemplateProcessByKeyThenRun(key) : processCache.forceCacheTemplateProcessThenRun(key,
 *                 buildGetPageIngredientCategoryTemplateProcess(tenantId, request, ingredientService, inventoryService)));
 * </pre>
 *
 * <h3>Example 2</h3>
 *
 * <pre>
 *     log.info(path(HttpMethod.POST, "type/page"));
 *     final var key = generateProcessKey(PROCESS_KEY_GET_PAGE_INGREDIENT_TYPE, tenantId);
 *     return (ResponseEntity) (isProcessCanBeGetFromCache(setTenantBoostrap(tenantId, request), key, processCache) ?
 *                 processCache.getTemplateProcessByKeyThenRun(key) : processCache.forceCacheTemplateProcessThenRun(key,
 *                 buildGetPageIngredientTypeTemplateProcess(tenantId, request, ingredientService, inventoryService)));
 * </pre>
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Slf4j
public class TemplateProcessMapCacheMangerImpl extends AbstractTemplateProcessMapCacheManger {

    //<editor-fold desc="SETUP">

    /**
     * Builder constructor
     * @param hashMap   HashMap
     */
    @Builder(builderMethodName = "hashMapCacheBuilder")
    public TemplateProcessMapCacheMangerImpl(
            Map<Serializable, TemplateProcess> hashMap
    ) {
        this.hashMapCache = hashMap;
    }

    //</editor-fold>

    //<editor-fold desc="PUBLIC OPERATIONS">

    /**
     * Cache template process referencing existence of provided key.
     * If process is exist by key do nothing.
     * If not cache the process with provided key
     *
     * @param key             Unique Process Key
     * @param templateProcess TemplateProcess
     * @return TemplateProcessCacheManger
     */
    @Override
    public TemplateProcessCacheManger cacheTemplateProcess(
            Serializable    key,
            TemplateProcess templateProcess
    ) {
        // Make sure there is no NullPointerException
        checkPreConditionOfKeyAndProcess(key, templateProcess);
        assert Objects.nonNull(this.hashMapCache);

        // Check provided key is not existed
        if(!this.hashMapCache.containsKey(key)) {

            // Cache process with provide key
            cacheProcessWithProvidedKey(key, templateProcess);

            // Check post-condition of cache process
            checkPostConditionAfterCache(key, templateProcess);
        } else {
            // Key is existed
            log.warn("Key exist: cache nothing");
        }

        return this;
    }

    /**
     * Cache template process referencing existence of provided key.
     * If process is exist by key do nothing.
     * If not cache the process with provided key
     * After cache, run process of success or else return null
     *
     * @param key             Unique Process Key
     * @param templateProcess TemplateProcess
     * @return TemplateProcess
     */
    @Override
    public Object cacheTemplateProcessThenRun(Serializable key, TemplateProcess templateProcess) {
        log.info("Application : cache process");
        this.cacheTemplateProcess(key, templateProcess);
        return Objects.requireNonNull(templateProcess).run();
    }

    /**
     * Cache template process without knowing provided key exist or not.
     * If key is existed, manager will override the process with the newly-provided
     * If key is not exist, manager cache the process with provided key
     *
     * @param key             Unique Process Key
     * @param templateProcess TemplateProcess
     * @return TemplateProcessCacheManger
     */
    @Override
    public TemplateProcessCacheManger forceCacheTemplateProcess(
            Serializable    key,
            TemplateProcess templateProcess
    ) {
        // Make sure there is no NullPointerException
        checkPreConditionOfKeyAndProcess(key, templateProcess);
        checkCacheIsNonNull();

        // Check provided key is not existed
        if(!this.hashMapCache.containsKey(key)) {

            // Cache process with provide key
            cacheProcessWithProvidedKey(key, templateProcess);
        }

        // Key is existed
        else {

            // Check old value of process is not null
            checkProcessIsNonNull(this.hashMapCache.getOrDefault(key, null));

            // Replace old process with new process
            replaceProcessWithProvidedKey(key, templateProcess);
        }

        // Check post-condition of cache process
        checkPostConditionAfterCache(key, templateProcess);

        return this;
    }

    /**
     * Cache template process without knowing provided key exist or not.
     * If key is existed, manager will override the process with the newly-provided
     * If key is not exist, manager cache the process with provided key
     * After cache, run process of success or else return null
     *
     * @param key               Unique Process Key
     * @param templateProcess   TemplateProcess
     * @return                  Object
     */
    @Override
    public Object forceCacheTemplateProcessThenRun(
            Serializable    key,
            TemplateProcess templateProcess
    ) {
        log.info("Application : force cache process");
        this.forceCacheTemplateProcess(key, templateProcess);
        return Objects.requireNonNull(templateProcess).run();
    }

    /**
     * Get template process by key. Return a template if key is exist.
     * Otherwise, return null
     *
     * @param key Unique Process Key
     * @return TemplateProcess
     */
    @Override
    public TemplateProcess getTemplateProcessByKey(
            Serializable key
    ) {
        // Check pre-condition before get process by key
        checkKeyIsNonNull(key);
        checkCacheIsNonNull();

        // Default get process with key
        return this.hashMapCache.getOrDefault(key, null);
    }

    /**
     * Get template process by key.
     * Run template process if key is exist.
     *
     * @param key Unique Process Key
     * @return TemplateProcess
     */
    @Override
    public Object getTemplateProcessByKeyThenRun(Serializable key) {
        log.info("Application : retrieve process from cache");
        return Objects.requireNonNull(this.getTemplateProcessByKey(key)).run();
    }

    /**
     * Check process is exited by key
     *
     * @param key Unique Process Key
     * @return true if provided key exist or else false
     */
    @Override
    public boolean checkProcessKeyExist(
            Serializable key
    ) {
        // Check pre-condition before get process by key
        this.checkKeyIsNonNull(key);
        this.checkCacheIsNonNull();

        // Get status of provided key in cache
        return this.hashMapCache.containsKey(key);
    }

    /**
     * Check process exist by key
     *
     * @param key Unique Process Key
     * @return true if process with provided key is not null or else false
     */
    @Override
    public boolean checkProcessExistByKey(
            Serializable key
    ) {
        // Check pre-condition before check exist process by key
        checkKeyIsNonNull(key);
        checkCacheIsNonNull();

        // Check process exist by key
        return this.hashMapCache.containsKey(key)
                && Objects.nonNull(this.hashMapCache.getOrDefault(key, null));
    }

    /**
     * Traverse to see whether a process value is existed or not
     *
     * @param templateProcess TemplateProcess
     * @return true is found otherwise false
     */
    @Override
    public boolean checkProcessExistByValue(
            TemplateProcess templateProcess
    ) {
        // Check pre-condition before check exist process by key
        checkCacheIsNonNull();

        // Check process exist by key
        return this.hashMapCache.containsValue(templateProcess);
    }

    @Override
    public void clearCache() {
        this.hashMapCache =  new HashMap<>();
    }

    //</editor-fold>

    //<editor-fold desc="NON-PUBLIC OPERATIONS">

    /**
     * Cache process with provided key.
     * This method should only be used as all pre-conditions are ensured to be right
     * @param key               Serializable
     * @param templateProcess   TemplateProcess
     */
    protected void cacheProcessWithProvidedKey(
            Serializable    key,
            TemplateProcess templateProcess
    ) {
        log.info("Cache process with key : {} - {}", key, templateProcess);
        this.hashMapCache.put(key, templateProcess);
    }

    /**
     * Check post-condition of cache
     * @param key               Serializable
     * @param templateProcess   TemplateProcess
     */
    protected void checkPostConditionAfterCache(
            Serializable    key,
            TemplateProcess templateProcess
    ) {
        assert this.hashMapCache.containsKey(key);
        assert this.hashMapCache.containsValue(templateProcess);
    }

    /**
     * Check pre-condition of key and template process
     * @param key               Serializable
     * @param templateProcess   TemplateProcess
     */
    protected void checkPreConditionOfKeyAndProcess(
            Serializable    key,
            TemplateProcess templateProcess
    ) {
        checkKeyIsNonNull(key);
        checkProcessIsNonNull(templateProcess);
    }

    /**
     * Replace old process with new process by provided key
     * This method should only be used as all pre-conditions are ensured to be right
     * @param key               Serializable
     * @param templateProcess   TemplateProcess
     */
    private void replaceProcessWithProvidedKey(
            Serializable    key,
            TemplateProcess templateProcess
    ) {
        this.hashMapCache.replace(key, templateProcess);
    }

    //</editor-fold>
}
