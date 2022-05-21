/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event;

import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity(name = "IngredientEvent")
@Table(name = "event")
@Getter
@Setter
@RequiredArgsConstructor
public class IngredientEventEntity extends BaseEntity<Long> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IngredientEventEntity that = (IngredientEventEntity) o;
        return id != null && Objects.equals(id, that.id);
    }
}
