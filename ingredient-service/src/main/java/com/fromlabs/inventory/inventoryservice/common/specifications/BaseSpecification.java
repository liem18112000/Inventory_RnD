package com.fromlabs.inventory.inventoryservice.common.specifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
@NoArgsConstructor
@Data
public final class BaseSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    static public <T> BaseSpecification<T> Spec(SearchCriteria searchCriteria) {
        var spec = new BaseSpecification<T>();
        spec.setCriteria(searchCriteria);
        return spec;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        final SearchOperation operation = criteria.getOperation();
        if (operation.equals(SearchOperation.GREATER_THAN_OR_EQUAL)) {
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (operation.equals(SearchOperation.GREATER_THAN)){
            return builder.greaterThan(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (operation.equals(SearchOperation.LESS_THAN_OR_EQUAL)) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (operation.equals(SearchOperation.LESS_THAN)){
            return builder.lessThan(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (operation.equals(SearchOperation.EQUAL)) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                if (criteria.isStrict()) {
                    if (criteria.getValue().toString().isEmpty() && criteria.isIgnoreEmpty()) {
                        return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
                    }
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        else if(operation.equals(SearchOperation.NOT_EQUAL)) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.notLike(
                        root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
