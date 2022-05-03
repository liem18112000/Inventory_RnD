/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track.specification;

import com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.event.IngredientEventEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request.IngredientHistoryPageRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;

import java.util.Objects;

import static com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification.Spec;
import static com.fromlabs.inventory.inventoryservice.common.specifications.SearchCriteria.*;
import static java.util.Objects.isNull;

@UtilityClass
public class IngredientHistorySpecification {

    /**
     * Filter for has client id
     * @param clientId  Client ID
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasClientId(Long clientId) {
        return Spec(criteriaEqual("clientId", clientId));
    }

    /**
     * Filter for has ingredient
     * @param ingredient    Ingredient entity
     * @return              BaseSpecification&lt;IngredientHistoryEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasIngredient(IngredientEntity ingredient) {
        return Spec(criteriaEqual("ingredient", ingredient));
    }

    /**
     * Filter for has event
     * @param event         Event entity
     * @return              BaseSpecification&lt;IngredientHistoryEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasEvent(IngredientEventEntity event) {
        return Spec(criteriaEqual("event", event));
    }

    /**
     * Filter for has history name
     * @param name      Ingredient history name
     * @return          BaseSpecification&lt;IngredientHistoryEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasName(String name) {
        return Spec(criteriaEqual("name", name.trim()));
    }

    /**
     * Filter for has actor name
     * @param actorName Ingredient history actor name
     * @return          BaseSpecification&lt;IngredientHistoryEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasActorName(String actorName) {
        return Spec(criteriaEqual("actorName", actorName.trim()));
    }

    /**
     * Filter for has actor role
     * @param actorRole Ingredient history actor role
     * @return          BaseSpecification&lt;IngredientHistoryEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasActorRole(String actorRole) {
        return Spec(criteriaEqual("actorRole", actorRole.trim()));
    }

    /**
     * Filter for has description
     * @param description Ingredient history description
     * @return            BaseSpecification&lt;IngredientHistoryEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasDescription(String description) {
        return Spec(criteriaEqual("description", description.trim()));
    }

    /**
     * Filter for has updated at
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;IngredientHistoryEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasUpdatedAt(String updateAt) {
        return Spec(criteriaEqual("updateAt", updateAt));
    }

    /**
     * Filter for has updated from
     * @param updateAt  update timestamp
     * @return          BaseSpecification&lt;IngredientHistoryEntity&gt;
     */
    public static BaseSpecification<IngredientHistoryEntity> hasUpdateAtGreaterThan(String updateAt) {
        return Spec(criteriaTimestampGreaterThanOrEqual("updateAt", updateAt));
    }

    /**
     * Filter for has track time stamp
     * @param track     Track timestamp
     * @return          BaseSpecification&lt;IngredientEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasTrackTimestamp(String track) {
        return Spec(criteriaEqual("trackTimestamp", track));
    }

    /**
     * Filter for has event status
     * @param eventStatus   history event status
     * @return              BaseSpecification&lt;IngredientEntity&gt;
     */
    public BaseSpecification<IngredientHistoryEntity> hasEventStatus(String eventStatus) {
        return Spec(criteriaEqual("eventStatus", eventStatus));
    }

    /**
     * Filter for all ingredient
     * @param criteria    IngredientHistoryPageRequest
     * @return          Specification&lt;IngredientEntity&gt;
     */
    public Specification<IngredientHistoryEntity> filter(
            @NotNull final IngredientHistoryPageRequest criteria,
            final IngredientEntity ingredient,
            final IngredientEventEntity event
    ) {
        var spec =   hasClientId(criteria.getClientId())
                .and(hasName(criteria.getName()))
                .and(hasDescription(criteria.getDescription()))
                .and(hasActorName(criteria.getActorName()))
                .and(hasActorRole(criteria.getActorRole()))
                .and(hasTrackTimestamp(criteria.getTrackTimestamp()))
                .and(StringUtils.hasText(criteria.getUpdateAt()) ?
                        hasUpdateAtGreaterThan(criteria.getUpdateAt()) : hasUpdatedAt(criteria.getUpdateAt()))
                .and(hasIngredient(ingredient))
                .and(hasEventStatus(criteria.getStatus()));
        return isNull(event) ? spec : spec.and(hasEvent(event));
    }
}
