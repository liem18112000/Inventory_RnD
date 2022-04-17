package com.fromlabs.inventory.recipeservice.common.specifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;

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
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (operation.equals(SearchOperation.GREATER_THAN)){
            return builder.greaterThan(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (operation.equals(SearchOperation.LESS_THAN_OR_EQUAL)) {
            return builder.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (operation.equals(SearchOperation.LESS_THAN)){
            return builder.lessThan(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (operation.equals(SearchOperation.EQUAL)) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        else if(operation.equals(SearchOperation.NOT_EQUAL)) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.notLike(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            }
        } else if (operation.equals(SearchOperation.TIMESTAMP_GREATER_THAN)){
            return builder.greaterThan(
                    builder.function("DATE", Date.class, root.get(criteria.getKey())),
                    builder.function("DATE", Date.class, builder.literal(criteria.getValue().toString()))
            );
        }
        else if (operation.equals(SearchOperation.TIMESTAMP_LESS_THAN)){
            return builder.lessThan(
                    builder.function("DATE", Date.class, root.get(criteria.getKey())),
                    builder.function("DATE", Date.class, builder.literal(criteria.getValue().toString()))
            );
        }
        else if (operation.equals(SearchOperation.TIMESTAMP_GREATER_THAN_OR_EQUAL)){
            return builder.greaterThanOrEqualTo(
                    builder.function("DATE", Date.class, root.get(criteria.getKey())),
                    builder.function("DATE", Date.class, builder.literal(criteria.getValue().toString()))
            );
        }
        else if (operation.equals(SearchOperation.TIMESTAMP_LESS_THAN_OR_EQUAL)){
            return builder.lessThanOrEqualTo(
                    builder.function("DATE", Date.class, root.get(criteria.getKey())),
                    builder.function("DATE", Date.class, builder.literal(criteria.getValue().toString()))
            );
        }
        return null;
    }
}
