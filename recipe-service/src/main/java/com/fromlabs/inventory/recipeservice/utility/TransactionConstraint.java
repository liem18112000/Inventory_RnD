package com.fromlabs.inventory.recipeservice.utility;

import com.fromlabs.inventory.recipeservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.recipeservice.common.exception.ConstraintViolateException;
import com.fromlabs.inventory.recipeservice.common.wrapper.ConstraintWrapper;
import com.fromlabs.inventory.recipeservice.detail.RecipeDetailService;
import com.fromlabs.inventory.recipeservice.detail.beans.request.RecipeDetailRequest;
import com.fromlabs.inventory.recipeservice.recipe.RecipeService;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipeRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.fromlabs.inventory.recipeservice.recipe.specification.RecipeSpecification.hasCode;
import static com.fromlabs.inventory.recipeservice.recipe.specification.RecipeSpecification.hasName;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.data.jpa.domain.Specification.where;

@UtilityClass
@Slf4j
public class TransactionConstraint {

    /**
     * Check recipe is existed by code
     * @param property  Recipe code
     * @param service   RecipeService
     * @return          boolean
     */
    public boolean isRecipeExistByCode(
            String          property,
            RecipeService   service
    ) {
        return logWrapper(nonNull(service.getByCode(property)), "isRecipeExistByCode : {}");
    }

    /**
     * Check recipe detail is existed by code
     * @param property  Recipe detail code
     * @param service   RecipeDetailService
     * @return boolean
     */
    public boolean isRecipeDetailExistByCode(
            String              property,
            RecipeDetailService service
    ) {
        return logWrapper(nonNull(service.getByCode(property)), "isRecipeDetailExistByCode : {}");
    }

    /**
     * Check recipe is existed by id
     * @param property  Recipe id
     * @param service   RecipeService
     * @return          boolean
     */
    public boolean isRecipeExistById(Long property, RecipeService service) {
        return logWrapper(nonNull(service.getById(property)), "isRecipeExistById : {}");
    }

    /**
     * Check recipe detail is existed by id
     * @param property  Recipe detail id
     * @param service   RecipeDetailService
     * @return          boolean
     */
    public boolean isRecipeDetailExistById(
            Long                property,
            RecipeDetailService service
    ) {
        return logWrapper(nonNull(service.getById(property)), "isRecipeDetailExistById : {}");
    }

    /**
     * Check constraints before save recipe group or child
     * @param request   RecipeRequest
     * @param service   RecipeServiceImpl
     * @return          boolean
     */
    public boolean checkConstrainsBeforeSaveRecipe(
            RecipeRequest   request,
            RecipeService   service
    ) {
        // Check pre-condition
        assert nonNull(service);

        final boolean result = buildConstraintWrapperCheckRecipeExistByNameOrCode(request.getName(), request.getCode(), service)
                && buildConstraintWrapperIsRecipeParentExistByIDOrNullParentId(request.getParentId(), service);
        return logWrapper( result, "checkConstrainsBeforeSaveRecipe: {}");
    }

    public boolean buildConstraintWrapperCheckRecipeExistByNameOrCode(
            String          name,
            String          code,
            RecipeService   service
    ) {
        return ConstraintWrapper.builder()
                .name("Check constrain recipe is not duplicate by name or code")
                .check(() -> service.getAll(where(hasName(name)).or(hasCode(code))).isEmpty())
                .exception(new ConstraintViolateException("Check constrain recipe is duplicate by name or code"))
                .build().constraintCheck();
    }

    /**
     * Buld constraint wrapper is recipe parent exist by ID
     * @param parentId  Recipe group id
     * @param service   RecipeService
     * @return          boolean
     */
    public boolean buildConstraintWrapperIsRecipeParentExistByIDOrNullParentId(
            Long            parentId,
            RecipeService   service
    ) {
        return ConstraintWrapper.builder()
                .name("Check constrain is recipe parent exist by id or null parent id")
                .check(() -> isRecipeParentExistByIDOrNullParentId(parentId, service))
                .exception(new ConstraintViolateException("Recipe parent exist by id or null parent id"))
                .build().constraintCheck();
    }

    /**
     * Check constraints before save recipe detail
     * @param request               RecipeDetailRequest
     * @param recipeDetailService   RecipeDetailService
     * @param ingredientClient      IngredientClient
     * @return                      boolean
     */
    public boolean checkConstrainsBeforeSaveRecipeDetail(
            RecipeDetailRequest request,
            RecipeDetailService recipeDetailService,
            IngredientClient    ingredientClient
    ) {
        // Check pre-conditions
        assert nonNull(request);
        assert nonNull(recipeDetailService);
        assert nonNull(ingredientClient);

        // Get all needed information
        final String code           = request.getCode();
        final Long   tenantId       = request.getTenantId();
        final Long   ingredientId   = request.getIngredientId();
        final float  quantity       = request.getQuantity();

        // Check constraints
        final boolean result =  buildConstraintWrapperCheckRecipeDetailExistByCode(code, recipeDetailService) &&
                                buildConstraintWrapperIsIngredientExistById(tenantId, ingredientId, ingredientClient) &&
                                quantity > 0;
        return logWrapper(result, "checkConstrainsBeforeSaveRecipeDetail: {}");
    }

