/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.client.recipe.beans.RecipeDetailDto;
import com.fromlabs.inventory.inventoryservice.client.recipe.beans.RecipeDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <h1>Suggest taxon response beans</h1>
 *
 * <h2>Brief Information</h2>
 * <p>Suggest Response beans is the data transfer object for the final result
 * of the Suggest Taxon transaction (see in RestaurantInventoryDomainService)</p>
 *
 * <h2>Properties</h2>
 * <p>Required properties which show the result are : </p>
 * <ul>
 *     <li>RecipeDto : Data Transfer Object of recipe child (children of a recipe group)</li>
 *     <li>Details : Collection of recipe details</li>
 *     <li>TaxonQuantity : the quantity (a whole number) of suggested product</li>
 * </ul>
 * @see RecipeDto
 * @see RecipeDetailDto
 * @see Builder
 */
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuggestResponse {
    private RecipeDto recipe;
    private List<RecipeDetailDto> details;
    private int taxonQuantity;
}
