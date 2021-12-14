package com.fromlabs.inventory.inventoryservice.ingredient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.inventoryservice.common.helper.FLStringUtils;
import com.fromlabs.inventory.inventoryservice.entity.IngredientBaseEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientPageRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.factory.IngredientEntityFactory;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryEntity;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType.DEFAULT;
import static com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType.EMPTY;
import static com.fromlabs.inventory.inventoryservice.ingredient.factory.IngredientEntityFactory.create;
import static java.time.Instant.*;
import static java.util.Objects.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Ingredient entity class
 */
@Entity
@Getter
@Setter
@ToString
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
    public IngredientEntity setParent(IngredientEntity parent){
        this.parent = parent;
        return this;
    }

    /**
     * Convert dto back to entity
     * @param dto   IngredientDto
     * @return  IngredientEntity
     */
    static public IngredientEntity from(IngredientDto dto) {
        var entity = new IngredientEntity();
        entity.setId(dto.getId());
        entity.setClientId(dto.getClientId());
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setUnit(dto.getUnit());
        entity.setUnitType(dto.getUnitType());
        entity.setChildren(nonNull(dto.getChildren()) ? dto.getChildren().stream().map(IngredientEntity::from).collect(Collectors.toList()) : new ArrayList<>()) ;
        entity.setCreateAt(dto.getCreateAt());
        entity.setUpdateAt(dto.getUpdateAt());
        return entity;
    }

    /**
     * Convert request to entity
     * @param request IngredientRequest
     * @return IngredientEntity
     */
    static public IngredientEntity from(IngredientRequest request) {
        var entity = create(DEFAULT);
        entity.setId(request.getId());
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(!FLStringUtils.isNullOrEmpty(request.getDescription()) ? request.getDescription() : request.getName());
        entity.setUnit(request.getUnit());
        entity.setUnitType(request.getUnitType());
        entity.setCategory(isNull(request.getParentId()));
        return entity;
    }

    /**
     * Covert to entity from page request
     * @param request   IngredientPageRequest
     * @return  IngredientEntity
     */
    static public IngredientEntity from(IngredientPageRequest request) {
        var entity = create(EMPTY);
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setUnit(request.getUnit());
        entity.setUnitType(request.getUnitType());
        entity.setCreateAt(request.getCreateAt());
        entity.setUpdateAt(request.getUpdateAt());
        entity.setCategory(isNull(request.getParentId()) || request.getParentId().equals(-1l));
        return entity;
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

    @Override
    public int hashCode() {
        return 49125696;
    }
}
