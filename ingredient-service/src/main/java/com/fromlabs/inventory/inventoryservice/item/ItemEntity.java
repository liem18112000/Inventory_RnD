package com.fromlabs.inventory.inventoryservice.item;

import com.fromlabs.inventory.inventoryservice.entity.IngredientReferencedExtendMultiEntity;
import com.fromlabs.inventory.inventoryservice.item.beans.ItemPageRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.ItemRequest;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ItemEntity extends IngredientReferencedExtendMultiEntity<Long> {

    @Column(name="import_id")
    private Long importId;

    @Column(name="code")
    private String code;

    @Column(name="expired_at")
    private String expiredAt;

    static public ItemEntity from(ItemRequest request) {
        var item = new ItemEntity();
        item.setId(request.getId());
        item.setClientId(request.getClientId());
        item.setCode(request.getCode());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setUnit(request.getUnit());
        item.setUnitType(request.getUnitType());
        item.setExpiredAt(request.getExpiredAt());
        item.setUpdateAt(Instant.now().toString());
        return item;
    }

    static public ItemEntity from(ItemPageRequest request) {
        var item = new ItemEntity();
        item.setClientId(request.getClientId());
        item.setCode(request.getCode());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setUnit(request.getUnit());
        item.setUnitType(request.getUnitType());
        item.setExpiredAt(request.getExpiredAt());
        item.setUpdateAt(request.getUpdateAt());
        return item;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(LocalDate.parse(this.getExpiredAt()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ItemEntity that = (ItemEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 1958895947;
    }
}
