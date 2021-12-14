/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * <h1>Ingredient Referenced Multi Entity</h1>
 *
 * <h2>Brief Information</h2>
 * <p>Ingredient Referenced Multi entity is derived from the Base Entity</p>
 * <p>The main property which are ingredient as many to one relationship will be used</p>
 *
 * <h2>Member Variables</h2>
 * <h3>Ingredient</h3>
 * <ul>
 *     <li>It is the Ingredient Type (child of Ingredient Category)</li>
 *     <li>The ingredient must be exist when this type of entity is created</li>
 * </ul>
 * @param <ID>  Entity ID type which must be derived from Serializable
 * @see BaseEntity
 * @see Serializable
 * @see IngredientEntity
 */
@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class IngredientReferencedMultiEntity <ID extends Serializable> extends BaseEntity<ID> {

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    @ToString.Exclude
    protected IngredientEntity ingredient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IngredientReferencedMultiEntity<?> that = (IngredientReferencedMultiEntity<?>) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
