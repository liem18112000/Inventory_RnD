package com.fromlabs.inventory.notificationservice.client.recipe.beans;

import com.fromlabs.inventory.notificationservice.common.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeDto extends BaseDto<Long> {
    protected List<RecipeDto> children;
}
