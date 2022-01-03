package com.fromlabs.inventory.recipeservice.recipe;

import com.fromlabs.inventory.recipeservice.RecipeServiceApplication;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipePageRequest;
import com.fromlabs.inventory.recipeservice.recipe.specification.RecipeSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = RecipeServiceApplication.class)
@ActiveProfiles("tuan-local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecipeServiceTest {
    @Autowired
    private RecipeService recipeService;
    //<editor-fold desc="Get recipe by code">
    @DisplayName("Get recipe by code" + " - positive case : all thing is right")
    @Test
    void getByCode_Postive_AllThingsIsRight() {
        var recipe = this.recipeService.get(1L,"123123");
        assert Objects.nonNull(recipe);
        assert Objects.equals(recipe.getClientId(),1L);
        assert Objects.equals(recipe.getCode(),"123123");
    }
    //</editor-fold>
    @DisplayName("Get all by client ID" + " - positive case : all thing is right")
    @Test
    void getAll_Postive_AllThingsIsRight() {
        var recipes=this.recipeService.getAll(1L);
        assert Objects.nonNull(recipes);
        assert !recipes.isEmpty();
        assert recipes.stream().allMatch(recipe -> Objects.equals(recipe.getClientId(),1L) );
    }
    //</editor-fold>
    @DisplayName("Get all by client ID" + " - negative case : client ID code is not exist")
    @Test
    void getAll_Negative_CodeIsNotExist() {
        var recipes = this.recipeService.getAll(777L);
        assert Objects.nonNull(recipes);
        assert recipes.isEmpty();
    }
    //<editor-fold desc="Get page recipe">
    @DisplayName("Get page by clientID" + " - positive case : all thing is right")
    @Test
    void getPage_PositiveCase_AllThingIsRight() {
        var request = new RecipePageRequest();
        request.setTenantId(1L);
        var page = this.recipeService.getPage(RecipeSpecification.filter(RecipeEntity.from(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().allMatch(recipe->{ return ((RecipeEntity)recipe).getClientId().equals(1L); } );
    }
    @DisplayName("Get page filter with exist name" + " - positive case : all thing is right")
    @Test
    void getPage_PositiveCase_FilterWithExistName() {
        var request = new RecipePageRequest();
        request.setName("Strawberri yogurt");
        request.setTenantId(1L);
        var page = this.recipeService.getPage(RecipeSpecification.filter(RecipeEntity.from(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().allMatch(recipe->{ return ((RecipeEntity)recipe).getClientId().equals(1L) && ((RecipeEntity)recipe).getName().equals("Strawberri yogurt"); } );
    }
    @DisplayName("Get page filter with none exist name and tenant ID" + " - negative case : Name and tenant ID is not exist")
    @Test
    void getPage_NegativeCase_FilterWithNoneExistNameAndTenantID() {
        var request = new RecipePageRequest();
        request.setName("Clown");
        request.setTenantId(2L);
        var page = this.recipeService.getPage(RecipeSpecification.filter(RecipeEntity.from(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().noneMatch(recipe->{ return ((RecipeEntity)recipe).getClientId().equals(2L) && ((RecipeEntity)recipe).getName().equals("Clown"); } );
    }
    @DisplayName("Get page filter with exist code" + " - positive case : all thing is right")
    @Test
    void getPage_PositiveCase_FilterWithExistCode() {
        var request = new RecipePageRequest();
        request.setCode("531531");
        request.setTenantId(1L);
        var page = this.recipeService.getPage(RecipeSpecification.filter(RecipeEntity.from(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().allMatch(recipe->{ return ((RecipeEntity)recipe).getClientId().equals(1L) && ((RecipeEntity)recipe).getCode().equals("531531"); } );
    }
    @DisplayName("Get page filter with none exist code" + " - negative case : code is not exist")
    @Test
    void getPage_NegativeCase_FilterWithNoneExistCode() {
        var request = new RecipePageRequest();
        request.setCode("999DS");
        request.setTenantId(1L);
        var page = this.recipeService.getPage(RecipeSpecification.filter(RecipeEntity.from(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().allMatch(recipe->{ return ((RecipeEntity)recipe).getClientId().equals(1L) && ((RecipeEntity)recipe).getCode().equals("999DS"); } );
    }
    @DisplayName("Get page filter with exist Description" + " - positive case : all thing is right")
    @Test
    void getPage_PositiveCase_FilterWithExistDescription() {
        var request = new RecipePageRequest();
        request.setDescription("yorgurt");
        request.setTenantId(1L);
        var page = this.recipeService.getPage(RecipeSpecification.filter(RecipeEntity.from(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().allMatch(recipe->{ return ((RecipeEntity)recipe).getClientId().equals(1L) && ((RecipeEntity)recipe).getDescription().equals("yorgurt"); } );
    }
    @DisplayName("Get page filter with none exist Description" + " - negative case : Description and Tenant ID is not exist")
    @Test
    void getPage_NegativeCase_FilterWithNoneExistDescriptionAndTenantID() {
        var request = new RecipePageRequest();
        request.setDescription("tooHot");
        request.setTenantId(2L);
        var page = this.recipeService.getPage(RecipeSpecification.filter(RecipeEntity.from(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().noneMatch(recipe->{ return ((RecipeEntity)recipe).getClientId().equals(2L) && ((RecipeEntity)recipe).getDescription().equals("tooHot"); } );
    }
    //</editor-fold>

    @Test
    void getAll() {
    }

    @Test
    void testGetPage() {
    }

    @Test
    void testGetAll() {
    }

    @Test
    void testGetPage1() {
    }

    @Test
    void testGetAll1() {
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }
}