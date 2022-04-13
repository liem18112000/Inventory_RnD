package com.fromlabs.inventory.recipeservice.client.ingredient;

import com.fromlabs.inventory.recipeservice.client.ingredient.beans.IngredientDto;
import com.fromlabs.inventory.recipeservice.config.ApiV1;
import io.sentry.spring.tracing.SentryTransaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fromlabs.inventory.recipeservice.config.AppConfig.*;

/**
 * Ingredient client
 */
@SentryTransaction(operation = "recipe-ingredient-client")
@FeignClient(value = "${services.ingredient-service.name}")
@RequestMapping(value = "endpoint/ingredient/" + ApiV1.URI_API)
public interface IngredientClient {

    /**
     * Return all ingredient category (ingredient parent) as a list
     * @param tenantId  The client id (must be provided)
     * @return          List&lt;IngredientDto&gt;
     */
    @GetMapping("category/all")
    List<IngredientDto> getAllIngredientCategory(
            @RequestHeader(TENANT_ID) Long tenantId
    );

    /**
     * Return a ResponseEntity object of all ingredient type (ingredient child) as a list
     * @param tenantId  The client id (must be provided)
     * @param parentId  The id of a parent category ingredient (must be provided)
     * @return          List&lt;IngredientDto&gt;
     */
    @GetMapping("type/all")
    List<IngredientDto> getAllIngredientType(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam(PARENT_ID) Long parentId
    );

    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with code
     * @param tenantId  The client id (must be provided)
     * @param code      The unique code for identify a ingredient
     * @return          IngredientDto
     */
    @GetMapping("code")
    IngredientDto getIngredient(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam(CODE) String code
    );

    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with ID
     * @param tenantId  The client id (must be provided)
     * @param id        The ID (unique) of an ingredient
     * @return          IngredientDto
     */
    @GetMapping("{id:\\d+}")
    IngredientDto getIngredientById(
            @RequestHeader(TENANT_ID) Long tenantId,
            @PathVariable(ID) Long id
    );
}
