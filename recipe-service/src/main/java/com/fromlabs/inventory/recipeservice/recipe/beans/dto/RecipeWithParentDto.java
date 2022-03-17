package com.fromlabs.inventory.recipeservice.recipe.beans.dto;

import com.fromlabs.inventory.recipeservice.common.dto.SimpleDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeWithParentDto extends RecipeDto {

    protected SimpleDto parent;

    @Builder(builderMethodName = "recipeWithParentBuilder")
    public RecipeWithParentDto(Long id, Long tenantId, String name, String description, String updateAt,
                               String createAt, boolean activated, Long media, String code,
                               List<RecipeDto> children, SimpleDto parent) {
        super(id, tenantId, name, description, updateAt, createAt, activated, media, code, children);
        this.parent = parent;
    }

    public RecipeWithParentDto() {
    }
}
