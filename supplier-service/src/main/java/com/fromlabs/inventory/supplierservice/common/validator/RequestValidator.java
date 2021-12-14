/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.common.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Request validation for web controller
 */
public class RequestValidator implements BaseValidator<String, Set<String>>{

    protected Map<String, Set<String>> errorContainer = new HashMap<>();
    protected Map<CriteriaImpl.Pair<String, Object>, Set<Enum>> criteriaContainer = new HashMap<>();
    protected Criteria<String> criteria;

    /**
     * Construction
     * @return RequestValidator
     */
    public static RequestValidator StringRequestValidator() {
        return new RequestValidator();
    }

    /**
     * Construction
     * @param criteria  Criteria&lt;String&gt;
     * @return RequestValidator
     */
    public static RequestValidator StringRequestValidator(Criteria<String> criteria) {
        return new RequestValidator(criteria);
    }

    /**
     * Construction
     */
    public RequestValidator() {
        this.criteria = new CriteriaImpl();
    }

    /**
     * Construction
     * @param criteria  Criteria&lt;String&gt;
     * @see Criteria
     * @see CriteriaImpl
     */
    public RequestValidator(Criteria<String> criteria) {
        this.criteria = Objects.requireNonNullElse(criteria, new CriteriaImpl());
    }

    /**
     * Create criteria
     * @param key       String
     * @param value     Object
     * @param criteria  Criteria&lt;String&gt;
     * @return RequestValidator
     * @see RequestValidator
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     */
    public RequestValidator criteria(String key, Object value, Set<Enum> criteria){
        final CriteriaImpl.Pair<String, Object> validatePair = new CriteriaImpl.Pair<>(key, value);
        if(criteria != null){
            this.criteriaContainer.put(validatePair, criteria);
        }
        return this;
    }

    /**
     * Set criteria
     * @param key       String
     * @param value     Object
     * @param criteria  Criteria&lt;String&gt;
     * @return RequestValidator
     * @see RequestValidator
     * @see CriteriaImpl
     * @see CriteriaImpl.CRITERIA
     */
    public RequestValidator criteria(String key, Object value, CriteriaImpl.CRITERIA criteria){
        final CriteriaImpl.Pair<String, Object> validatePair = new CriteriaImpl.Pair<>(key, value);
        if(criteria != null){
            this.criteriaContainer.put(validatePair, Set.of(criteria));
        }
        return this;
    }

    /**
     * Set multiple ternary criteria
     * @param key       String
     * @param value1    first value
     * @param value2    second value
     * @param criteria  Enum of criteria
     * @param <V1>      generic type for first value
     * @param <V2>      generic type for second value
     * @return RequestValidator
     * @see RequestValidator
     */
    public <V1, V2> RequestValidator criteria(String key, V1 value1, V2 value2, Set<Enum> criteria){
        return this.criteria(key, new CriteriaImpl.Pair<>(value1, value2), criteria);
    }

    /**
     * Set single ternary criteria
     * @param key       String
     * @param value1    first value
     * @param value2    second value
     * @param criteria  CriteriaImpl.CRITERIA
     * @param <V1>      generic type for first value
     * @param <V2>      generic type for second value
     * @return RequestValidator
     * @see RequestValidator
     * @see CriteriaImpl.CRITERIA
     * @see CriteriaImpl
     */
    public <V1, V2> RequestValidator criteria(String key, V1 value1, V2 value2, CriteriaImpl.CRITERIA criteria){
        return this.criteria(key, new CriteriaImpl.Pair<>(value1, value2), criteria);
    }

    /**
     * criteriaRequired
     * @param key   String
     * @param value Value
     * @return RequestValidator
     * @see RequestValidator
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     * @see CriteriaImpl.CRITERIA
     */
    public RequestValidator criteriaRequired(String key, Object value){
        final CriteriaImpl.Pair<String, Object> validatePair = new CriteriaImpl.Pair<>(key, value);
        this.criteriaContainer.put(validatePair, Set.of(CriteriaImpl.CRITERIA.REQUIRED));
        return this;
    }

