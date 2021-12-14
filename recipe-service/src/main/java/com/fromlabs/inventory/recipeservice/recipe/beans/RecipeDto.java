package com.fromlabs.inventory.recipeservice.recipe.beans;

import com.fromlabs.inventory.recipeservice.common.dto.BaseDto;
import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeDto extends BaseDto<Long> implements Serializable {

    protected List<RecipeDto> children;
    protected String code;

    @Builder(builderMethodName = "recipeBuilder")
    public RecipeDto(Long id, Long tenantId, String name, String description, String updateAt, String createAt, boolean activated,
                     String code, List<RecipeDto> children) {
        super(id, tenantId, name, description, updateAt, createAt, activated);
        this.setCode(code);
        this.setChildren(children);
    }

    public RecipeDto() {
        super();
    }

    static public RecipeDto from(RecipeEntity entity) {
        var builder = RecipeDto.recipeBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .createAt(entity.getUpdateAt())
                .updateAt(entity.getUpdateAt())
                .activated(entity.isActivated());
        return Objects.nonNull(entity.getParent()) ? builder.build() :
                builder.children(from(entity.getChildren())).build();
    }

    static public List<RecipeDto> from(List<RecipeEntity> entities) {
        return entities.stream().map(RecipeDto::from).collect(Collectors.toList());
    }

    static public Page<RecipeDto> from(Page<RecipeEntity> entities) {
        return entities.map(RecipeDto::from);
    }
}
