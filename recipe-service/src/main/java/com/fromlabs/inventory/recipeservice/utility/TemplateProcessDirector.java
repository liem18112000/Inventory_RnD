package com.fromlabs.inventory.recipeservice.utility;

import com.fromlabs.inventory.recipeservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.recipeservice.common.template.*;
import com.fromlabs.inventory.recipeservice.detail.RecipeDetailService;
import com.fromlabs.inventory.recipeservice.detail.beans.*;
import com.fromlabs.inventory.recipeservice.recipe.RecipeService;
import com.fromlabs.inventory.recipeservice.recipe.beans.*;
import lombok.experimental.UtilityClass;

import static com.fromlabs.inventory.recipeservice.common.validator.RequestValidator.StringRequestValidator;
import static com.fromlabs.inventory.recipeservice.config.AppConfig.*;
import static com.fromlabs.inventory.recipeservice.utility.ControllerValidation.*;
import static com.fromlabs.inventory.recipeservice.utility.RequestBootstrap.*;
import static com.fromlabs.inventory.recipeservice.utility.TransactionConstraint.*;
import static com.fromlabs.inventory.recipeservice.utility.TransactionLogic.*;
import static org.springframework.http.ResponseEntity.*;

/**
 * <h1>Template process builder director</h1>
 */
@UtilityClass
public class TemplateProcessDirector {

    //<editor-fold desc="Build get all recipe group template process">

