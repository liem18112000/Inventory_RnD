package com.fromlabs.inventory.recipeservice.entity;

import com.fromlabs.inventory.recipeservice.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RecipeBaseEntity<ID extends Serializable> extends BaseEntity<ID> {

    @NotBlank(message = "The code should not be blank")
    @NotNull(message = "The code should not be null")
    @Column(name="code")
    protected String code;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecipeBaseEntity that = (RecipeBaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 1815580465;
    }
}
