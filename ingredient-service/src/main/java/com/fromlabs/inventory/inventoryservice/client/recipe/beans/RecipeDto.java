package com.fromlabs.inventory.inventoryservice.client.recipe.beans;

import com.fromlabs.inventory.inventoryservice.common.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeDto extends BaseDto<Long> {
    protected List<RecipeDto> children;
}
