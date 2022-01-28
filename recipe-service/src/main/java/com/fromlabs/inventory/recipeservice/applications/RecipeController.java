package com.fromlabs.inventory.recipeservice.applications;

import com.fromlabs.inventory.recipeservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.recipeservice.common.template.WebTemplateProcessWithCheckBeforeAfter;
import com.fromlabs.inventory.recipeservice.config.ApiV1;
import com.fromlabs.inventory.recipeservice.detail.RecipeDetailService;
import com.fromlabs.inventory.recipeservice.detail.beans.request.RecipeDetailPageRequest;
import com.fromlabs.inventory.recipeservice.detail.beans.request.RecipeDetailRequest;
import com.fromlabs.inventory.recipeservice.recipe.RecipeService;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipePageRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipeRequest;
import com.fromlabs.inventory.recipeservice.recipe.mapper.RecipeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;

import static com.fromlabs.inventory.recipeservice.config.AppConfig.*;
import static com.fromlabs.inventory.recipeservice.utility.ControllerValidation.*;
import static com.fromlabs.inventory.recipeservice.utility.TemplateProcessDirector.*;
import static com.fromlabs.inventory.recipeservice.utility.TransactionConstraint.*;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API, produces = ApiV1.MIME_API)
public class RecipeController implements ApplicationController {

    //<editor-fold desc="SETUP">

    private final RecipeService recipeService;
    private final RecipeDetailService recipeDetailService;
    private final IngredientClient ingredientClient;
    public static final String SERVICE_PATH = "/recipe/";

    /**
     * Get full path and http method
     * @param method    HttpMethod
     * @param subPath   Sub path
     * @return          String
     */
    private String path(HttpMethod method, String subPath) {
        return method.name().concat(" ")
                .concat(InetAddress.getLoopbackAddress().getCanonicalHostName())
                .concat(SERVICE_PATH).concat(subPath);
    }

    /**
     * Constructor
     * @param recipeService         RecipeService
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     */
    public RecipeController(
            RecipeService recipeService,
            RecipeDetailService recipeDetailService,
            IngredientClient ingredientClient
    ) {
        this.recipeService = recipeService;
        this.recipeDetailService = recipeDetailService;
        this.ingredientClient = ingredientClient;
    }

    //</editor-fold>

    //<editor-fold desc="RECIPE">

    //<editor-fold desc="GROUP">

    /**
     * Get all recipe group as list
     * @param tenantId  Tenant ID
     * @return          ResponseEntity
     */
    @GetMapping("group/all")
    public ResponseEntity<?> getAllGroup(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        log.info(path(HttpMethod.GET, "group/all"));
        return (ResponseEntity<?>) buildGetAllRecipeGroupTemplateProcess(tenantId, recipeService).run();
    }

    /**
     * Get all recipe group with pagination
     * @param tenantId  Tenant ID
     * @param request   RecipePageRequest
     * @return          ResponseEntity
     */
    @PostMapping("group/page")
    public ResponseEntity<?> getGroupPage(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody RecipePageRequest request
    ) {
        log.info(path(HttpMethod.POST, "group/page"));
        return (ResponseEntity<?>) buildGetPageRecipeGroupTemplateProcess(tenantId, request, recipeService).run();
    }

    @GetMapping("group/simple")
    public ResponseEntity<?> getGroupLabelValue(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        log.info(path(HttpMethod.GET, "group/simple"));
        return (ResponseEntity<?>) buildGetLabelValueRecipeChildTemplateProcess(tenantId, recipeService).run();
    }

    //</editor-fold>

    //<editor-fold desc="CHILD">

    /**
     * Get all recipe child as list
     * @param tenantId  Tenant Id
     * @param parentId  Parent Id
     * @return          ResponseEntity
     */
    @GetMapping("child/all")
    public ResponseEntity<?> getAllChild(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam(PARENT_ID)  Long parentId
    ) {
        log.info(path(HttpMethod.GET, "child/all"));
        return (ResponseEntity<?>) buildGetAllRecipeChildTemplateProcess(tenantId, parentId, recipeService).run();
    }

    /**
     * Get recipe child with pagination
     * @param tenantId  Tenant ID
     * @param request   RecipePageRequest
     * @return          ResponseEntity
     */
    @PostMapping("child/page")
    public ResponseEntity<?> getChildPage(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody RecipePageRequest request
    ) {
        log.info(path(HttpMethod.POST, "child/page"));
        return (ResponseEntity<?>) buildGetPageRecipeChildTemplateProcess(tenantId, request, recipeService).run();
    }

    /**
     * Get all recipe child page
     * @param tenantId  Tenant ID
     * @param request   RecipePageRequest
     * @return          ResponseEntity
     */
    @PostMapping("child/page/all")
    public ResponseEntity<?> getAllChildPage(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody RecipePageRequest request
    ) {
        log.info(path(HttpMethod.POST, "child/page/all"));
        return (ResponseEntity<?>) buildGetPageAllRecipeChildTemplateProcess(tenantId, request, recipeService).run();
    }

    //</editor-fold>

    //<editor-fold desc="GENERAL">

