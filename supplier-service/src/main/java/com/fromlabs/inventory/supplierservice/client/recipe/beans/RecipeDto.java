package com.fromlabs.inventory.supplierservice.client.recipe.beans;

import com.fromlabs.inventory.supplierservice.common.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeDto extends BaseDto<Long> {
    protected List<RecipeDto> children;
}
