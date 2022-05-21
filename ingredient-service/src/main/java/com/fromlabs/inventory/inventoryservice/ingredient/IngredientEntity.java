package com.fromlabs.inventory.inventoryservice.ingredient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fromlabs.inventory.inventoryservice.entity.IngredientBaseEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.Instant.*;

/**
 * Ingredient entity class
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name="ingredient")
public class IngredientEntity extends IngredientBaseEntity<Long> {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private IngredientEntity parent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<IngredientEntity> children = new ArrayList<>();

    @Column(name="category")
    private boolean category;

    /**
     * Chain set parent
     * @param parent IngredientEntity
     * @return IngredientEntity
     */
    public IngredientEntity setParent(
            @NotNull final IngredientEntity parent
    ){
        this.parent = parent;
        return this;
    }

    /**
     * Update entity from req
     * @param request IngredientRequest
     * @return IngredientEntity
     */
    public IngredientEntity update(IngredientRequest request) {
        this.setName(request.getName());
        this.setCode(request.getCode());
        this.setDescription(request.getDescription());
        this.setUpdateAt(now().toString());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IngredientEntity that = (IngredientEntity) o;
        return Objects.equals(id, that.id);
    }
}