    /**
     * Get recipe child and group by id
     * @param id    Entity ID
     * @return      ResponseEntity
     */
    @GetMapping("{id:\\d+}")
    public ResponseEntity<?> getById(
            @PathVariable(ID) Long id
    ) {
        log.info(path(HttpMethod.GET, "id"));
        return (ResponseEntity<?>) WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .validate(  () -> validateId(id))
                .process(   () -> ok(RecipeMapper.toDto(recipeService.getByIdWithException(id))))
                .build().run();
    }

    /**
     * Get recipe group and child by code
     * @param code  Recipe code
     * @return      ResponseEntity
     */
    @GetMapping("code")
    public ResponseEntity<?> getByCode(
            @RequestParam(CODE) String code
    ) {
        log.info(path(HttpMethod.GET, "code"));
        return (ResponseEntity<?>) buildGetRecipeByCodeTemplateProcess(code, recipeService).run();
    }

    /**
     * Save recipe group or child by request
     * @param tenantId  Tenant ID
     * @param request   RecipeRequest
     * @return          ResponseEntity
     */
    @PostMapping
    public ResponseEntity<?> save(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody RecipeRequest request
    ) {
        log.info(path(HttpMethod.POST, ""));
        return (ResponseEntity<?>) buildSaveRecipeTemplateProcess(tenantId, request, recipeService).run();
    }

    /**
     * Update recipe by request
     * @param tenantId  Tenant ID
     * @param request   Recipe Request
     * @return          ResponseEntity
     */
    @PutMapping
    public ResponseEntity<?> update(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody RecipeRequest request
    ) {
        log.info(path(HttpMethod.PUT, ""));
        return (ResponseEntity<?>) buildUpdateRecipeTemplateProcess(tenantId, request, recipeService).run();
    }

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="RECIPE DETAIL">

    /**
     * Get page of recipe detail with filter
     * @param tenantId  Client ID
     * @param request   RecipeDetailPageRequest
     * @return          ResponseEntity
     */
    @PostMapping("detail/page")
    public ResponseEntity<?> getDetailPage(
            @RequestHeader(TENANT_ID) Long       tenantId,
            @RequestBody RecipeDetailPageRequest request
    ) {
        log.info(path(HttpMethod.POST, "detail/page"));
        return (ResponseEntity<?>) buildGetPageRecipeDetailTemplateProcess(
                tenantId, request, recipeService, recipeDetailService, ingredientClient).run();
    }

    /**
     * Get list of recipe detail with tenantId
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    @GetMapping("detail/all")
    public ResponseEntity<?> getDetailAll(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        log.info(path(HttpMethod.GET, "detail/all"));
        return (ResponseEntity<?>) buildGetAllRecipeDetailTemplateProcess(tenantId, recipeDetailService, ingredientClient).run();
    }

    /**
     * Get list of recipe detail with tenantId and recipe
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    @GetMapping("detail/get-with-recipe")
    public ResponseEntity<?> getDetailAll(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam("recipeId") Long recipeId
    ) {
        log.info(path(HttpMethod.GET, "detail/get-with-recipe"));
        return (ResponseEntity<?>) buildGetRecipeDetailByRecipeTemplateProcess(
                tenantId, recipeId, recipeService, recipeDetailService, ingredientClient).run();
    }

    /**
     * Get recipe detail by id
     * @param id    Entity ID
     * @return      ResponseEntity
     */
    @GetMapping("detail/{id:\\d+}")
    public ResponseEntity<?> getDetailById(
            @PathVariable(ID) Long id
    ) {
        log.info(path(HttpMethod.GET, "detail/".concat(String.valueOf(id))));
        return (ResponseEntity<?>) buildGetRecipeDetailByIdTemplateProcess(id, recipeDetailService, ingredientClient).run();
    }

    /**
     * Get recipe detail by code
     * @param code  Recipe detail code
     * @return      ResponseEntity
     */
    @GetMapping("detail/code")
    public ResponseEntity<?> getDetailByCode(
            @RequestParam(CODE) String code
    ) {
        log.info(path(HttpMethod.GET, "detail/code"));
        return (ResponseEntity<?>) buildGetRecipeDetailByCodeTemplateProcess(code, recipeDetailService, ingredientClient).run();
    }

    /**
     * Save recipe detail by request
     * @param tenantId  Tenant ID
     * @param request   RecipeDetailRequest
     * @return          ResponseEntity
     */
    @PostMapping("detail")
    public ResponseEntity<?> saveDetail(
            @RequestHeader(TENANT_ID) Long      tenantId,
            @RequestBody RecipeDetailRequest request
    ) {
        log.info(path(HttpMethod.POST, "detail"));
        return (ResponseEntity<?>) buildSaveRecipeDetailTemplateProcess(
                tenantId, request, recipeService, recipeDetailService, ingredientClient).run();
    }

    /**
     * Update recipe detail by request
     * @param tenantId  Tenant ID
     * @param request   Recipe Request
     * @return          ResponseEntity
     */
    @PutMapping("detail")
    public ResponseEntity<?> updateDetail(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody RecipeDetailRequest request
    ) {
        log.info(path(HttpMethod.PUT, "detail"));
        return (ResponseEntity<?>) buildUpdateRecipeDetailTemplateProcess(tenantId, request, recipeDetailService, ingredientClient).run();
    }

    //</editor-fold>
}
