/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.validator.mapper;

import com.fromlabs.inventory.inventoryservice.common.helper.FLStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.fromlabs.inventory.inventoryservice.common.validator.CriteriaImpl.CRITERIA.*;

/**
 * Implementation of ValidatorMessageMapper
 */
public class ValidatorMessageMapperImpl implements ValidatorMessageMapper {

    /**
     * Constructor
     */
    public ValidatorMessageMapperImpl() {
        this.container = new HashMap<>();
        this.defaultMessageAssign();
    }

    protected Map<String, String> container;

    protected final String DEFAULT_VALIDATE_MESSAGE = "%s does not pass validation";

    /**
     * defaultMessageAssign
     * @return ValidatorMessageMapper
     */
    public ValidatorMessageMapper defaultMessageAssign() {
        return this.map(REQUIRED.toString(), "Field is required")
                .map(LONG.toString(), "%s is not a Long")
                .map(FLOAT.toString(), "%s is not a Float")
                .map(DUPLICATE.toString(), "'%s' is duplicated")
                .map(EQUAL.toString(), "'%s' is not equal '%s'")
                .map(POSITIVE.toString(), "'%S' is not a positive number");
    }

    /**
     * map
     * @param criteria  String of criteria
     * @param message   String of message
     * @return ValidatorMessageMapper
     */
    public ValidatorMessageMapper map(String criteria, String message) {
        if(this.containCriteria(criteria)) this.container.replace(criteria, message);
        else this.container.put(criteria, message);
        return this;
    }

    /**
     * getMessage
     * @param key String
     * @return String
     */
    public String getMessage(String key) {
        return FLStringUtils.isNullOrEmpty(this.container.get(key)) ?
                this.DEFAULT_VALIDATE_MESSAGE : this.container.get(key) ;
    }

    /**
     * Get all criteria
     * @param key String
     * @return Set&lt;String&gt;
     */
    public Set<String> getAllCriteria(String key) {
        return this.container.keySet();
    }

    /**
     * Get all messages
     * @param key String
     * @return Set&lt;String&gt;
     */
    public Set<String> getAllMessage(String key) {
        return Set.copyOf(this.container.values());
    }

    /**
     * containCriteria
     * @param criteria String of criteria
     * @return boolean
     */
    public boolean containCriteria(String criteria) {
        if(FLStringUtils.isNullOrEmpty(criteria)) return false;
        return this.container.containsKey(criteria);
    }

    /**
     * containMessage
     * @param message String of message
     * @return boolean
     */
    public boolean containMessage(String message) {
        return this.container.containsValue(message);
    }
}
