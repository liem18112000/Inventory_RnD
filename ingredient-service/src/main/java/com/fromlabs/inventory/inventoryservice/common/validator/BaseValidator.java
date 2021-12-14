/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.validator;

/**
 * Base validator
 * @param <K> key
 * @param <V> value
 */
public interface BaseValidator<K, V>{

    /**
     * isValid
     * @return boolean
     */
    boolean isValid();

    /**
     * isInvalid
     * @return boolean
     */
    boolean isInvalid();

    /**
     * hasError
     * @return boolean
     */
    boolean hasError();

    /**
     * validate
     * @return BaseValidator
     */
    BaseValidator<K, V> validate();

    /**
     * getErrors
     * @return Object
     */
    Object getErrors();
}
