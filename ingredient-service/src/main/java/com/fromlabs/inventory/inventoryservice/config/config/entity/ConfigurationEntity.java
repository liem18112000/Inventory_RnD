package com.fromlabs.inventory.inventoryservice.config.config.entity;

import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "Configuration")
@Table(name = "configuration")
public class ConfigurationEntity extends BaseEntity<Long> {

    @NotNull
    @NotBlank
    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    @NotBlank
    @NotNull
    @Column(name="value_type")
    private String valueType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ConfigurationEntity that = (ConfigurationEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
