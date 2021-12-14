/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.common.helper.CustomizePageRequest;
import com.fromlabs.inventory.inventoryservice.common.template.UnitTestTemplateProcess;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientPageRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
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
import static com.fromlabs.inventory.inventoryservice.helper.TestKeyIndicator.*;
import static com.fromlabs.inventory.inventoryservice.helper.TestcaseName.*;
import static com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity.from;
import static com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientSpecification.filter;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Service test for Ingredient service
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientServiceTest {

    @Autowired
    private IngredientService service;

    private UnitTestTemplateProcess getTemplateUnitTest;

    //<editor-fold desc="SETUP">
    @BeforeEach
    void setUp() {
        this.getTemplateUnitTest = UnitTestTemplateProcess.builder()
                .build()
                .input(ID,          1L).input(NEGATIVE_ID,          -1L).input(NON_EXIST_ID,            99999L).input(CHILD_ID,    2L)
                .input(TENANT_ID,   1L).input(NEGATIVE_TENANT_ID,   -1L).input(NON_EXIST_TENANT_ID,     99999L)
                .input(PARENT_ID,   1L).input(NEGATIVE_PARENT_ID,   -1L).input(NON_EXIST_PARENT_ID,     99999L)
                .input(PAGE_NUMBER, 0) .input(NEGATIVE_PAGE_NUMBER, -1) .input(NON_EXIST_PAGE_NUMBER,   99999)
                .input(PAGE_SIZE,   10) .input(NEGATIVE_PAGE_SIZE,  -1) .input(NON_EXIST_PAGE_SIZE,     99999).input(ZERO_PAGE_SIZE, 0)
                .input(ASC_SORT,    "id, asc")  .input(NON_EXIST_ASC_SORT,      "nonExist, asc")
                .input(ASC_SORT,    "id, desc") .input(NON_EXIST_DESC_SORT,     "nonExist, desc")
                .input(NAME,        "Milk")     .input(NON_EXIST_NAME,          NON_EXIST_NAME).input(CHILD_NAME, "New Zealand Cow Milk")
                .input(CODE,        "11223344") .input(NON_EXIST_CODE,          NON_EXIST_CODE).input(CHILD_CODE, "NZCM")
                .input(DESCRIPTION, "Milk")     .input(NON_EXIST_DESCRIPTION,   NON_EXIST_DESCRIPTION)
                .input(UNIT,        "bottle")   .input(NON_EXIST_UNIT,          NON_EXIST_UNIT)
                .input(UNIT_TYPE,   "whole")    .input(NON_EXIST_UNIT_TYPE,     NON_EXIST_UNIT_TYPE)
                .input(CREATED_AT,  "2021-10-03 22:27:07").input(NON_EXIST_CREATED_AT, Instant.now().plusSeconds(3600).toString())
                .input(CHILD_CREATED_AT, "2021-10-03 22:27:06").input(CHILD_UPDATED_AT, "2021-11-20T05:17:06.343531900Z")
                .input(UPDATED_AT,  "2021-10-03 22:27:07").input(NON_EXIST_UPDATED_AT, Instant.now().plusSeconds(3600).toString())
                .input(PAGE_REQUEST, new IngredientPageRequest());
    }

    @AfterEach
    void tearDown() {
    }

    private UnitTestTemplateProcess getTemplate( Callable<Object> process, Consumer<Object> testCase) {
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

    @DisplayName(GET_INGREDIENT_BY_ID + " - positive case : all thing is right")
    @Test
    @Order(1)
    void getByID_PositiveCase_AllThingIsRight() {
        this.getTemplate(
            () -> this.service.get((Long) getInput(ID)), (ingredient) -> {
            assert Objects.nonNull(ingredient);
            assert Objects.equals(((IngredientEntity)ingredient).getId(), getInput(ID));
        }).run();
    }

    @DisplayName(GET_INGREDIENT_BY_ID + " - negative case : ID is negative")
    @Test
    @Order(2)
    void getByID_NegativeCase_IdIsNegative() {
        this.getTemplate(
            () -> this.service.get((Long) getInput(NEGATIVE_ID)), (ingredient) -> {
            assert Objects.isNull(ingredient);
        }).run();
    }

    @DisplayName(GET_INGREDIENT_BY_ID + " - negative case : ID is non-exist")
    @Test
    @Order(3)
    void getByID_NegativeCase_IdIsNonExist() {
        this.getTemplate(
            () -> this.service.get((Long) getInput(NON_EXIST_ID)), (ingredient) -> {
            assert Objects.isNull(ingredient);
        }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET BY CODE">

    @DisplayName(GET_INGREDIENT_BY_CODE + " - positive case : all thing is right")
    @Test
    @Order(4)
    void getByCode_PositiveCase_AllThingIsRight() {
        this.getTemplate(
        () -> this.service.get((String) getInput(CODE)), (ingredient) -> {
            assert Objects.nonNull(ingredient);
            assert Objects.equals(((IngredientEntity) ingredient).getCode(), getInput(CODE));
        }).run();
    }

    @DisplayName(GET_INGREDIENT_BY_CODE + " - negative case : Code is not exist")
    @Test
    @Order(5)
    void getByCode_NegativeCase_CodeIsNotExist() {
        this.getTemplate(
                () -> this.service.get((String) getInput(NON_EXIST_CODE)), (ingredient) -> {
                    assert Objects.isNull(ingredient);
                }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET BY NAME">

    @DisplayName(GET_INGREDIENT_BY_NAME + " - positive case : All thing is right")
    @Test
    @Order(5)
    void getByName_PositiveCase_AllThingIsRight() {
        this.getTemplate(
            () -> this.service.get((Long) getInput(TENANT_ID), (String) getInput(NAME)), (ingredient) -> {
            assert Objects.nonNull(ingredient);
            assert Objects.equals(((IngredientEntity) ingredient).getClientId(), getInput(TENANT_ID));
            assert Objects.equals(((IngredientEntity) ingredient).getName(), getInput(NAME));
        }).run();
    }

    @DisplayName(GET_INGREDIENT_BY_NAME + " - negative case : name is not exist")
    @Test
    @Order(6)
    void getByName_NegativeCase_NameIsNotExist() {
        this.getTemplate(
            () -> this.service.get((Long) getInput(TENANT_ID), (String) getInput(NON_EXIST_NAME)), (ingredient) -> {
            assert Objects.isNull(ingredient);
        }).run();
    }

    @DisplayName(GET_INGREDIENT_BY_NAME + " - negative case : tenant is not exist")
    @Test
    @Order(7)
    void getByName_NegativeCase_TenantIsNotExist() {
        this.getTemplate(
            () -> this.service.get((Long) getInput(TENANT_ID), (String) getInput(NON_EXIST_NAME)), (ingredient) -> {
            assert Objects.isNull(ingredient);
        }).run();
    }

    @DisplayName(GET_INGREDIENT_BY_NAME + " - negative case : tenant is negative")
    @Test
    @Order(8)
    void getByName_NegativeCase_TenantIsNegative() {
        this.getTemplate(
                () -> this.service.get((Long) getInput(NEGATIVE_TENANT_ID), (String) getInput(NAME)), (ingredient) -> {
                    assert Objects.isNull(ingredient);
                }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET PAGE CATEGORY">

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : all things is right")
    @Test
    @Order(9)
    void getPageCategory_PositiveCase_AllThingIsRight() {
        this.getTemplate(
            () -> {
                var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                request.setClientId((Long) getInput(TENANT_ID));
                return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
            }, bootstrap -> {
                Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                assert  ingredientPage.stream().allMatch(item -> {
                    var     ingredient = (IngredientEntity) item;
                    return  ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID));
                });
            }).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with exist name")
    @Test
    @Order(10)
    void getPageCategory_PositiveCase_FilterWithExistName() {
        this.getTemplate(
            () -> {
                var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                request.setClientId((Long) getInput(TENANT_ID));
                request.setName((String) getInput(NAME));
                return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
            }, bootstrap -> {
                Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                assert  ingredientPage.stream().allMatch(item -> {
                    var     ingredient = (IngredientEntity) item;
                    return  ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                            Objects.equals(ingredient.getName(), getInput(NAME));
                });
            }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with exist code")
    @Test
    @Order(11)
    void getPageCategory_PositiveCase_FilterWithExistCode() {
        this.getTemplate(
            () -> {
                var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                request.setClientId((Long) getInput(TENANT_ID));
                request.setCode((String) getInput(CODE));
                return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
            }, bootstrap -> {
                Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                assert  ingredientPage.stream().allMatch(item -> {
                    var     ingredient = (IngredientEntity) item;
                    return  ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                            Objects.equals(ingredient.getCode(), getInput(CODE));
                });
            }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with exist unit type")
    @Test
    @Order(12)
    void getPageCategory_PositiveCase_FilterWithExistUnitType() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnitType((String) getInput(UNIT_TYPE));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                assert  ingredientPage.stream().allMatch(item -> {
                    var     ingredient = (IngredientEntity) item;
                    return  ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                            Objects.equals(ingredient.getUnitType(), getInput(UNIT_TYPE));
                });
            }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with exist unit")
    @Test
    @Order(13)
    void getPageCategory_PositiveCase_FilterWithExistUnit() {
        this.getTemplate(
            () -> {
                var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                request.setClientId((Long) getInput(TENANT_ID));
                request.setUnit((String) getInput(UNIT));
                return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
            }, bootstrap -> {
                Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                assert  ingredientPage.stream().allMatch(item -> {
                    var     ingredient = (IngredientEntity) item;
                    return  ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                            Objects.equals(ingredient.getUnit(), getInput(UNIT));
                });
            }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with exist description")
    @Test
    @Order(14)
    void getPageCategory_PositiveCase_FilterWithExistDescription() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setDescription((String) getInput(DESCRIPTION));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(ingredient.getDescription(), getInput(DESCRIPTION));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with exist created at")
    @Test
    @Order(15)
    void getPageCategory_PositiveCase_FilterWithExistCreatedAt() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setCreateAt((String) getInput(CREATED_AT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(ingredient.getCreateAt(), getInput(CREATED_AT));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with exist updated at")
    @Test
    @Order(16)
    void getPageCategory_PositiveCase_FilterWithExistUpdatedAt() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUpdateAt((String) getInput(UPDATED_AT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(ingredient.getUpdateAt(), getInput(UPDATED_AT));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - negative case : filter with non exist name")
    @Test
    @Order(17)
    void getPageCategory_NegativeCase_FilterWithNonExistName() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setName((String) getInput(NON_EXIST_NAME));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - negative case : filter with non exist code")
    @Test
    @Order(18)
    void getPageCategory_NegativeCase_FilterWithNonExistCode() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setCode((String) getInput(NON_EXIST_CODE));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - negative case : filter with non exist unit type")
    @Test
    @Order(19)
    void getPageCategory_NegativeCase_FilterWithNonExistUnitType() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnitType((String) getInput(NON_EXIST_UNIT_TYPE));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - negative case : filter with non exist unit")
    @Test
    @Order(20)
    void getPageCategory_NegativeCase_FilterWithNonExistUnit() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnit((String) getInput(NON_EXIST_UNIT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - negative case : filter with non exist description")
    @Test
    @Order(21)
    void getPageCategory_NegativeCase_FilterWithNonExistDescription() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setDescription((String) getInput(NON_EXIST_DESCRIPTION));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - negative case : filter with non exist created at")
    @Test
    @Order(22)
    void getPageCategory_NegativeCase_FilterWithNonExistCreatedAt() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setCreateAt((String) getInput(NON_EXIST_CREATED_AT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - negative case : filter with non exist updated at")
    @Test
    @Order(23)
    void getPageCategory_NegativeCase_FilterWithNonExistUpdatedAt() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUpdateAt((String) getInput(NON_EXIST_UPDATED_AT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with a specific page number and page number")
    @Test
    @Order(24)
    void getPageCategory_PositiveCase_FilterWithSpecificPageNumberAndPageSize() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setPage(1);
                    request.setSize(1);
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), 1);
                    assert Objects.equals(page.getSize(), 1);
                    assert Objects.equals(page.getSort().isSorted(), false);
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - negative case : filter with a negative page")
    @Test
    @Order(25)
    void getPageCategory_NegativeCase_FilterWithNegativePageNumber() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long)getInput(TENANT_ID));
                    request.setPage((Integer) getInput(NEGATIVE_PAGE_NUMBER));
                    request.setSize((Integer) getInput(PAGE_SIZE));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), CustomizePageRequest.MIN_PAGE_VALUE);
                    assert Objects.equals(page.getSize(), getInput(PAGE_SIZE));
                    assert Objects.equals(page.getSort().isSorted(), false);
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - negative case : filter with a negative size")
    @Test
    @Order(26)
    void getPageCategory_NegativeCase_FilterWithNegativePageSize() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long)getInput(TENANT_ID));
                    request.setPage((Integer) getInput(PAGE_NUMBER));
                    request.setSize((Integer) getInput(NEGATIVE_PAGE_NUMBER));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), getInput(PAGE_NUMBER));
                    assert Objects.equals(page.getSize(), CustomizePageRequest.MAX_SIZE_VALUE);
                    assert Objects.equals(page.getSort().isSorted(), false);
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with ascending sort")
    @Test
    @Order(27)
    void getPageCategory_PositiveCase_FilterWithAscendingSort() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long)getInput(TENANT_ID));
                    request.setPage((Integer) getInput(PAGE_NUMBER));
                    request.setSize((Integer) getInput(PAGE_SIZE));
                    request.setSort("id, asc");
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), getInput(PAGE_NUMBER));
                    assert Objects.equals(page.getSize(), getInput(PAGE_SIZE));
                    assert Objects.equals(page.getSort().isSorted(), true);
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - category - positive case : filter with descending sort")
    @Test
    @Order(28)
    void getPageCategory_PositiveCase_FilterWithDescendingSort() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setClientId((Long)getInput(TENANT_ID));
                    request.setPage((Integer) getInput(PAGE_NUMBER));
                    request.setSize((Integer) getInput(PAGE_SIZE));
                    request.setSort("id, desc");
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    var page = (Page<?>) bootstrap;
                    assert Objects.equals(page.getNumber(), getInput(PAGE_NUMBER));
                    assert Objects.equals(page.getSize(), getInput(PAGE_SIZE));
                    assert Objects.equals(page.getSort().isSorted(), true);
                }
        ).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET ALL CATEGORY">

    @DisplayName(GET_INGREDIENT_ALL + " - category - positive case : all things is right")
    @Test
    @Order(29)
    void getAllCategory_PositiveCase_AllThingIsRight() {
        this.getTemplate(
            () -> this.service.getAll((Long) getInput(TENANT_ID)), (ingredientList) -> {
            assert Objects.nonNull(ingredientList);
            assert ((List<?>) ingredientList).stream().allMatch(t -> ((IngredientEntity)t).isCategory());
            assert !((List<?>) ingredientList).isEmpty();
        }).run();
    }

    @DisplayName(GET_INGREDIENT_ALL + " - category - negative case : tenant id is not exist")
    @Test
    @Order(30)
    void getAllCategory_NegativeCase_TenantIdIsNotExist() {
        this.getTemplate(
                () -> this.service.getAll((Long) getInput(NON_EXIST_TENANT_ID)), (ingredientList) -> {
                    assert Objects.nonNull(ingredientList);
                    assert ((List<?>) ingredientList).isEmpty();
                }).run();
    }

    @DisplayName(GET_INGREDIENT_ALL + " - category - negative case : tenant id is negative")
    @Test
    @Order(31)
    void getAllCategory_NegativeCase_TenantIdIsNegative() {
        this.getTemplate(
                () -> this.service.getAll((Long) getInput(NEGATIVE_TENANT_ID)), (ingredientList) -> {
                    assert Objects.nonNull(ingredientList);
                    assert ((List<?>) ingredientList).isEmpty();
                }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET TYPE PAGE">

    @DisplayName(GET_INGREDIENT_PAGE + " - type - positive case : all things is right")
    @Test
    @Order(32)
    void getPageType_PositiveCase_AllThingIsRight() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID));
                    });
                }).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - positive case : filter with exist name")
    @Test
    @Order(33)
    void getPageType_PositiveCase_FilterWithExistName() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setName((String)   getInput(CHILD_NAME));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID))
                                && Objects.equals(ingredient.getName(), getInput(CHILD_NAME));
                    });
                }).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - positive case : filter with exist code")
    @Test
    @Order(34)
    void getPageType_PositiveCase_FilterWithExistCode() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setCode((String)  getInput(CHILD_CODE));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID))
                                && Objects.equals(ingredient.getCode(), getInput(CHILD_CODE));
                    });
                }).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - positive case : filter with exist unit")
    @Test
    @Order(35)
    void getPageType_PositiveCase_FilterWithExistUnit() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnit((String)   getInput(UNIT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID))
                                && Objects.equals(ingredient.getUnit(), getInput(UNIT));
                    });
                }).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - positive case : filter with exist unit type")
    @Test
    @Order(36)
    void getPageType_PositiveCase_FilterWithExistUnitType() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnitType((String)getInput(UNIT_TYPE));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID))
                                && Objects.equals(ingredient.getUnitType(), getInput(UNIT_TYPE));
                    });
                }).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - positive case : filter with exist description")
    @Test
    @Order(37)
    void getPageType_PositiveCase_FilterWithExistDescription() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setDescription((String) getInput(DESCRIPTION));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                                ingredient.getDescription().contains((CharSequence) getInput(DESCRIPTION));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - positive case : filter with exist created at")
    @Test
    @Order(38)
    void getPageType_PositiveCase_FilterWithExistCreatedAt() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setCreateAt((String) getInput(CHILD_CREATED_AT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(ingredient.getCreateAt(), getInput(CHILD_CREATED_AT));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - positive case : filter with exist updated at")
    @Test
    @Order(39)
    void getPageType_PositiveCase_FilterWithExistUpdatedAt() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUpdateAt((String) getInput(CHILD_UPDATED_AT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> {
                    Page<?> ingredientPage = assertPageIsNotNullAndNotEmpty((Page<?>) bootstrap);
                    assert  ingredientPage.stream().allMatch(item -> {
                        var     ingredient = (IngredientEntity) item;
                        return  !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID)) &&
                                Objects.equals(ingredient.getUpdateAt(), getInput(CHILD_UPDATED_AT));
                    });
                }
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - negative case : filter with non exist name")
    @Test
    @Order(40)
    void getPageType_NegativeCase_FilterWithNonExistName() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setName((String) getInput(NON_EXIST_NAME));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - negative case : filter with non exist code")
    @Test
    @Order(41)
    void getPageType_NegativeCase_FilterWithNonExistCode() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setCode((String) getInput(NON_EXIST_CODE));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - negative case : filter with non exist unit type")
    @Test
    @Order(42)
    void getPageType_NegativeCase_FilterWithNonExistUnitType() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnitType((String) getInput(NON_EXIST_UNIT_TYPE));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - negative case : filter with non exist unit")
    @Test
    @Order(43)
    void getPageType_NegativeCase_FilterWithNonExistUnit() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUnit((String) getInput(NON_EXIST_UNIT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - negative case : filter with non exist description")
    @Test
    @Order(44)
    void getPageType_NegativeCase_FilterWithNonExistDescription() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setDescription((String) getInput(NON_EXIST_DESCRIPTION));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - negative case : filter with non exist created at")
    @Test
    @Order(45)
    void getPageType_NegativeCase_FilterWithNonExistCreatedAt() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setCreateAt((String) getInput(NON_EXIST_CREATED_AT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    @DisplayName(GET_INGREDIENT_PAGE + " - type - negative case : filter with non exist updated at")
    @Test
    @Order(46)
    void getPageType_NegativeCase_FilterWithNonExistUpdatedAt() {
        this.getTemplate(
                () -> {
                    var request = (IngredientPageRequest) getInput(PAGE_REQUEST);
                    request.setParentId((Long) getInput(PARENT_ID));
                    request.setClientId((Long) getInput(TENANT_ID));
                    request.setUpdateAt((String) getInput(NON_EXIST_UPDATED_AT));
                    return this.service.getPage(filter(from(request), service.get(request.getParentId())), request.getPageable());
                }, bootstrap -> assertPageIsNotNullAndEmpty((Page<?>) bootstrap)
        ).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET ALL TYPE BY PARENT ID">

    @DisplayName(GET_INGREDIENT_ALL_BY_PARENT + " - type - positive case : all things is right")
    @Test
    @Order(37)
    void getAllChildByParentId_PositiveCase_AllThingIsRight() {
        this.getTemplate(
            () -> this.service.getAll((Long) getInput(TENANT_ID), (Long) getInput(PARENT_ID)), (ingredientList) -> {
            assert Objects.nonNull(ingredientList);
            assert !((List<?>) ingredientList).isEmpty();
            assert ((List<?>) ingredientList).stream().allMatch(item -> {
                var ingredient = (IngredientEntity) item;
                return !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID))
                        && Objects.equals(ingredient.getParent().getId(), getInput(PARENT_ID));
            } );
        }).run();
    }

    @DisplayName(GET_INGREDIENT_ALL_BY_PARENT + " - type - negative case : tenant id is not exist")
    @Test
    @Order(38)
    void getAllChildByParentId_NegativeCase_TenantIdIsNotExist() {
        this.getTemplate(
                () -> this.service.getAll((Long) getInput(NON_EXIST_TENANT_ID), (Long) getInput(PARENT_ID)), (ingredientList) -> {
                    assert Objects.nonNull(ingredientList);
                    assert ((List<?>) ingredientList).isEmpty();
                }).run();
    }

    @DisplayName(GET_INGREDIENT_ALL_BY_PARENT + " - type - negative case : parent id is not exist")
    @Test
    @Order(39)
    void getAllChildByParentId_NegativeCase_ParentIdIsNotExist() {
        this.getTemplate(
                () -> this.service.getAll((Long) getInput(TENANT_ID), (Long) getInput(NON_EXIST_PARENT_ID)), (ingredientList) -> {
                    assert Objects.nonNull(ingredientList);
                    assert ((List<?>) ingredientList).isEmpty();
                }).run();
    }

    @DisplayName(GET_INGREDIENT_ALL_BY_PARENT + " - type - negative case : tenant id negative")
    @Test
    @Order(40)
    void getAllChildByParentId_NegativeCase_TenantIdIsNegative() {
        this.getTemplate(
                () -> this.service.getAll((Long) getInput(NEGATIVE_TENANT_ID), (Long) getInput(PARENT_ID)), (ingredientList) -> {
                    assert Objects.nonNull(ingredientList);
                    assert ((List<?>) ingredientList).isEmpty();
                }).run();
    }

    @DisplayName(GET_INGREDIENT_ALL_BY_PARENT + " - type - negative case : parent id is negative")
    @Test
    @Order(41)
    void getAllChildByParentId_NegativeCase_ParentIdIsNegative() {
        this.getTemplate(
                () -> this.service.getAll((Long) getInput(TENANT_ID), (Long) getInput(NEGATIVE_PARENT_ID)), (ingredientList) -> {
                    assert Objects.nonNull(ingredientList);
                    assert ((List<?>) ingredientList).isEmpty();
                }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET ALL CHILD">

    @DisplayName(GET_INGREDIENT_ALL + " - type - positive case : all things is right")
    @Test
    @Order(42)
    void getAllChild_PositiveCase_AllThingIsRight() {
        this.getTemplate(
                () -> this.service.getAllChild((Long) getInput(TENANT_ID)), (ingredientList) -> {
                    assert Objects.nonNull(ingredientList);
                    assert !((List<?>) ingredientList).isEmpty();
                    assert ((List<?>) ingredientList).stream().allMatch(item -> {
                        var ingredient = (IngredientEntity) item;
                        return !ingredient.isCategory() && Objects.equals(ingredient.getClientId(), getInput(TENANT_ID));
                    } );
                }).run();
    }

    @DisplayName(GET_INGREDIENT_ALL + " - type - negative case : tenant id is not exist")
    @Test
    @Order(43)
    void getAllChild_NegativeCase_TenantIdIsNotExist() {
        this.getTemplate(
                () -> this.service.getAll((Long) getInput(NON_EXIST_TENANT_ID), (Long) getInput(PARENT_ID)), (ingredientList) -> {
                    assert Objects.nonNull(ingredientList);
                    assert ((List<?>) ingredientList).isEmpty();
                }).run();
    }

    @DisplayName(GET_INGREDIENT_ALL + " - type - negative case : tenant id is negative")
    @Test
    @Order(44)
    void getAllChild_NegativeCase_TenantIdIsNegative() {
        this.getTemplate(
                () -> this.service.getAll((Long) getInput(NEGATIVE_TENANT_ID), (Long) getInput(PARENT_ID)), (ingredientList) -> {
                    assert Objects.nonNull(ingredientList);
                    assert ((List<?>) ingredientList).isEmpty();
                }).run();
    }

    //</editor-fold>

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    //<editor-fold desc="GET CONFIG BY ID">

    @DisplayName(GET_INGREDIENT_CONFIG_BY_ID + " - positive case : all thing is right")
    @Test
    @Order(45)
    void getConfig_PositiveCase_AllThingIsRight() {
        this.getTemplate(
                () -> this.service.getConfig((Long) getInput(ID)), (config) -> {
                    assert Objects.nonNull(config);
                    assert Objects.equals(((IngredientConfigEntity) config).getId(), getInput(ID));
                }).run();
    }

    // TODO: Try to fix the bugs
    @Disabled
    @DisplayName(GET_INGREDIENT_CONFIG_BY_ID + " - negative case : id is negative")
    @Test
    @Order(46)
    void getConfig_NegativeCase_IdIsNegative() {
        this.getTemplate(
                () -> this.service.getConfig((Long) getInput(NEGATIVE_ID)), (config) -> {
                    assert Objects.isNull(config);
                }).run();
    }

    // TODO: Try to fix the bugs
    @Disabled
    @DisplayName(GET_INGREDIENT_CONFIG_BY_ID + " - negative case : id is non exist")
    @Test
    @Order(47)
    void getConfig_NegativeCase_IdIsNonExist() {
        this.getTemplate(
                () -> this.service.getConfig((Long) getInput(NON_EXIST_ID)), (config) -> {
                    assert Objects.isNull(config);
                }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET CONFIG BY INGREDIENT ID">

    @DisplayName(GET_INGREDIENT_CONFIG_BY_INGREDIENT + " - positive case : all thing is right")
    @Test
    @Order(48)
    void GetConfigByIngredientId_PositiveCase_AllThingIsRight() {
        this.getTemplate(
                () -> this.service.getConfig((Long) getInput(TENANT_ID), service.get((Long) getInput(ID))), (config) -> {
                    assert Objects.isNull(config);
                }).run();
    }

    @DisplayName(GET_INGREDIENT_CONFIG_BY_INGREDIENT + " - negative case : tenant id is not exist")
    @Test
    @Order(49)
    void GetConfigByIngredientId_NegativeCase_TenantIdIsNotExist() {
        this.getTemplate(
                () -> this.service.getConfig((Long) getInput(NON_EXIST_TENANT_ID), service.get((Long) getInput(ID))), (config) -> {
                    assert Objects.isNull(config);
                }).run();
    }

    @DisplayName(GET_INGREDIENT_CONFIG_BY_INGREDIENT + " - negative case : ingredient id is not exist")
    @Test
    @Order(50)
    void GetConfigByIngredientId_NegativeCase_IngredientIdIsNotExist() {
        this.getTemplate(
                () -> this.service.getConfig((Long) getInput(TENANT_ID), service.get((Long) getInput(NON_EXIST_ID))), (config) -> {
                    assert Objects.isNull(config);
                }).run();
    }

    //</editor-fold>

    //<editor-fold desc="GET ALL CONFIG">

    @DisplayName(GET_INGREDIENT_CONFIG_ALL + " - positive case : all thing is right")
    @Test
    @Order(51)
    void getAllConfig_PositiveCase_AllThingIsRight() {
        this.getTemplate(
                () -> this.service.getAllConfig((Long) getInput(TENANT_ID)), (configList) -> {
                    assert Objects.nonNull(configList);
                    assert !((List<?>) configList).isEmpty();
                    assert ((List<?>) configList).stream().allMatch(item -> Objects.equals(((IngredientConfigEntity)item).getClientId(), getInput(TENANT_ID)));
                }).run();
    }

    @DisplayName(GET_INGREDIENT_CONFIG_ALL + " - negative case : tenant id is non exist")
    @Test
    @Order(52)
    void getAllConfig_NegativeCase_TenantIdIsNotExist() {
        this.getTemplate(
                () -> this.service.getAllConfig((Long) getInput(NON_EXIST_TENANT_ID)), (configList) -> {
                    assert Objects.nonNull(configList);
                    assert ((List<?>) configList).isEmpty();
                }).run();
    }

    //</editor-fold>

    @Test
    void getPageConfig() {
    }

    @Test
    void saveConfig() {
    }

    @Test
    void deleteConfig() {
    }
}