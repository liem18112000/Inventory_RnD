/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.recipeservice.client.endpoint;

import com.fromlabs.inventory.recipeservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.recipeservice.config.ApiV1;
import com.fromlabs.inventory.recipeservice.detail.RecipeDetailService;
import com.fromlabs.inventory.recipeservice.detail.beans.dto.RecipeDetailDto;
import com.fromlabs.inventory.recipeservice.detail.mapper.RecipeDetailMapper;
import com.fromlabs.inventory.recipeservice.recipe.RecipeService;
import com.fromlabs.inventory.recipeservice.recipe.beans.dto.RecipeDto;
import com.fromlabs.inventory.recipeservice.recipe.mapper.RecipeMapper;
import com.fromlabs.inventory.recipeservice.utility.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fromlabs.inventory.recipeservice.config.AppConfig.*;
import static com.fromlabs.inventory.recipeservice.utility.TransactionLogic.*;

/**
 * Ingredient endpoint implementation for internally expose API to other service
 */
@Slf4j
@RestController
@RequestMapping(value = "endpoint/recipe/" + ApiV1.URI_API)
public class EndPointImpl implements EndPoint {

    private final RecipeService         recipeService;
    private final RecipeDetailService   recipeDetailService;
    private final IngredientClient      ingredientClient;

    /**
     * Constructor
     * @param recipeService         RecipeService
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     */
    public EndPointImpl(
            RecipeService recipeService,
            RecipeDetailService recipeDetailService,
            IngredientClient ingredientClient
    ) {
        log.info("Endpoint initialized : {}", "endpoint/recipe/" + ApiV1.URI_API);
        this.recipeService = recipeService;
        this.recipeDetailService = recipeDetailService;
        this.ingredientClient = ingredientClient;
    }

    public static final String SERVICE_PATH = "/endpoint/recipe/" + ApiV1.URI_API + "/";

    private String path(HttpMethod method, String subPath) {
        return TransactionLogic.path(method, SERVICE_PATH, subPath, ApiV1.VERSION);
    }

    /**
     * Get recipe child and group by id
     * @param id    Entity ID
     * @return      ResponseEntity
     */
    @GetMapping("{id:\\d+}")
    public RecipeDto getById(
            @PathVariable(ID) Long id
    ) {
        log.info(path(HttpMethod.GET, String.valueOf(id)));
        return RecipeMapper.toDto(this.recipeService.getById(id));
    }

    /**
     * Get recipe group and child by code
     * @param code  Recipe code
     * @return      ResponseEntity
     */
    @GetMapping("code")
    public RecipeDto getByCode(
            @RequestParam(CODE) String code
    ) {
        log.info(path(HttpMethod.GET, "code"));
        return RecipeMapper.toDto(this.recipeService.getByCode(code));
    }

    /**
     * Get all recipe group as list
     * @param tenantId  Tenant ID
     * @return          ResponseEntity
     */
    @GetMapping("group/all")
    public List<RecipeDto> getAllGroup(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        log.info(path(HttpMethod.GET, "group/all"));
        return RecipeMapper.toDto(this.recipeService.getAll(tenantId));
    }

    /**
     * Get all recipe child as list
     * @param tenantId  Tenant Id
     * @param parentId  Parent Id
     * @return          ResponseEntity
     */
    @GetMapping("child/all")
    public List<RecipeDto> getAllChild(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam(PARENT_ID) Long parentId
    ){
        log.info(path(HttpMethod.GET, "child/all"));
        return RecipeMapper.toDto(this.recipeService.getAll(tenantId, parentId));
    }

    /**
     * Get list of recipe detail with tenantId
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    @GetMapping("detail/all")
    public List<RecipeDetailDto> getDetailAll(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        log.info(path(HttpMethod.GET, "detail/all"));
        return getAllRecipeDetail(tenantId, recipeDetailService, ingredientClient);
    }

    /**
     * Get list of recipe detail with tenantId and recipe
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    @GetMapping("detail/get-with-recipe")
    public List<RecipeDetailDto> getDetailAll(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam("recipeId") Long recipeId
    ) {
        log.info(path(HttpMethod.GET, "detail/get-with-recipe"));
        return getAllRecipeDetailWithRecipe(tenantId, recipeId, recipeService, recipeDetailService, ingredientClient);
    }

    /**
     * Get recipe detail by id
     * @param id    Entity ID
     * @return      ResponseEntity
     */
    @GetMapping("detail/{id:\\d+}")
    public RecipeDetailDto getDetailById(
            @PathVariable(ID) Long id
    ) {
        log.info(path(HttpMethod.GET, "detail/".concat(String.valueOf(id))));
        return RecipeDetailMapper.toDto(recipeDetailService.getById(id), ingredientClient);
    }

    /**
     * Get recipe detail by code
     * @param code  Recipe detail code
     * @return      ResponseEntity
     */
    @GetMapping("detail/code")
    public RecipeDetailDto getDetailByCode(
            @RequestParam(CODE) String code
    ) {
        log.info(path(HttpMethod.GET, "detail/code"));
        return RecipeDetailMapper.toDto(recipeDetailService.getByCode(code), ingredientClient);
    }
}
