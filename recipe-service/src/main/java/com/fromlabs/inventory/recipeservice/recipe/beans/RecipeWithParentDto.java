package com.fromlabs.inventory.recipeservice.recipe.beans;

import com.fromlabs.inventory.recipeservice.common.dto.SimpleDto;
import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeWithParentDto extends RecipeDto {

    protected SimpleDto parent;

    @Builder(builderMethodName = "recipeWithParentBuilder")
    public RecipeWithParentDto(Long id, Long tenantId, String name, String description, String updateAt,
                               String createAt, boolean activated, String code, List<RecipeDto> children,
                               SimpleDto parent) {
        super(id, tenantId, name, description, updateAt, createAt, activated, code, children);
        this.parent = parent;
    }

    public RecipeWithParentDto() {
    }

    static public RecipeWithParentDto fromWithParent(RecipeEntity entity) {
        final var parent = entity.getParent();
        return RecipeWithParentDto.recipeWithParentBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .createAt(entity.getUpdateAt())
                .updateAt(entity.getUpdateAt())
                .activated(entity.isActivated())
                .children(nonNull(entity.getChildren()) ? from(entity.getChildren()) : null)
                .parent(nonNull(parent) ? SimpleDto.builder().label(parent.getName()).value(parent.getId()).build() : null)
                .build();
    }

    static public List<RecipeWithParentDto> fromWithParent(List<RecipeEntity> entities) {
        return entities.stream().map(RecipeWithParentDto::fromWithParent).collect(Collectors.toList());
    }

    static public Page<RecipeWithParentDto> fromWithParent(Page<RecipeEntity> entities) {
        return entities.map(RecipeWithParentDto::fromWithParent);
    }
}
