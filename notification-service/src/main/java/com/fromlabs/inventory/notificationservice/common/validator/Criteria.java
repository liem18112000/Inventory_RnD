/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.validator;

import java.util.HashSet;
import java.util.Set;

/**
 * Criteria for validation
 * @param <V> generic type
 */
public interface Criteria<V> {
    /**
     * check
     * @param object    checked object
     * @param criterias Set of criteria
     * @return CriteriaImpl.Pair
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     */
    CriteriaImpl.Pair<Set<V>, Boolean> check(Object object, Set<Enum> criterias);

    /**
     * unaryCheck
     * @param object        checked object
     * @param errMessage    map of error
     * @param criteria      criteria
     */
    void unaryCheck(Object object, HashSet<String> errMessage, Enum criteria);

    /**
     * binaryCheck
     * @param object        checked object
     * @param errMessage    map of error
     * @param criteria      criteria
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     */
    void binaryCheck(CriteriaImpl.Pair<?, ?> object, HashSet<String> errMessage, Enum criteria);

    /**
     * binarySingleValueCheck
     * @param errMessage    map of error
     * @param criteria      criteria
     * @param pair          CriteriaImpl.Pair
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     */
    void binarySingleValueCheck(HashSet<String> errMessage, Enum criteria, CriteriaImpl.Pair<?, ?> pair);

    /**
     * binaryMultiValueCheck
     * @param errMessage    map of error
     * @param criteria      criteria
     * @param pair          CriteriaImpl.Pair
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     */
    void binaryMultiValueCheck(HashSet<String> errMessage, Enum criteria, CriteriaImpl.Pair<?, ?> pair);
}
