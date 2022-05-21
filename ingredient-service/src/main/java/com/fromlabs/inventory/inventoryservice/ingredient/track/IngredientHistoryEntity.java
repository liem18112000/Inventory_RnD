/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track;

import com.fromlabs.inventory.inventoryservice.entity.IngredientReferencedMultiEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.event.IngredientEventEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request.IngredientHistoryRequest;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Instant;
import java.util.Objects;

/**
 * Ingredient history entity
 * @author Liem
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name="history")
public class IngredientHistoryEntity extends IngredientReferencedMultiEntity<Long> {

    static public transient final String DEFAULT_ACTOR_ROLE = "inventory-keeper";

    @Column(name="actor_name")
    protected String actorName = DEFAULT_ACTOR_ROLE;

    @Column(name="actor_role")
    protected String actorRole = DEFAULT_ACTOR_ROLE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @ToString.Exclude
    protected IngredientEventEntity event;

    @Column(name="extra_information")
    protected String extraInformation;

    @Column(name="extra_information_class")
    protected String extraInformationClass = "ItemRequest";

    @Column(name="track_timestamp")
    protected String trackTimestamp = Instant.now().toString();

    @Column(name="event_status")
    protected String eventStatus;

    /**
     * Update entity information
     * @param request   IngredientHistoryRequest
     * @return          IngredientHistoryEntity
     */
    public IngredientHistoryEntity update(
            @NotNull final IngredientHistoryRequest request
    ) {
        this.setName(request.getName());
        this.setDescription(request.getDescription());
        this.setExtraInformation(request.getExtraInformation());
        this.setUpdateAt(Instant.now().toString());
        return this;
    }
}
