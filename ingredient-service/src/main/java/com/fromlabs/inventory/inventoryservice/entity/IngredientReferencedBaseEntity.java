/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * <h1>Ingredient Referenced Base Entity</h1>
 *
 * <h2>Brief Information</h2>
 * <p>Base Referenced Ingredient entity is derived from the Base Entity</p>
 * <p>The main property which are ingredient as one to one relationship  will be used</p>
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
public class IngredientReferencedBaseEntity<ID extends Serializable> extends BaseEntity<ID> {

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    @ToString.Exclude
    protected IngredientEntity ingredient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IngredientReferencedBaseEntity<?> that = (IngredientReferencedBaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 809860562;
    }
}