    /**
     * Build get all recipe group template process
     * @param tenantId      Tenant ID
     * @param recipeService RecipeService
     * @return              TemplateProcess
     */
    public TemplateProcess buildGetAllRecipeGroupTemplateProcess(
            Long            tenantId,
            RecipeService   recipeService
    ) {
        return WebTemplateProcess.builder()
                .validate(() -> validateTenant(tenantId))
                .process(() -> ok(getRecipeGroupList(tenantId, recipeService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get page request group template process">

    /**
     * Build get page request group template process
     * @param tenantId      Tenant ID
     * @param request       RecipePageRequest
     * @param recipeService RecipeService
     * @return              TemplateProcess
     */
    public TemplateProcess buildGetPageRecipeGroupTemplateProcess(
            Long                tenantId,
            RecipePageRequest   request,
            RecipeService       recipeService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> pageBootstrap(tenantId, request))
                .validate(  () -> validateTenant(request.getTenantId()))
                .process(   () -> ok(getRecipePageWithFilter(request, recipeService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get page recipe page with filter template process">

    /**
     * Build get page recipe page with filter template process
     * @param tenantId      Tenant ID
     * @param request       RecipePageRequest
     * @param recipeService RecipeService
     * @return              TemplateProcess
     */
    public TemplateProcess buildGetPageRecipeChildTemplateProcess(
            Long                tenantId,
            RecipePageRequest   request,
            RecipeService       recipeService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> pageBootstrap(tenantId, request))
                .validate(  () -> validateGetPageChild(tenantId, request))
                .process(   () -> ok(getRecipePageWithFilter(request, recipeService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get page all recipe child template process">

    public TemplateProcess buildGetPageAllRecipeChildTemplateProcess(
            Long                tenantId,
            RecipePageRequest   request,
            RecipeService       recipeService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapGetAllRecipeChildPage(tenantId, request))
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(getRecipePageWithFilter(request, recipeService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get all recipe child template process">

    /**
     * Build get all recipe child template process
     * @param tenantId      Tenant ID
     * @param parentId      Recipe group id
     * @param recipeService RecipeService
     * @return              TemplateProcess
     */
    public TemplateProcess buildGetAllRecipeChildTemplateProcess(
            Long            tenantId,
            Long            parentId,
            RecipeService   recipeService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenantAndParentId(tenantId, parentId))
                .process(   () -> ok(getAllRecipeChild(tenantId, parentId, recipeService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get recipe by code template process">

    /**
     * Build get recipe by code template process
     * @param code          Recipe unique code
     * @param recipeService RecipeService
     * @return              TemplateProcess
     */
    public TemplateProcess buildGetRecipeByCodeTemplateProcess(
            String          code,
            RecipeService   recipeService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .validate(  () -> StringRequestValidator().criteriaRequired(CODE, code).validate())
                .before(    () -> isRecipeExistByCode(code, recipeService))
                .process(   () -> ok(getRecipeByCode(code, recipeService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build save recipe template process">

    /**
     * Build save recipe template process
     * @param tenantId      Tenant ID
     * @param request       RecipeRequest
     * @param recipeService RecipeService
     * @return              TemplateProcess
     */
    public TemplateProcess buildSaveRecipeTemplateProcess(
            Long            tenantId,
            RecipeRequest   request,
            RecipeService   recipeService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> recipeRequestBootstrap(tenantId, request))
                .validate(  () -> validateRecipeRequest(request, false))
                .before(    () -> checkConstrainsBeforeSaveRecipe(request, recipeService))
                .process(   () -> ok(saveRecipeEntity(request, recipeService)))
                .after(     () -> isRecipeExistByCode(request.getCode(), recipeService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build update recipe template process">

    /**
     * Build update recipe template process
     * @param tenantId      Tenant ID
     * @param request       RecipeRequest
     * @param recipeService RecipeService
     * @return              TemplateProcess
     */
    public TemplateProcess buildUpdateRecipeTemplateProcess(
            Long            tenantId,
            RecipeRequest   request,
            RecipeService   recipeService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> recipeRequestBootstrap(tenantId, request))
                .validate(  () -> validateRecipeRequest(request, true))
                .before(    () -> checkConstrainsBeforeUpdateRecipe(request, recipeService))
                .process(   () -> ok(updateRecipeEntity(request, recipeService)))
                .after(     () -> checkConstrainsAfterUpdateRecipe(request, recipeService))
                .build();
    }

    //</editor-fold>


    //<editor-fold desc="Build get page recipe detail template process">

    /**
     * Build get page recipe detail template process
     * @param tenantId              Tenant ID
     * @param request               RecipeDetailPageRequest
     * @param recipeService         RecipeService
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      TemplateProcess
     */
    public TemplateProcess buildGetPageRecipeDetailTemplateProcess(
            Long                    tenantId,
            RecipeDetailPageRequest request,
            RecipeService           recipeService,
            RecipeDetailService     recipeDetailService,
            IngredientClient        ingredientClient
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> pageBootstrap(tenantId, request))
                .validate(  () -> validateGetPageRecipeDetail(request))
                .process(   () -> ok(getRecipeDetailPageWithFilter(request, recipeService, recipeDetailService, ingredientClient)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get all recipe detail template process">

    /**
     * Build get all recipe detail template process
     * @param tenantId              Tenant ID
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      TemplateProcess
     */
    public TemplateProcess buildGetAllRecipeDetailTemplateProcess(
            Long                tenantId,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(getAllRecipeDetail(tenantId, recipeDetailService, ingredientClient)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get recipe detail by recipe template process">

    /**
     * Build get recipe detail by recipe template process
     * @param tenantId              Tenant ID
     * @param recipeId              Recipe ID
     * @param recipeService         RecipeService
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      TemplateProcess
     */
    public TemplateProcess buildGetRecipeDetailByRecipeTemplateProcess(
            Long                tenantId,
            Long                recipeId,
            RecipeService       recipeService,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .validate(  () -> validateTenant(tenantId))
                .before(    () -> isRecipeExistById(recipeId, recipeService))
                .process(   () -> ok(getAllRecipeDetailWithRecipe(tenantId, recipeId, recipeService, recipeDetailService, ingredientClient)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get reci[e detail by id template process">

    /**
     * Build get reci[e detail by id template process
     * @param id                    Recipe Detail ID
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      TemplateProcess
     */
    public TemplateProcess buildGetRecipeDetailByIdTemplateProcess(
            Long                id,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .validate(  () -> validateId(id))
                .before(    () -> isRecipeDetailExistById(id, recipeDetailService))
                .process(   () -> ok(getRecipeDetailById(id, recipeDetailService, ingredientClient)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get recipe detail by code template process">

    /**
     * Build get recipe detail by code template process
     * @param code                  Recipe Detail Code
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      TemplateProcess
     */
    public TemplateProcess buildGetRecipeDetailByCodeTemplateProcess(
            String              code,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .validate(  () -> StringRequestValidator().criteriaRequired(CODE, code).validate())
                .before(    () -> isRecipeDetailExistByCode(code, recipeDetailService))
                .process(   () -> ok(getRecipeDetailByCode(code, recipeDetailService, ingredientClient)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build save recipe detail template process">

    /**
     * Build save recipe detail template process
     * @param tenantId              Tenant ID
     * @param request               RecipeDetailRequest
     * @param recipeService         RecipeService
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      TemplateProcess
     */
    public TemplateProcess buildSaveRecipeDetailTemplateProcess(
            Long                tenantId,
            RecipeDetailRequest request,
            RecipeService       recipeService,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> recipeDetailRequestBootstrap(tenantId, request))
                .validate(  () -> validateRecipeDetailRequest(request, false))
                .before(    () -> checkConstrainsBeforeSaveRecipeDetail(request, recipeDetailService, ingredientClient))
                .process(   () -> ok(saveRecipeDetailEntity(request, recipeService, recipeDetailService, ingredientClient)))
                .after(     () -> isRecipeDetailExistByCode(request.getCode(), recipeDetailService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build update recipe detail template process">

    /**
     * Build update recipe detail template process
     * @param tenantId              Tenant ID
     * @param request               RecipeDetailRequest
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      TemplateProcess
     */
    public TemplateProcess buildUpdateRecipeDetailTemplateProcess(
            Long                tenantId,
            RecipeDetailRequest request,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> recipeDetailRequestBootstrap(tenantId, request))
                .validate(  () -> validateRecipeDetailRequest(request, true))
                .before(    () -> checkConstrainsBeforeUpdateRecipeDetail(request, recipeDetailService))
                .process(   () -> ok(updateRecipeEntity(request, recipeDetailService, ingredientClient)))
                .after(     () -> checkConstrainsAfterUpdateRecipeDetail(request, recipeDetailService))
                .build();
    }

    //</editor-fold>
}
