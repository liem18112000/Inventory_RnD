package com.fromlabs.inventory.inventoryservice.inventory;

import com.fromlabs.inventory.inventoryservice.entity.IngredientReferencedExtendEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientPageRequest;
import com.fromlabs.inventory.inventoryservice.inventory.beans.InventoryPageRequest;
import com.fromlabs.inventory.inventoryservice.inventory.beans.InventoryRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.Objects;

/**
 * Inventory entity
 */
@Entity
@Table(name="inventory")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class InventoryEntity extends IngredientReferencedExtendEntity<Long> {

    @Min(value = 0, message = "Quantity should be equal or greater than zero")
    @Column(name="quantity")
    private Float quantity = 0f;

    @Min(value = 0, message = "Reserve should be equal or greater than zero")
    @Column(name="reserved")
    private Float reserved = 0f;

    public Long getIngredientId() {
        return this.ingredient.getId();
    }

    public InventoryEntity update(InventoryRequest request) {
        this.setName(request.getName());
        this.setDescription(request.getDescription());
        this.setUpdateAt(request.getUpdateAt());
        return this;
    }

    static public InventoryEntity from(InventoryPageRequest request) {
        var entity = new InventoryEntity();
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setUnit(request.getUnit());
        entity.setUnitType(request.getUnitType());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InventoryEntity that = (InventoryEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 99820787;
    }
}
