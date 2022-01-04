/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.entity;

import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntityWithCreatedAt;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.unit.IngredientUnit;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * <h1>Ingredient Base Entity</h1>
 *
 * <h2>Brief Information</h2>
 * <p>Base Ingredient entity is derived from the Base Entity with the created at property</p>
 * <p>The main properties which are unit, unit type and code will be used</p>
 *
 * <h2>Member Variables</h2>
 * <h3>Code</h3>
 * <ul>
 *     <li>Code must be unique so the constraint and transaction must enforce the rule</li>
 *     <li>In term of Restaurant Domain, code is used as the UNSPSC code standard</li>
 * </ul>
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
 * @see BaseEntityWithCreatedAt
 * @see Serializable
 * @see IngredientUnit
 * @see <a href="https://www.unspsc.org/"> UNSPSC Standard</a>
 */
@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class IngredientBaseEntity<ID extends Serializable> extends BaseEntityWithCreatedAt<ID> {

    @Column(name="code")
    protected String code;

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
        IngredientBaseEntity<?> that = (IngredientBaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 849462854;
    }
}