    /**
     * criteriaIsLong
     * @param key   String
     * @param value Value
     * @return RequestValidator
     * @see RequestValidator
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     * @see CriteriaImpl.CRITERIA
     */
    public RequestValidator criteriaIsLong(String key, Object value){
        final CriteriaImpl.Pair<String, Object> validatePair = new CriteriaImpl.Pair<>(key, value);
        this.criteriaContainer.put(validatePair, Set.of(CriteriaImpl.CRITERIA.REQUIRED, CriteriaImpl.CRITERIA.LONG));
        return this;
    }

    /**
     * criteriaIsFloat
     * @param key   String
     * @param value Value
     * @return RequestValidator
     * @see RequestValidator
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     * @see CriteriaImpl.CRITERIA
     */
    public RequestValidator criteriaIsFloat(String key, Object value){
        final CriteriaImpl.Pair<String, Object> validatePair = new CriteriaImpl.Pair<>(key, value);
        this.criteriaContainer.put(validatePair, Set.of(CriteriaImpl.CRITERIA.REQUIRED, CriteriaImpl.CRITERIA.FLOAT));
        return this;
    }

    /**
     * criteriaIsPositiveLong
     * @param key   String
     * @param value Value
     * @return RequestValidator
     * @see RequestValidator
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     * @see CriteriaImpl.CRITERIA
     */
    public RequestValidator criteriaIsPositiveLong(String key, Object value){
        final CriteriaImpl.Pair<String, Object> validatePair = new CriteriaImpl.Pair<>(key, value);
        this.criteriaContainer.put(validatePair, Set.of(CriteriaImpl.CRITERIA.REQUIRED, CriteriaImpl.CRITERIA.LONG, CriteriaImpl.CRITERIA.POSITIVE));
        return this;
    }

    /**
     * criteriaIsPositiveFloat
     * @param key   String
     * @param value Value
     * @return RequestValidator
     * @see RequestValidator
     * @see CriteriaImpl.Pair
     * @see CriteriaImpl
     * @see CriteriaImpl.CRITERIA
     */
    public RequestValidator criteriaIsPositiveFloat(String key, Object value){
        final CriteriaImpl.Pair<String, Object> validatePair = new CriteriaImpl.Pair<>(key, value);
        this.criteriaContainer.put(validatePair, Set.of(CriteriaImpl.CRITERIA.REQUIRED, CriteriaImpl.CRITERIA.FLOAT, CriteriaImpl.CRITERIA.POSITIVE));
        return this;
    }

    /**
     * Check validation is valid or not
     * @return boolean
     */
    public boolean isValid() {
        return this.errorContainer.isEmpty();
    }

    /**
     * Check validation is invalid or not
     * @return boolean
     */
    public boolean isInvalid() {
        return !this.isValid();
    }

    /**
     * Check has error in map container
     * @return boolean
     */
    public boolean hasError() {
        return this.isInvalid();
    }

    /**
     * Get errors
     * @return Object
     * @see ValidationInfo
     */
    public Object getErrors() {
        return ValidationInfo.getInfo(this.errorContainer);
    }

    /**
     * Get error from throwable
     * @param throwable an Exception
     * @return Object
     * @see ValidationInfo
     */
    public Object getErrors(Throwable throwable) {
        return ValidationInfo.getInfo(throwable, this.errorContainer);
    }

    /**
     * Generate validation wrapper of error
     * @param message   extra error message
     * @return Object
     * @see ValidationInfo
     */
    public Object getErrors(String message) {
        return ValidationInfo.getInfo(this.errorContainer, message);
    }

    /**
     * Validate function
     * @return RequestValidator
     */
    public RequestValidator validate() {
        this.criteriaContainer.forEach(this::checkEachParam);
        return this;
    }

    /**
     * Check each param in the map container
     * @param pair      CriteriaImpl.Pair
     * @param criteria  Enum of criteria
     * @see CriteriaImpl.Pair
     */
    private void checkEachParam(CriteriaImpl.Pair<String, Object> pair, Set<Enum> criteria) {
        final var checkResult = this.criteria.check(pair.getValue(), criteria);
        if(!checkResult.getValue()){
            if(this.errorContainer.isEmpty() || !this.errorContainer.containsKey(pair.getKey()))
                this.errorContainer.put(pair.getKey(), checkResult.getKey());
            else
                this.errorContainer.get(pair.getKey()).addAll(checkResult.getKey());
        }
    }
}
