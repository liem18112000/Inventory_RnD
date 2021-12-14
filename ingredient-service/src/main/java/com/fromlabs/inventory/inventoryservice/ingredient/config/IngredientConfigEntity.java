/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.config;

import com.fromlabs.inventory.inventoryservice.entity.IngredientReferencedBaseEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.config.beans.request.IngredientConfigRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.Objects;

/**
 * Entity for ingredient config
 */
@Entity
@Table(name="ingredient_config")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class IngredientConfigEntity extends IngredientReferencedBaseEntity<Long> {

    @Max(value = 32767)
    @Min(value = 0)
    @Column(name="minimum_quantity")
    private Float minimumQuantity = 1f;

    @Min(value = 1)
    @Column(name="maximum_quantity")
    private Float maximumQuantity = 32767F;

    /**
     * Convert from request to entity
     * @param request       IngredientRequest
     * @param ingredient    IngredientEntity
     * @return  IngredientConfigEntity
     */
    static public IngredientConfigEntity from(IngredientRequest request, IngredientEntity ingredient) {
        var config = new IngredientConfigEntity();
        config.setClientId(request.getClientId());
        config.setName(request.getName());
        config.setDescription(request.getDescription());
        config.setIngredient(ingredient);
        if(Objects.nonNull(request.getMinimumQuantity())) config.setMinimumQuantity(request.getMinimumQuantity());
        if(Objects.nonNull(request.getMaximumQuantity())) config.setMaximumQuantity(request.getMaximumQuantity());
        config.setUpdateAt(Instant.now().toString());
        return config;
    }

    /**
     * Update entity from request
     * @param request   IngredientConfigRequest
     * @return  IngredientConfigEntity
     */
    public IngredientConfigEntity update(IngredientConfigRequest request) {
        this.setClientId(request.getClientId());
        this.setIngredient(ingredient);
        if(Objects.nonNull(request.getMinimumQuantity())) this.setMinimumQuantity(request.getMinimumQuantity());
        if(Objects.nonNull(request.getMaximumQuantity())) this.setMaximumQuantity(request.getMaximumQuantity());
        this.setUpdateAt(Instant.now().toString());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IngredientConfigEntity that = (IngredientConfigEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 566213035;
    }
}
