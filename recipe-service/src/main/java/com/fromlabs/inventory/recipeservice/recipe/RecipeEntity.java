package com.fromlabs.inventory.recipeservice.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fromlabs.inventory.recipeservice.entity.RecipeBaseEntity;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipePageRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipeRequest;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fromlabs.inventory.recipeservice.common.factory.FactoryCreateType.EMPTY;
import static com.fromlabs.inventory.recipeservice.recipe.factory.RecipeEntityFactory.create;
import static java.util.Objects.nonNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="recipe")
public class RecipeEntity extends RecipeBaseEntity<Long> {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private RecipeEntity parent = null;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<RecipeEntity> children = new ArrayList<>();

    @NotNull(message = "Group boolean flag should not be null")
    @Column(name="is_group")
    protected boolean group;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecipeEntity that = (RecipeEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 738380931;
    }

    public static RecipeEntity from(RecipePageRequest request) {
        var entity = create(EMPTY);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setGroup(checkIsGroupFromPageRequest(request));
        return entity;
    }

    private static boolean checkIsGroupFromPageRequest(RecipePageRequest request) {
        if(Objects.nonNull(request.getGroup())) {
            return request.getGroup();
        } else {
            return Objects.isNull(request.getParentId()) || request.getParentId() < 0;
        }

    }

    public static RecipeEntity from(RecipeRequest request) {
        var entity = create(EMPTY);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setGroup(Objects.isNull(request.getParentId()));
        entity.setUpdateAt(request.getCreatedAt());
        return entity;
    }

    public static RecipeEntity update(RecipeRequest request, RecipeEntity entity) {
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setUpdateAt(request.getCreatedAt());
        return entity;
    }

    public RecipeEntity setParent(RecipeEntity parent) {
        if(nonNull(parent)) this.parent = parent;
        return this;
    }
}
