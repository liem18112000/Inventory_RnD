package com.fromlabs.inventory.recipeservice.client.ingredient.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Ingredient Data Transfer Object
 */
@Builder(toBuilder = true)
@Data
@JsonIgnoreProperties({"createAt", "updateAt", "clientId"})
public class IngredientDto implements Serializable {
    private Long id;
    private Long clientId;
    private String name;
    private String code;
    private String description;
    private String unit;
    private String unitType;
    private String createAt;
    private String updateAt;
}
