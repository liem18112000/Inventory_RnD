/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.validator;

import com.fromlabs.inventory.notificationservice.common.validator.mapper.ValidatorMessageMapper;
import com.fromlabs.inventory.notificationservice.common.validator.mapper.ValidatorMessageMapperImpl;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Criteria Implementation for Criteria Interface
 */
public class CriteriaImpl implements Criteria<String>{

    /**
     * Constructor
     */
    public CriteriaImpl() {
        this.messageMapper = new ValidatorMessageMapperImpl();
    }

    /**
     * Constructor with ValidatorMessageMapper
     * @param messageMapper ValidatorMessageMapper
     * @see ValidatorMessageMapper
     */
    public CriteriaImpl(ValidatorMessageMapper messageMapper) {
        this.messageMapper = Objects.requireNonNullElse(messageMapper, new ValidatorMessageMapperImpl()) ;
    }

    protected ValidatorMessageMapper messageMapper;

    @Data
    @AllArgsConstructor
    static public class Pair<K, V>{
        private K key;
        private V value;
    }

    public enum CRITERIA{
        REQUIRED,
        LONG,
        DUPLICATE,
        EQUAL,
        FLOAT,
        POSITIVE
    }

    /**
     * Overall validation check for all criteria of a specific object
     * @param object    checked object
     * @param criterias Set of criteria
     * @return Pair
     * @see Pair
     */
    public Pair<Set<String>, Boolean> check(Object object,  Set<Enum> criterias){
        var errMessage = new HashSet<String>();
        criterias.forEach(criteria -> {
            if(object instanceof Pair<?, ?>)
                binaryCheck((Pair<?, ?>) object, errMessage, criteria);
            else
                unaryCheck(object, errMessage, criteria);
        });
        return new Pair<>(errMessage, errMessage.isEmpty());
    }

    /**
     * unaryCheck
     * @param object        checked object
     * @param errMessage    map of errors
     * @param criteria      a criteria
     */
    public void unaryCheck(Object object, HashSet<String> errMessage, Enum criteria) {
        if(criteria.name().equals(CRITERIA.REQUIRED.name()) && isNull(object))
            errMessage.add(this.messageMapper.getMessage(CRITERIA.REQUIRED.toString()));

        if(criteria.name().equals(CRITERIA.LONG.name()) && !isLong(object))
            errMessage.add(String.format(this.messageMapper.getMessage(CRITERIA.LONG.toString()), object));

        if(criteria.name().equals(CRITERIA.FLOAT.name()) && !isFloat(object))
            errMessage.add(String.format(this.messageMapper.getMessage(CRITERIA.FLOAT.toString()), object));

        if(criteria.name().equals(CRITERIA.POSITIVE.name()) && !isPositive(object))
            errMessage.add(String.format(this.messageMapper.getMessage(CRITERIA.POSITIVE.toString()), object));
    }

    /**
     * binaryCheck
     * @param object        checked object
     * @param errMessage    map of error
     * @param criteria      a criteria
     */
    public void binaryCheck(Pair<?, ?> object, HashSet<String> errMessage, Enum criteria) {
        if(!isNull(object) && object.getValue() instanceof Collection<?>)
            binaryMultiValueCheck(errMessage, criteria, object);
        else
            binarySingleValueCheck(errMessage, criteria, object);
    }

    /**
     * binarySingleValueCheck
     * @param errMessage    map of error
     * @param criteria      Enum of criteria
     * @param pair          Pair
     * @see Pair
     * @see CRITERIA
     */
    public void binarySingleValueCheck(HashSet<String> errMessage, Enum criteria, Pair<?, ?> pair) {
        if(criteria.name().equals(CRITERIA.EQUAL.name()) && !isEqual(pair))
            errMessage.add(String.format(this.messageMapper.getMessage(CRITERIA.EQUAL.toString()), pair.getKey(), pair.getValue()));
    }

    /**
     * binaryMultiValueCheck
     * @param errMessage    map of error
     * @param criteria      Enum of criteria
     * @param pair          Pair
     * @see Pair
     * @see CRITERIA
     */
    public void binaryMultiValueCheck(HashSet<String> errMessage, Enum criteria, Pair<?, ?> pair) {
        if(criteria.name().equals(CRITERIA.DUPLICATE.name()) && isDuplicate(pair))
            errMessage.add(String.format(this.messageMapper.getMessage(CRITERIA.DUPLICATE.toString()), pair.getKey()));
    }

    /**
     * isEqual
     * @param pair  Pair
     * @return boolean
     * @see Pair
     */
    protected boolean isEqual(Pair<?, ?> pair) {
        return Objects.equals(pair.getKey(), pair.getValue());
    }

    /**
     * isDuplicate
     * @param pair  Pair
     * @return boolean
     * @see Pair
     */
    protected boolean isDuplicate(Pair<?, ?> pair) {
        return ( (Collection<?>) pair.getValue()).contains(pair.getKey());
    }

    /**
     * isNull
     * @param obj checked object
     * @return boolean
     */
    protected boolean isNull(Object obj){
        if (obj == null){
            return true;
        } else if (obj instanceof String) {
            return String.valueOf(obj).isEmpty() || String.valueOf(obj).isBlank();
        }
        return false;
    }

    /**
     * isLong
     * @param obj checked object
     * @return boolean
     */
    protected boolean isLong(Object obj){
        try{
            Long.parseLong(String.valueOf(obj));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * isFloat
     * @param obj checked object
     * @return boolean
     * @see Float
     * @see Exception
     */
    protected boolean isFloat(Object obj){
        try{
            Float.parseFloat(String.valueOf(obj));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * isPositive
     * @param obj checked object
     * @return boolean
     * @see Float
     * @see Exception
     */
    protected boolean isPositive(Object obj){
        if(obj instanceof Long)
            return (Long)obj >= 0L;
        else if(obj instanceof Integer)
            return (Integer)obj >= 0L;
        else if(obj instanceof Float)
            return (Float)obj >= 0;
        else if(obj instanceof Double)
            return (Double)obj >= 0;
        else if(obj instanceof String){
            return (isFloat(obj) && Float.parseFloat((String) obj) >= 0) || (isLong(obj) && Long.parseLong((String) obj) >= 0);
        }
        return false;
    }
}
