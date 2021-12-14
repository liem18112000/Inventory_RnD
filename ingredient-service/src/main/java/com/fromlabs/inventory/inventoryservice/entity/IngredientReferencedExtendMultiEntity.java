/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * <h1>Ingredient Referenced Extend Multi Entity</h1>
 *
 * <h2>Brief Information</h2>
 * <p>Ingredient Referenced Extend Multi Entity is derived from the Base Entity</p>
 * <p>The main property which are unit and unit type will be used</p>
 *
 * <h2>Member Variables</h2>
 * <h3>Unit</h3>
 * <ul>
 *     <li>At first, there are five primitive types of measurement unit type :
 *          <ul>
 *              <li>Whole : represent for whole unit measurement</li>
 *              <li>Length : represent for length-based unit measurement</li>
 *              <li>Area : represent for area-based unit measurement</li>
 *              <li>Weight: represent for weight-based measurement</li>
 *              <li>Volume: represent for volume-bases measurement</li>
 *          </ul>
 *     </li>
 * </ul>
 * @param <ID>  Entity ID type which must be derived from Serializable
 * @see IngredientReferencedMultiEntity
 * @see Serializable
 */
@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class IngredientReferencedExtendMultiEntity<ID extends Serializable> extends IngredientReferencedMultiEntity<ID> {

    @NotNull(message = "Unit should not be null")
    @NotBlank(message = "Unit should not be blank")
    @Column(name="unit")
    protected String unit;

    @NotNull(message = "Unit type should not be null")
    @NotBlank(message = "Unit type should not be blank")
    @Column(name="unit_type")
    protected String unitType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IngredientReferencedExtendMultiEntity<?> that = (IngredientReferencedExtendMultiEntity<?>) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
