/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.common.template.UnitTestTemplateProcess;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemPageRequest;
import com.fromlabs.inventory.inventoryservice.item.mapper.ItemMapper;
import com.fromlabs.inventory.inventoryservice.item.specification.ItemSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;
import java.util.function.Consumer;

import static com.fromlabs.inventory.inventoryservice.common.template.UnitTestTemplateProcess.getInput;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Test runner for item service test
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"liem-local", "dev"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemServiceTest {

    @Autowired
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getById() {
        getByIdTemplate(item -> {
            assert Objects.nonNull(item);
            assert Objects.equals(((ItemEntity)item).getId(), getInput("id"));
        }).run();
    }

    private UnitTestTemplateProcess getByIdTemplate(Consumer<Object> testCase) {
        return UnitTestTemplateProcess.builder()
                .process(() -> this.itemService.getById((Long) getInput("id")))
                .asserts(testCase).build()
                .input("id", 1L).input("clientId", 1L);
    }
    //<editor-fold desc="Get item by code">
    @DisplayName("Get item by code" + " - positive case : all thing is right")
    @Test
    void getByCode_Postive_AllThingsIsRight() {
        // get item from service
        var item = this.itemService.getByCode(1L,"QLGM01");
        assert Objects.nonNull(item);
        assert Objects.equals(item.getClientId(),1L);
        assert Objects.equals(item.getCode(),"QLGM01");
    }
    @DisplayName("Get item by code" + " - negative case : item code is not exist")
    @Test
    void getByCode_Negative_CodeIsNotExist() {
        // get item from service
        var item = this.itemService.getByCode(1L,"LIEMGAUGAU");
        assert Objects.isNull(item);
    }
    @DisplayName("Get item by code" + " - negative case : tenant is not exist")
    @Test
    void getByCode_Negative_TenantIsNotExist() {
        // get item from service
        var item = this.itemService.getByCode(666L,"QLGM01");
        assert Objects.isNull(item);
    }
    //</editor-fold>

    //<editor-fold desc="get item by name">
    @DisplayName("Get item by name" + " - positive case : all thing is right")
    @Test
    void getByName_Postive_AllThingsIsRight() {
        // get item by name
        var items = this.itemService.getByName(1L,"British Coca cola");
        assert Objects.nonNull(items);
        assert !items.isEmpty();

        assert items.stream().allMatch(item -> item.getClientId().equals(1L) &&
                item.getName().equals("British Coca cola"));
    }
    //</editor-fold>

    //<editor-fold desc="get all by client ID">
    @DisplayName("Get all by client ID" + " - positive case : all thing is right")
    @Test
    void getAll_Postive_AllThingsIsRight() {
        var items = this.itemService.getAll(1L);
        assert Objects.nonNull(items);
        assert !items.isEmpty();
        assert items.stream().allMatch(item -> Objects.equals(item.getClientId(),1L) );
    }
    @DisplayName("Get all by client ID" + " - negative case : client ID code is not exist")
    @Test
    void getAll_Negative_CodeIsNotExist() {
        var items = this.itemService.getAll(666L);
        assert Objects.nonNull(items);
        assert items.isEmpty();
    }
    //</editor-fold>

    //<editor-fold desc="Get page item>
    @DisplayName("Get page by clientID" + " - positive case : all thing is right")
    @Test
    void getPage_PositiveCase_AllThingIsRight() {
        var request = new ItemPageRequest();
        request.setClientId(1L);
        var page = this.itemService.getPage(ItemSpecification.filter(ItemMapper.toEntity(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().allMatch(item->{ return ((ItemEntity)item).getClientId().equals(1L); } );
    }
    @DisplayName("Get page filter with exist name" + " - positive case : all thing is right")
    @Test
    void getPage_PositiveCase_FilterWithExistName() {
        var request = new ItemPageRequest();
        request.setName("New Zealand cow milk");
        request.setClientId(1L);
        var page = this.itemService.getPage(ItemSpecification.filter(ItemMapper.toEntity(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().allMatch(item->{ return ((ItemEntity)item).getClientId().equals(1L) && ((ItemEntity)item).getName().equals("New Zealand cow milk"); } );
    }
    @DisplayName("Get page filter with exist code" + " - positive case : all thing is right")
    @Test
    void getPage_PositiveCase_FilterWithExistCode() {
        var request = new ItemPageRequest();
        request.setCode("QLGM01");
        request.setClientId(1L);
        var page = this.itemService.getPage(ItemSpecification.filter(ItemMapper.toEntity(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().allMatch(item->{ return ((ItemEntity)item).getClientId().equals(1L) && ((ItemEntity)item).getCode().equals("QLGM01"); } );
    }
    @DisplayName("Get page filter with exist unit type" + " - positive case : all thing is right")
    @Test
    void getPage_PositiveCase_FilterWithExistUnitType() {
        var request = new ItemPageRequest();
        request.setUnitType("whole");
        request.setClientId(1L);
        var page = this.itemService.getPage(ItemSpecification.filter(ItemMapper.toEntity(request), null), request.getPageable());
        assert Objects.nonNull(page);
        assert ((Page<?>)page).stream().allMatch(item->{ return ((ItemEntity)item).getClientId().equals(1L) && ((ItemEntity)item).getUnitType().equals("whole"); } );
    }


    //</editor-fold>

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void deleteAllByIds() {
    }

    @Test
    void getAllByIngredient() {
    }
}
