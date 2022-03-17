package com.fromlabs.inventory.recipeservice.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fromlabs.inventory.recipeservice.entity.RecipeBaseEntity;
import com.fromlabs.inventory.recipeservice.media.entity.MediaEntity;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipePageRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipeRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

/**
 * Recipe Entity
 * @author Liem
 */
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

    @Column(name = "media_id")
    protected Long mediaId;

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

    public static boolean checkIsGroupFromPageRequest(RecipePageRequest request) {
        if(Objects.nonNull(request.getGroup())) {
            return request.getGroup();
        } else {
            return Objects.isNull(request.getParentId()) || request.getParentId() < 0;
        }

    }

    public RecipeEntity update(RecipeRequest request) {
        this.setName(request.getName());
        this.setCode(request.getCode());
        this.setDescription(request.getDescription());
        this.setUpdateAt(request.getCreatedAt());
        return this;
    }

    public RecipeEntity setParent(RecipeEntity parent) {
        if(nonNull(parent)) this.parent = parent;
        return this;
    }
}
