/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.client.recipe;

import com.fromlabs.inventory.supplierservice.client.recipe.beans.RecipeDetailDto;
import com.fromlabs.inventory.supplierservice.client.recipe.beans.RecipeDto;
import com.fromlabs.inventory.supplierservice.config.ApiV1;
import io.sentry.spring.tracing.SentryTransaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fromlabs.inventory.supplierservice.config.AppConfig.*;

/**
 * Recipe client to recipe module
 */
@SentryTransaction(operation = "supplier-recipe-client")
@FeignClient(value = "${services.recipe-service.name}")
@RequestMapping(value = "recipe/" + ApiV1.URI_API)
public interface RecipeClient {

    /**
     * Get recipe child and group by id
     * @param id    Entity ID
     * @return      ResponseEntity
     */
    @GetMapping("{id:\\d+}")
    RecipeDto getById(
            @PathVariable(ID) Long id
    );

    /**
     * Get recipe group and child by code
     * @param code  Recipe code
     * @return      ResponseEntity
     */
    @GetMapping("code")
    RecipeDto getByCode(
            @RequestParam(CODE) String code
    );

    /**
     * Get all recipe group as list
     * @param tenantId  Tenant ID
     * @return          ResponseEntity
     */
    @GetMapping("group/all")
    List<RecipeDto> getAllGroup(
            @RequestHeader(TENANT_ID) Long tenantId
    );

    /**
     * Get all recipe child as list
     * @param tenantId  Tenant Id
     * @param parentId  Parent Id
     * @return          ResponseEntity
     */
    @GetMapping("child/all")
    List<RecipeDto> getAllChild(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam(PARENT_ID) Long parentId
    );

    /**
     * Get list of recipe detail with tenantId
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    @GetMapping("detail/all")
    List<RecipeDetailDto> getDetailAll(
            @RequestHeader(TENANT_ID) Long tenantId
    );

    /**
     * Get list of recipe detail with tenantId and recipe
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    @GetMapping("detail/get-with-recipe")
    List<RecipeDetailDto> getDetailAll(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam("recipeId") Long recipeId
    );

    /**
     * Get recipe detail by id
     * @param id    Entity ID
     * @return      ResponseEntity
     */
    @GetMapping("detail/{id:\\d+}")
    RecipeDetailDto getDetailById(
            @PathVariable(ID) Long id
    );

    /**
     * Get recipe detail by code
     * @param code  Recipe detail code
     * @return      ResponseEntity
     */
    @GetMapping("detail/code")
    RecipeDetailDto getDetailByCode(
            @RequestParam(CODE) String code
    );
}
