package com.fromlabs.inventory.recipeservice.common.specifications;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;

    static public SearchCriteria criteriaLessThan(String key, Object value) {
        return SearchCriteria.builder().key(key).operation(SearchOperation.LESS_THAN).value(value).build();
    }

    static public SearchCriteria criteriaLessThanOrEqual(String key, Object value) {
        return SearchCriteria.builder().key(key).operation(SearchOperation.LESS_THAN_OR_EQUAL).value(value).build();
    }

    static public SearchCriteria criteriaGreaterThan(String key, Object value) {
        return SearchCriteria.builder().key(key).operation(SearchOperation.GREATER_THAN).value(value).build();
    }

    static public SearchCriteria criteriaGreaterThanOrEqual(String key, Object value) {
        return SearchCriteria.builder().key(key).operation(SearchOperation.GREATER_THAN_OR_EQUAL).value(value).build();
    }

    static public SearchCriteria criteriaEqual(String key, Object value) {
        return SearchCriteria.builder().key(key).operation(SearchOperation.EQUAL).value(value).build();
    }

    static public SearchCriteria criteriaNotEqual(String key, Object value) {
        return SearchCriteria.builder().key(key).operation(SearchOperation.NOT_EQUAL).value(value).build();
    }
}
