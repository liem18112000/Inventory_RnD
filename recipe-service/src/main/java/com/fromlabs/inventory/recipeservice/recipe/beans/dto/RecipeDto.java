package com.fromlabs.inventory.recipeservice.recipe.beans.dto;

import com.fromlabs.inventory.recipeservice.common.dto.BaseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Recipe Data Transfer Object
 * @author Liem
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeDto extends BaseDto<Long> implements Serializable {

    protected List<RecipeDto> children;
    protected String code;

    @Builder(builderMethodName = "recipeBuilder")
    public RecipeDto(Long id, Long tenantId, String name, String description, String updateAt,
                     String createAt, boolean activated,
                     String code, List<RecipeDto> children) {
        super(id, tenantId, name, description, updateAt, createAt, activated);
        this.setCode(code);
        this.setChildren(children);
    }

    public RecipeDto() {
        super();
    }
}
