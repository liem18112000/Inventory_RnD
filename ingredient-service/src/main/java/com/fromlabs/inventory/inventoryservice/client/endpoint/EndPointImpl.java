/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.client.endpoint;

import com.fromlabs.inventory.inventoryservice.config.ApiV1;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.mapper.IngredientMapper;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.utility.TransactionLogic;
import io.sentry.spring.tracing.SentryTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.inventoryservice.config.AppConfig.*;

/**
 * Ingredient endpoint implementation for internally expose API to other service
 * @author Liem
 */
@SentryTransaction(operation = "ingredient-endpoint")
@Slf4j
@RestController
@RequestMapping(value = "endpoint/ingredient/" + ApiV1.URI_API)
public class EndPointImpl implements EndPoint {

    private final IngredientService ingredientService;
    private final InventoryService  inventoryService;

    /**
     * Constructor
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     */
    public EndPointImpl(
            IngredientService ingredientService,
            InventoryService inventoryService
    ) {
        log.info("Endpoint initialized : {}", "endpoint/ingredient/" + ApiV1.URI_API);
        this.inventoryService = inventoryService;
        this.ingredientService = ingredientService;
    }

    public static final String SERVICE_PATH = "/endpoint/ingredient/" + ApiV1.URI_API + "/";

    /**
     * Log out the path for endpoint request
     * @param method    HttpMethod
     * @param subPath   Endpoint sub path
     * @return          String
     */
    private String path(HttpMethod method, String subPath) {
        return TransactionLogic.path(method, SERVICE_PATH, subPath, ApiV1.VERSION);
    }

    /**
     * Return all ingredient category (ingredient parent) as a list
     *
     * @param tenantId  The client id (must be provided)
     * @return          List&lt;IngredientDto&gt;
     */
    @Override
    @GetMapping("category/all")
    public List<IngredientDto> getAllIngredientCategory(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        log.info(path(HttpMethod.GET, "category/all"));
        return IngredientMapper.toDto(ingredientService.getAll(tenantId));
    }

    /**
     * Return a ResponseEntity object of all ingredient type (ingredient child) as a list
     *
     * @param tenantId  The client id (must be provided)
     * @param parentId  The id of a parent category ingredient (must be provided)
     * @return          List&lt;IngredientDto&gt;
     */
    @Override
    @GetMapping("type/all")
    public List<IngredientDto> getAllIngredientType(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam(PARENT_ID) Long parentId
    ) {
        log.info(path(HttpMethod.GET, "type/all"));
        return  IngredientMapper.toDto(ingredientService.getAll(tenantId, parentId)).stream()
                .map(ingredient -> TransactionLogic.setIngredientQuantity(inventoryService, ingredient))
                .collect(Collectors.toList());
    }

    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with code
     *
     * @param tenantId  The client id (must be provided)
     * @param code      The unique code for identify a ingredient
     * @return          IngredientEntity
     */
    @Override
    @GetMapping("code")
    public IngredientDto getIngredient(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam(CODE) String code
    ) {
        log.info(path(HttpMethod.GET, "code"));
        return TransactionLogic.getIngredientByCodeWithQuantity(code, ingredientService, inventoryService);
    }

    // TODO: Weird error on recipe client on got POST
    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with ID
     *
     * @param tenantId The client id (must be provided)
     * @param id       The ID (unique) of an ingredient
     * @return          IngredientEntity
     */
    @Override
    @RequestMapping(value = "{id:\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public IngredientDto getIngredientById(
            @RequestHeader(TENANT_ID) Long tenantId,
            @PathVariable(ID) Long id
    ) {
        log.info(path(HttpMethod.GET, String.valueOf(id)));
        return TransactionLogic.getIngredientByIdWithQuantity(id, ingredientService, inventoryService);
    }
}
