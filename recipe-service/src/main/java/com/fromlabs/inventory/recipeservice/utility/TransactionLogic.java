package com.fromlabs.inventory.recipeservice.utility;

import com.fromlabs.inventory.recipeservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.recipeservice.detail.*;
import com.fromlabs.inventory.recipeservice.detail.beans.*;
import com.fromlabs.inventory.recipeservice.detail.specification.RecipeDetailSpecification;
import com.fromlabs.inventory.recipeservice.recipe.*;
import com.fromlabs.inventory.recipeservice.recipe.beans.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;


import java.net.InetAddress;
import java.util.List;
import java.util.Objects;

import static com.fromlabs.inventory.recipeservice.recipe.RecipeEntity.update;
import static com.fromlabs.inventory.recipeservice.recipe.beans.RecipeDto.from;
import static com.fromlabs.inventory.recipeservice.recipe.specification.RecipeSpecification.filter;
import static java.util.Objects.*;

/**
 * Transaction Logic Layer
 */
@UtilityClass
@Slf4j
public class TransactionLogic {

    /**
     * Get all recipe group list by tenant id
     * @param tenantId  Tenant ID
     * @param service   RecipeService
     * @return          List&lt;RecipeDto&gt;
     */
    public List<RecipeDto> getRecipeGroupList(
            Long                tenantId,
            RecipeService       service
    ) {
        log.info("getRecipeGroupList");
        return from(service.getAll(tenantId));
    }

    //<editor-fold desc="Get Recipe Page With Filter">

    /**
     * Get page with filter of recipe group and child
     * @param request   RecipePageRequest
     * @param service   RecipeService
     * @return          Page&lt;RecipeDto&gt;
     */
    public Page<RecipeDto> getRecipePageWithFilter(
            RecipePageRequest   request,
            RecipeService       service
    ) {
        log.info("getRecipePageWithFilter: start");
        return from(service.getPage(getFilter(request, service), request.getPageable()));
    }

    /**
     * Get filter for entity recipe group and child
     * @param request RecipePageRequest
     * @param service RecipeServiceImpl
     * @return Specification&lt;RecipeEntity&gt;
     */
    private Specification<RecipeEntity> getFilter(
            RecipePageRequest   request,
            RecipeService       service
    ) {
        log.info("getRecipePageWithFilter: getFilter");
        return filter(RecipeEntity.from(request), getParent(request, service));
    }

    /**
     * Get parent as recipe group
     * @param request RecipePageRequest
     * @param service RecipeServiceImpl
     * @return RecipeEntity
     */
    private RecipeEntity getParent(
            RecipePageRequest   request,
            RecipeService       service
    ) {
        log.info("getRecipePageWithFilter: getParent");
        return isNull(request.getParentId()) ? null : service.get(request.getParentId());
    }

    //</editor-fold>

    //<editor-fold desc="Save recipe entity">

    /**
     * Save recipe entity from recipe request
     * @param request   RecipeRequest
     * @param service   RecipeService
     * @return          RecipeDto
     */
    public RecipeDto saveRecipeEntity(
            RecipeRequest request,
            RecipeService service
    ) {
        log.info("saveRecipeEntity");
        return from(service.save(RecipeEntity.from(request).setParent(nonNull(request.getParentId()) ? service.get(request.getParentId()) : null)));
    }

    //</editor-fold>

    //<editor-fold desc="Save recipe detail entity">

    /**
     * Save recipe entity from recipe detail request
     * @param request               RecipeRequest
     * @param recipeService         RecipeDetailService
     * @param recipeDetailService   RecipeDetailService
     * @return                      RecipeDetailDto
     */
    public RecipeDetailDto saveRecipeDetailEntity(
            RecipeDetailRequest request,
            RecipeService       recipeService,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        log.info("saveRecipeDetailEntity");
        return RecipeDetailDto.from(recipeDetailService.save(
                RecipeDetailEntity.from(request, recipeService.get(request.getRecipeId()))),
                ingredientClient);
    }

    //</editor-fold>

    //<editor-fold desc="Update recipe entity">

    /**
     * Update recipe entity from recipe request
     * @param request   RecipeRequest
     * @param service   RecipeService
     * @return          RecipeDto
     */
    public RecipeDto updateRecipeEntity(
            RecipeRequest request,
            RecipeService service
    ) {
        log.info("updateRecipeEntity");
        final var recipe = service.get(request.getId());
        return from(service.save(
                update(request, recipe)
                        .setParent(isNull(request.getParentId()) || request.getParentId() < 0 ?
                                null : service.get(request.getParentId()))
        ));
    }

    //</editor-fold>

    //<editor-fold desc="Update recipe detail entity">

    /**
     * Update recipe entity from recipe request
     * @param request           RecipeRequest
     * @param service           RecipeServiceImpl
     * @param ingredientClient  IngredientClient
     * @return                   RecipeDto
     */
    public RecipeDetailDto updateRecipeEntity(
            RecipeDetailRequest request,
            RecipeDetailService service,
            IngredientClient    ingredientClient
    ) {
        log.info("updateRecipeEntity");
        return RecipeDetailDto.from(service.save(
                RecipeDetailEntity.update(request, service.get(request.getId()))), ingredientClient);
    }

    //</editor-fold>

