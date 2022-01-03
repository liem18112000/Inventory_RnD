package com.fromlabs.inventory.recipeservice.utility;

import com.fromlabs.inventory.recipeservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.recipeservice.common.dto.SimpleDto;
import com.fromlabs.inventory.recipeservice.detail.*;
import com.fromlabs.inventory.recipeservice.detail.beans.dto.RecipeDetailDto;
import com.fromlabs.inventory.recipeservice.detail.beans.request.RecipeDetailPageRequest;
import com.fromlabs.inventory.recipeservice.detail.beans.request.RecipeDetailRequest;
import com.fromlabs.inventory.recipeservice.detail.mapper.RecipeDetailMapper;
import com.fromlabs.inventory.recipeservice.detail.specification.RecipeDetailSpecification;
import com.fromlabs.inventory.recipeservice.recipe.*;
import com.fromlabs.inventory.recipeservice.recipe.beans.dto.RecipeDto;
import com.fromlabs.inventory.recipeservice.recipe.beans.dto.RecipeWithParentDto;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipePageRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipeRequest;
import com.fromlabs.inventory.recipeservice.recipe.mapper.RecipeMapper;
import com.fromlabs.inventory.recipeservice.recipe.mapper.RecipeWithParentMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;


import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        return RecipeMapper.toDto(service.getAll(tenantId));
    }

    //<editor-fold desc="Get Recipe Page With Filter">

    /**
     * Get page with filter of recipe group and child
     * @param request   RecipePageRequest
     * @param service   RecipeService
     * @return          Page&lt;RecipeDto&gt;
     */
    public Page<RecipeDto> getRecipePageWithFilter(
            RecipePageRequest request,
            RecipeService       service
    ) {
        log.info("getRecipePageWithFilter: start");
        return RecipeMapper.toDto(service.getPage(getFilter(request, service), request.getPageable()));
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
        return filter(RecipeMapper.toEntity(request), getParent(request, service));
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
        return isNull(request.getParentId()) ? null : service.getById(request.getParentId());
    }

    //</editor-fold>

    //<editor-fold desc="Get recipe extend page with filter">

    public Page<RecipeWithParentDto> getRecipeExtendPageWithFilter(
            RecipePageRequest   request,
            RecipeService       service
    ) {
        log.info("getRecipeExtendPageWithFilter: start");
        return RecipeWithParentMapper.toDto(service.getPage(getFilter(request, service), request.getPageable()));
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
        return RecipeMapper.toDto(service.save(RecipeMapper.toEntity(request).setParent(nonNull(request.getParentId()) ? service.getById(request.getParentId()) : null)));
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
        return RecipeDetailMapper.toDto(recipeDetailService.save(
                RecipeDetailMapper.toEntity(request, recipeService.getById(request.getRecipeId()))),
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
        final var recipe = service.getById(request.getId());
        return RecipeMapper.toDto(service.save(
                recipe.update(request).setParent(
                        isNull(request.getParentId()) || request.getParentId() < 0 ?
                                null : service.getById(request.getParentId()))
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
        return RecipeDetailMapper.toDto(
                service.save(service.getById(request.getId()).update(request)),
                ingredientClient
        );
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
        return RecipeDetailMapper.toDto(recipeDetailService.getPage(
                getFilter(request, getRecipeById(request.getRecipeId(), recipeService)), request.getPageable()), client);
    }

    private RecipeEntity getRecipeById(Long id, RecipeService recipeService) {
        return Objects.nonNull(id) ? recipeService.getById(id) : null;
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
        return RecipeDetailSpecification.filter(RecipeDetailMapper.toEntity(request), recipe);
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
        return RecipeDetailMapper.toDto(recipeDetailService.getAll(RecipeDetailSpecification.hasClientId(tenantId)), ingredientClient);
    }

    //</editor-fold>

    //<editor-fold desc="Get simple label-value recipe group active DTO">

    /**
     * Get simple label-value recipe group active DTO
     * @param tenantId      Tenant ID
     * @param recipeService RecipeService
     * @return              List&lt;SimpleDto&gt;
     */
    public List<SimpleDto> getSimpleRecipeGroupActiveDto(
            Long            tenantId,
            RecipeService   recipeService
    ) {
        // Check pre-condition
        assert nonNull(tenantId);
        assert nonNull(recipeService);

        // Get all active children recipe as Label-Value DTO list
        return recipeService.getAll(tenantId).stream()
                // Filter to children are active
                .filter(    recipe -> recipe.isGroup() && recipe.isActivated())
                // Map to label as recipe ID and value as recipe name
                .map(       recipe -> new SimpleDto(recipe.getId(), recipe.getName()))
                // Convert to list
                .collect(   Collectors.toList());
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
        return RecipeDetailMapper.toDto(recipeDetailService.getAll(getSpecificationWithTenantAndRecipe(tenantId, recipeId, recipeService)), ingredientClient);
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
        return RecipeDetailSpecification.hasClientId(tenantId).and(RecipeDetailSpecification.hasRecipe(recipeService.getById(recipeId)));
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
        return RecipeMapper.toDto(recipeService.getAll(tenantId, parentId));
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
        return RecipeMapper.toDto(recipeService.getByCode(code));
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
        return RecipeDetailMapper.toDto(recipeDetailService.getById(id), ingredientClient);
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
        return RecipeDetailMapper.toDto(recipeDetailService.getByCode(code), ingredientClient);
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