    /**
     * Build constraint wrapper check recipe detail exist by code
     * @param code      Recipe Detail code
     * @param service   RecipeDetailService
     * @return          boolean
     */
    public boolean buildConstraintWrapperCheckRecipeDetailExistByCode(
            String                  code,
            RecipeDetailService     service
    ) {
        return ConstraintWrapper.builder()
                .name("Check constrain recipe detail is not duplicate by code")
                .check(() -> isNull(service.getByCode(code)))
                .exception(new ConstraintViolateException("Check constrain recipe detail is duplicate by code"))
                .build().constraintCheck();
    }

    public boolean buildConstraintWrapperIsIngredientExistById(
            Long                tenantId,
            Long                ingredientId,
            IngredientClient    ingredientClient
    ) {
        return ConstraintWrapper.builder()
                .name("Check constrain ingredient exist by id")
                .check(() -> nonNull(ingredientClient.getIngredientById(tenantId, ingredientId)))
                .exception(new ConstraintViolateException("Check constrain ingredient is not exist by id"))
                .build().constraintCheck();
    }

    /**
     * Check constraints before update recipe group or child
     * @param request   RecipeRequest
     * @param service   RecipeService
     * @return          boolean
     */
    public boolean checkConstrainsBeforeUpdateRecipe(
            RecipeRequest request,
            RecipeService service
    ) {
        final var recipeWithName = service.getByName(request.getTenantId(), request.getName());
        final var recipeWithCode = service.getByCode(request.getCode());
        return logWrapper( nonNull(service.getById(request.getId())) &&
                (isNull(recipeWithCode) || recipeWithCode.getId().equals(request.getId())) &&
                (isNull(recipeWithName) || recipeWithName.getId().equals(request.getId())) &&
                isRecipeParentExistByIDOrNullParentId(request.getParentId(), service)
                , "checkConstrainsBeforeUpdateRecipe: {}");
    }

    /**
     * Check constraints before update recipe detail
     * @param request   RecipeDetailRequest
     * @param service   RecipeDetailService
     * @return          boolean
     */
    public boolean checkConstrainsBeforeUpdateRecipeDetail(
            RecipeDetailRequest request,
            RecipeDetailService service
    ) {
        return logWrapper( nonNull(service.getById(request.getId())) &&
                (isNull(service.getByCode(request.getCode())) || service.getByCode(request.getCode()).getId().equals(request.getId()))
                , "checkConstrainsBeforeUpdateRecipe: {}");
    }

    /**
     * Check constraints after update recipe group or child
     * @param request   RecipeRequest
     * @param service   RecipeService
     * @return          boolean
     */
    public boolean checkConstrainsAfterUpdateRecipe(
            RecipeRequest request,
            RecipeService service
    ) {
        final var recipe = service.getById(request.getId());
        return logWrapper( nonNull(recipe) &&
                request.getCode().equals(recipe.getCode()) &&
                request.getName().equals(recipe.getName())
                , "checkConstrainsAfterUpdateRecipe: {}");
    }

    /**
     * Check constraints after update recipe detail
     * @param request   RecipeDetailRequest
     * @param service   RecipeDetailService
     * @return          boolean
     */
    public boolean checkConstrainsAfterUpdateRecipeDetail(
            RecipeDetailRequest request,
            RecipeDetailService service
    ) {
        final var recipe = service.getById(request.getId());
        return logWrapper( nonNull(recipe) &&
                request.getCode().equals(recipe.getCode())
                , "checkConstrainsAfterUpdateRecipeDetail: {}");
    }

    /**
     * Check that if the parent id is not null the parent recipe must be existed
     * @param parentId  Parent ID
     * @param service   RecipeServiceImpl
     * @return          boolean
     */
    public boolean isRecipeParentExistByIDOrNullParentId(Long parentId, RecipeService service) {
        return logWrapper( isNull(parentId) || nonNull(service.getById(parentId)),"isRecipeParentExistByID: {}");
    }

    /**
     * Log wrapper
     * @param result    boolean
     * @param message   Log message
     * @return          boolean
     */
    private boolean logWrapper(boolean result, String message) {
        log.info(message, result);
        return result;
    }
}