    //<editor-fold desc="Get Recipe Detail Page With Filter">
    /**
     * Get page with filter of recipe detail
     * @param request               RecipeDetailPageRequest
     * @param recipeService         RecipeService
     * @param recipeDetailService   RecipeDetailService
     * @param client                IngredientClient
     * @return                      Page&lt;RecipeDetailDto&gt;
     */
    public Page<RecipeDetailDto> getRecipeDetailPageWithFilter(
            RecipeDetailPageRequest request,
            RecipeService           recipeService,
            RecipeDetailService     recipeDetailService,
            IngredientClient        client
    ) {
        log.info("getRecipeDetailPageWithFilter: start");
        return RecipeDetailDto.from(recipeDetailService.getPage(
                getFilter(request, getRecipeById(request.getRecipeId(), recipeService)), request.getPageable()), client);
    }

    private RecipeEntity getRecipeById(Long id, RecipeService recipeService) {
        return Objects.nonNull(id) ? recipeService.get(id) : null;
    }

    /**
     * Get filter for entity recipe detail
     * @param request   RecipeDetailPageRequest
     * @return          Specification&lt;RecipeDetailEntity&gt;
     */
    private Specification<RecipeDetailEntity> getFilter(
            RecipeDetailPageRequest request,
            RecipeEntity            recipe
    ) {
        return RecipeDetailSpecification.filter(RecipeDetailEntity.from(request), recipe);
    }
    //</editor-fold>

    //<editor-fold desc="Get all recipe detail list by tenant id">

    /**
     * Get all recipe detail list by tenant id
     * @param tenantId              Client ID
     * @param recipeDetailService   RecipeDetailService
     * @return                      List&lt;RecipeDetailEntity&gt;
     */
    public List<RecipeDetailDto> getAllRecipeDetail(
            Long                tenantId,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        log.info("getAllRecipeDetail");
        return RecipeDetailDto.from(recipeDetailService.getAll(RecipeDetailSpecification.hasClientId(tenantId)), ingredientClient);
    }

    //</editor-fold>

    //<editor-fold desc="Get all recipe detail list by tenant id and recipe">

    /**
     * Get all recipe detail list by tenant id and recipe
     * @param tenantId              Client ID
     * @param recipeId              Recipe ID
     * @param recipeService         RecipeService
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      List&lt;RecipeDetailDto&gt;
     */
    public List<RecipeDetailDto> getAllRecipeDetailWithRecipe(
            Long                tenantId,
            Long                recipeId,
            RecipeService       recipeService,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        log.info("getAllRecipeDetailWithRecipe");
        return RecipeDetailDto.from(recipeDetailService.getAll(getSpecificationWithTenantAndRecipe(tenantId, recipeId, recipeService)), ingredientClient);
    }

    /**
     * getSpecificationWithTenantAndRecipe
     * @param tenantId      Tenant ID
     * @param recipeId      Recipe ID
     * @param recipeService RecipeService
     * @return              Specification&lt;RecipeDetailEntity&gt;
     */
    public Specification<RecipeDetailEntity> getSpecificationWithTenantAndRecipe(
            Long                tenantId,
            Long                recipeId,
            RecipeService       recipeService
    ) {
        return RecipeDetailSpecification.hasClientId(tenantId).and(RecipeDetailSpecification.hasRecipe(recipeService.get(recipeId)));
    }

    //</editor-fold>

    //<editor-fold desc="Get all recipe child as list">

    /**
     * Get all recipe child as list by tenant id and parent id
     * @param tenantId      Tenant ID
     * @param parentId      Recipe group ID
     * @param recipeService RecipeService
     * @return              List&lt;RecipeDto&gt;
     */
    public List<RecipeDto> getAllRecipeChild(
            Long            tenantId,
            Long            parentId,
            RecipeService   recipeService
    ) {
        log.info("getAllRecipeChild");
        return from(recipeService.getAll(tenantId, parentId));
    }

    //</editor-fold>

    //<editor-fold desc="Get recipe by code">

    /**
     * Get recipe (both types) by code
     * @param code          Recipe Unique Code
     * @param recipeService RecipeService
     * @return              RecipeDto
     */
    public RecipeDto getRecipeByCode(
            String          code,
            RecipeService   recipeService
    ) {
        log.info("getRecipeByCode");
        return from(recipeService.get(code));
    }

    //</editor-fold>

    //<editor-fold desc="Get recipe detail by id">

    /**
     * Get recipe detail by id
     * @param id                    Recipe detail id
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      RecipeDetailDto
     */
    public RecipeDetailDto getRecipeDetailById(
            Long                id,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        log.info("getRecipeDetailById");
        return RecipeDetailDto.from(recipeDetailService.get(id), ingredientClient);
    }

    //</editor-fold>

    //<editor-fold desc="Get recipe detail by code">

    /**
     * Get recipe detail by code
     * @param code                  Recipe detail code
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      RecipeDetailDto
     */
    public RecipeDetailDto getRecipeDetailByCode(
            String              code,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        log.info("getRecipeDetailByCode");
        return RecipeDetailDto.from(recipeDetailService.get(code), ingredientClient);
    }

    //</editor-fold>

    /**
     * Log out Http path with extra information of service
     * @param method        HTTP method
     * @param servicePath   Service path
     * @param subPath       Sub path
     * @return              String
     */
    public String path(HttpMethod method, String servicePath, String subPath, String apiVersion) {
        return method.name().concat(" ")
                .concat(InetAddress.getLoopbackAddress().getCanonicalHostName().trim())
                .concat(servicePath.trim()).concat(subPath.trim()).concat(" ")
                .concat(apiVersion.trim()).trim();
    }
}
