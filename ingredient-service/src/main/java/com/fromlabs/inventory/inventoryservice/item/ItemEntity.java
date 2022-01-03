package com.fromlabs.inventory.inventoryservice.item;

import com.fromlabs.inventory.inventoryservice.entity.IngredientReferencedExtendMultiEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ItemEntity extends IngredientReferencedExtendMultiEntity<Long>{

    @Column(name="import_id")
    private Long importId;

    @Column(name="code")
    private String code;

    @Column(name="expired_at")
    private String expiredAt;

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
