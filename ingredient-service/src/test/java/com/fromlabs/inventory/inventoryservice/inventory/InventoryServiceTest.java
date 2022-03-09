/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.common.helper.CustomizePageRequest;
import com.fromlabs.inventory.inventoryservice.common.template.UnitTestTemplateProcess;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryPageRequest;
import com.fromlabs.inventory.inventoryservice.inventory.mapper.InventoryMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static com.fromlabs.inventory.inventoryservice.common.template.UnitTestTemplateProcess.getInput;
import static com.fromlabs.inventory.inventoryservice.helper.KeyIndicator.*;
import static com.fromlabs.inventory.inventoryservice.helper.TestKeyIndicator.*;
import static com.fromlabs.inventory.inventoryservice.helper.TestcaseName.*;
import static com.fromlabs.inventory.inventoryservice.inventory.specification.InventorySpecification.filter;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"liem-local", "dev"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryServiceTest {

    @Autowired
    private InventoryService service;

    @Autowired
    private IngredientService ingredientService;

    private UnitTestTemplateProcess getTemplateUnitTest;

    //<editor-fold desc="SETUP">

    @BeforeEach
    void setUp() {
        this.getTemplateUnitTest = UnitTestTemplateProcess.builder()
                .build()
                .input(ID,         81L).input(NEGATIVE_ID,          -1L).input(NON_EXIST_ID,            99999L)
                .input(INGREDIENT_ID, 161L).input(NEGATIVE_INGREDIENT_ID, -1L).input(NON_EXIST_INGREDIENT_ID, 99999L)
                .input(TENANT_ID,   1L).input(NEGATIVE_TENANT_ID,   -1L).input(NON_EXIST_TENANT_ID,     99999L)
                .input(PAGE_NUMBER, 0) .input(NEGATIVE_PAGE_NUMBER, -1) .input(NON_EXIST_PAGE_NUMBER,   99999)
                .input(PAGE_SIZE,   10) .input(NEGATIVE_PAGE_SIZE,  -1) .input(NON_EXIST_PAGE_SIZE,     99999).input(ZERO_PAGE_SIZE, 0)
                .input(ASC_SORT,    "id, asc")                  .input(NON_EXIST_ASC_SORT,      "nonExist, asc")
                .input(ASC_SORT,    "id, desc")                 .input(NON_EXIST_DESC_SORT,     "nonExist, desc")
                .input(NAME,        "ST 25 Rice ST025Rice").input(NON_EXIST_NAME,          NON_EXIST_NAME)
                .input(DESCRIPTION, "The best rice in the world")     .input(NON_EXIST_DESCRIPTION,   NON_EXIST_DESCRIPTION)
                .input(UNIT,        "kilogram")                   .input(NON_EXIST_UNIT,          NON_EXIST_UNIT)
                .input(UNIT_TYPE,   "weight")                    .input(NON_EXIST_UNIT_TYPE,     NON_EXIST_UNIT_TYPE)
                .input(UPDATED_AT,  "2022-03-09T11:54:51.252494100Z")      .input(NON_EXIST_UPDATED_AT,    Instant.now().plusSeconds(3600).toString())
                .input(PAGE_REQUEST, new InventoryPageRequest());
    }

    @AfterEach
    void tearDown() {
    }

    private UnitTestTemplateProcess getTemplate(Callable<Object> process, Consumer<Object> testCase) {
        this.getTemplateUnitTest.setProcess(process);
        this.getTemplateUnitTest.setAsserts(testCase);
        return this.getTemplateUnitTest;
    }

    private Page<?> assertPageIsNotNullAndNotEmpty(Page<?> bootstrap) {
        assert Objects.nonNull(bootstrap);
        assert !bootstrap.isEmpty();
        return bootstrap;
    }

    private Page<?> assertPageIsNotNullAndEmpty(Page<?> bootstrap) {
        assert Objects.nonNull(bootstrap);
        assert bootstrap.isEmpty();
        return bootstrap;
    }

    //</editor-fold>

    //<editor-fold desc="GET BY ID">

    @DisplayName(GET_INVENTORY_BY_ID + " - positive case : all thing is right")
    @Test
    @Order(1)
    void getByID_PositiveCase_AllThingIsRight() {
        this.getTemplate(
                () -> this.service.getById((Long) getInput(ID)), (inventory) -> {
                    assert Objects.nonNull(inventory);
                    assert Objects.equals(((InventoryEntity)inventory).getId(), getInput(ID));
                }).run();
    }

    @DisplayName(GET_INVENTORY_BY_ID + " - negative case : ID is negative")
    @Test
    @Order(2)
    void getByID_NegativeCase_IdIsNegative() {
        this.getTemplate(
                () -> this.service.getById((Long) getInput(NEGATIVE_ID)), (inventory) -> {
                    assert Objects.isNull(inventory);
                }).run();
    }

    @DisplayName(GET_INVENTORY_BY_ID + " - negative case : ID is non-exist")
    @Test
    @Order(3)
    void getByID_NegativeCase_IdIsNonExist() {
        this.getTemplate(
                () -> this.service.getById((Long) getInput(NON_EXIST_ID)), (inventory) -> {
                    assert Objects.isNull(inventory);
                }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET BY NAME">

    @DisplayName(GET_INVENTORY_BY_NAME + " - positive case : all thing is right")
    @Test
    @Order(4)
    void getByName_PositiveCase_AllThingIsRight() {
        this.getTemplate(
            () -> this.service.getByName((Long) getInput(TENANT_ID), (String) getInput(NAME)), (inventory) -> {
                assert Objects.nonNull(inventory);
                assert Objects.equals(((InventoryEntity)inventory).getClientId(), getInput(TENANT_ID));
                assert Objects.equals(((InventoryEntity)inventory).getName(), getInput(NAME));
            }).run();
    }

    @DisplayName(GET_INVENTORY_BY_NAME + " - negative case : name is not exist")
    @Test
    @Order(5)
    void getByName_NegativeCase_NameIsNotExist() {
        this.getTemplate(
                () -> this.service.getByName((Long) getInput(TENANT_ID), (String) getInput(NON_EXIST_NAME)), (inventory) -> {
                    assert Objects.isNull(inventory);
                }).run();
    }

    @DisplayName(GET_INVENTORY_BY_NAME + " - negative case : tenant id is not exist")
    @Test
    @Order(6)
    void getByName_NegativeCase_TenantIdIsNotExist() {
        this.getTemplate(
                () -> this.service.getByName((Long) getInput(NON_EXIST_TENANT_ID), (String) getInput(NAME)), (inventory) -> {
                    assert Objects.isNull(inventory);
                }).run();
    }

    @DisplayName(GET_INVENTORY_BY_NAME + " - negative case : tenant id is negative")
    @Test
    @Order(7)
    void getByName_NegativeCase_TenantIdIsNegative() {
        this.getTemplate(
                () -> this.service.getByName((Long) getInput(NEGATIVE_TENANT_ID), (String) getInput(NAME)), (inventory) -> {
                    assert Objects.isNull(inventory);
                }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET BY INGREDIENT">

    @DisplayName(GET_INVENTORY_BY_INGREDIENT + " - positive case : all thing is right")
    @Test
    @Order(8)
    void getByIngredient_PositiveCase_AllThingIsRight() {
        this.getTemplate(
                () -> this.service.getByIngredient(this.ingredientService.getById((Long) getInput(INGREDIENT_ID))), (inventory) -> {
                    final var ingredient = this.ingredientService.getById((Long) getInput(INGREDIENT_ID));
                    assert Objects.nonNull(inventory);
                    assert Objects.equals(((InventoryEntity)inventory).getIngredientId(), ingredient.getId());
                    assert Objects.equals(((InventoryEntity)inventory).getClientId(), ingredient.getClientId());
                }).run();
    }

    @DisplayName(GET_INVENTORY_BY_INGREDIENT + " - negative case : ingredient id is non exist")
    @Test
    @Order(9)
    void getByIngredient_PositiveCase_IngredientIdIsNonExist() {
        this.getTemplate(
                () -> this.service.getByIngredient(this.ingredientService.getById((Long) getInput(NON_EXIST_INGREDIENT_ID))), (inventory) -> {
                    assert Objects.isNull(inventory);
                }).run();
    }

    @DisplayName(GET_INVENTORY_BY_INGREDIENT + " - negative case : ingredient id is negative")
    @Test
    @Order(10)
    void getByIngredient_NegativeCase_IngredientIdIsNegative() {
        this.getTemplate(
                () -> this.service.getByIngredient(this.ingredientService.getById((Long) getInput(NEGATIVE_INGREDIENT_ID))), (inventory) -> {
                    assert Objects.isNull(inventory);
                }).run();
    }

    @DisplayName(GET_INVENTORY_BY_INGREDIENT + " - negative case : ingredient id exist but it is not ingredient child")
    @Test
    @Order(11)
    void getByIngredient_NegativeCase_IngredientIsNotChild() {
        this.getTemplate(
                () -> this.service.getByIngredient(this.ingredientService.getById(1L)), (inventory) -> {
                    assert Objects.isNull(inventory);
                }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET ALL BY CLIENT ID">

    @DisplayName(GET_INVENTORY_ALL + " - positive case : all thing is right")
    @Test
    @Order(12)
    void getAllByClientId_PositiveCase_AllThingIsRight() {
        this.getTemplate(
            () -> this.service.getAll((Long) getInput(TENANT_ID)), (inventoryList) -> {
                assert Objects.nonNull(inventoryList);
                assert !((List<?>)inventoryList).isEmpty();
                assert ((List<?>)inventoryList).stream().allMatch(
                        inventory -> ((InventoryEntity)inventory).isActive() &&
                                Objects.equals(((InventoryEntity)inventory).getClientId(), getInput(TENANT_ID)));
            }).run();
    }

    @DisplayName(GET_INVENTORY_ALL + " - negative case : tenant id is non exist")
    @Test
    @Order(13)
    void getAllByClientId_NegativeCase_TenantIdIsNonExist() {
        this.getTemplate(
            () -> this.service.getAll((Long) getInput(NON_EXIST_TENANT_ID)), (inventoryList) -> {
                assert Objects.nonNull(inventoryList);
                assert ((List<?>)inventoryList).isEmpty();
            }).run();
    }

    @DisplayName(GET_INVENTORY_ALL + " - negative case : tenant id is negative")
    @Test
    @Order(14)
    void getAllByClientId_NegativeCase_TenantIdIsNegative() {
        this.getTemplate(
            () -> this.service.getAll((Long) getInput(NEGATIVE_TENANT_ID)), (inventoryList) -> {
                assert Objects.nonNull(inventoryList);
                assert ((List<?>)inventoryList).isEmpty();
            }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET PAGE">

    @DisplayName(GET_INVENTORY_PAGE + " - positive case : all things is right")
    @Test
    @Order(9)
    void getPage_PositiveCase_AllThingIsRight() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (InventoryEntity) item;
                        return  ingredient.isActive() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID));
                    });
                }).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - category - positive case : filter with exist name")
    @Test
    @Order(10)
    void getPage_PositiveCase_FilterWithExistName() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setName((String) getInput(NAME));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> inventoryPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  inventoryPage.stream().allMatch(item -> {
                        var     inventory = (InventoryEntity) item;
                        return  inventory.isActive() && Objects.equals(inventory.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(inventory.getName(), getInput(NAME));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - positive case : filter with exist unit type")
    @Test
    @Order(12)
    void getPage_PositiveCase_FilterWithExistUnitType() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnitType((String) getInput(UNIT_TYPE));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     inventory = (InventoryEntity) item;
                        return  inventory.isActive() && Objects.equals(inventory.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(inventory.getUnitType(), getInput(UNIT_TYPE));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " positive case : filter with exist unit")
    @Test
    @Order(13)
    void getPage_PositiveCase_FilterWithExistUnit() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnit((String) getInput(UNIT));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> inventoryPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  inventoryPage.stream().allMatch(item -> {
                        var     inventory = (InventoryEntity) item;
                        return  inventory.isActive() && Objects.equals(inventory.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(inventory.getUnit(), getInput(UNIT));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - positive case : filter with exist description")
    @Test
    @Order(14)
    void getPage_PositiveCase_FilterWithExistDescription() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setDescription((String) getInput(DESCRIPTION));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> inventoryPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  inventoryPage.stream().allMatch(item -> {
                        var     inventory = (IngredientEntity) item;
                        return  inventory.isCategory() && Objects.equals(inventory.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(inventory.getDescription(), getInput(DESCRIPTION));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - positive case : filter with exist updated at")
    @Test
    @Order(16)
    void getPage_PositiveCase_FilterWithExistUpdatedAt() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUpdateAt((String) getInput(UPDATED_AT));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> inventoryPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  inventoryPage.stream().allMatch(item -> {
                        var     inventory = (InventoryEntity) item;
                        return  inventory.isActive() && Objects.equals(inventory.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(inventory.getUpdateAt(), getInput(UPDATED_AT));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - negative case : filter with non exist name")
    @Test
    @Order(17)
    void getPage_NegativeCase_FilterWithNonExistName() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setName((String) getInput(NON_EXIST_NAME));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - category - negative case : filter with non exist unit type")
    @Test
    @Order(19)
    void getPageCategory_NegativeCase_FilterWithNonExistUnitType() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnitType((String) getInput(NON_EXIST_UNIT_TYPE));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - negative case : filter with non exist unit")
    @Test
    @Order(20)
    void getPageCategory_NegativeCase_FilterWithNonExistUnit() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnit((String) getInput(NON_EXIST_UNIT));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - negative case : filter with non exist description")
    @Test
    @Order(21)
    void getPageCategory_NegativeCase_FilterWithNonExistDescription() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setDescription((String) getInput(NON_EXIST_DESCRIPTION));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - negative case : filter with non exist updated at")
    @Test
    @Order(23)
    void getPage_NegativeCase_FilterWithNonExistUpdatedAt() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUpdateAt((String) getInput(NON_EXIST_UPDATED_AT));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - positive case : filter with a specific page number and page number")
    @Test
    @Order(24)
    void getPage_PositiveCase_FilterWithSpecificPageNumberAndPageSize() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setPage(1);
                    request.setSize(1);
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), 1);
                    assert Objects.equals(page.getSize(), 1);
                    assert Objects.equals(page.getSort().isSorted(), false);
                }
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - negative case : filter with a negative page")
    @Test
    @Order(25)
    void getPage_NegativeCase_FilterWithNegativePageNumber() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long)getInput(TENANT_ID));
                    request.setPage((Integer) getInput(NEGATIVE_PAGE_NUMBER));
                    request.setSize((Integer) getInput(PAGE_SIZE));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), CustomizePageRequest.MIN_PAGE_VALUE);
                    assert Objects.equals(page.getSize(), getInput(PAGE_SIZE));
                    assert Objects.equals(page.getSort().isSorted(), false);
                }
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - negative case : filter with a negative size")
    @Test
    @Order(26)
    void getPage_NegativeCase_FilterWithNegativePageSize() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long)getInput(TENANT_ID));
                    request.setPage((Integer) getInput(PAGE_NUMBER));
                    request.setSize((Integer) getInput(NEGATIVE_PAGE_NUMBER));
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), getInput(PAGE_NUMBER));
                    assert Objects.equals(page.getSize(), CustomizePageRequest.MAX_SIZE_VALUE);
                    assert Objects.equals(page.getSort().isSorted(), false);
                }
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - positive case : filter with ascending sort")
    @Test
    @Order(27)
    void getPage_PositiveCase_FilterWithAscendingSort() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long)getInput(TENANT_ID));
                    request.setPage((Integer) getInput(PAGE_NUMBER));
                    request.setSize((Integer) getInput(PAGE_SIZE));
                    request.setSort("id, asc");
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), getInput(PAGE_NUMBER));
                    assert Objects.equals(page.getSize(), getInput(PAGE_SIZE));
                    assert Objects.equals(page.getSort().isSorted(), true);
                }
        ).run();
    }

    @DisplayName(GET_INVENTORY_PAGE + " - positive case : filter with descending sort")
    @Test
    @Order(28)
    void getPage_PositiveCase_FilterWithDescendingSort() {
        this.getTemplate(
                () -> {
                    var request = (InventoryPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long)getInput(TENANT_ID));
                    request.setPage((Integer) getInput(PAGE_NUMBER));
                    request.setSize((Integer) getInput(PAGE_SIZE));
                    request.setSort("id, desc");
                    return this.service.getPage(filter(InventoryMapper.toEntity(request),ingredientService.getById(request.getIngredientId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), getInput(PAGE_NUMBER));
                    assert Objects.equals(page.getSize(), getInput(PAGE_SIZE));
                    assert Objects.equals(page.getSort().isSorted(), true);
                }
        ).run();
    }

    //</editor-fold>

    @Test
    void save() {
    }

    @Test
    void delete() {
    }
}