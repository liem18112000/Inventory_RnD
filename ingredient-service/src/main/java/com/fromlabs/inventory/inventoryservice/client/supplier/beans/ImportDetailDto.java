package com.fromlabs.inventory.inventoryservice.client.supplier.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fromlabs.inventory.inventoryservice.common.dto.BaseDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Import detail DTO
 * @author Liem
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({"active", "accessAt"})
public class ImportDetailDto extends BaseDto<Long> implements Serializable {
    private static final long serialVersionUID = 4907163004009834931L;

    private IngredientDto ingredient;

    private ImportDto imports;

    private float quantity;

    public ImportDetailDto() {
    }
}
