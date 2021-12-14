/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.validator.mapper;

import java.util.Set;

/**
 * Validator mapper for error message_queue
 */
public interface ValidatorMessageMapper {
    /**
     * map
     * @param criteria  String of criteria
     * @param message   String of message
     * @return  ValidatorMessageMapper
     */
    ValidatorMessageMapper map(String criteria, String message);;

    /**
     * getMessage
     * @param key String
     * @return String
     */
    String getMessage(String key);

    /**
     * getAllCriteria
     * @param key String
     * @return Set&lt;String&gt;
     */
    Set<String> getAllCriteria(String key);

    /**
     * getAllMessage
     * @param key String
     * @return Set&lt;String&gt;
     */
    Set<String> getAllMessage(String key);

    /**
     * containCriteria
     * @param criteria String of criteria
     * @return boolean
     */
    boolean containCriteria(String criteria);

    /**
     * containMessage
     * @param message String of message
     * @return boolean
     */
    boolean containMessage(String message);

    /**
     * defaultMessageAssign
     * @return ValidatorMessageMapper
     */
    ValidatorMessageMapper defaultMessageAssign();
}
