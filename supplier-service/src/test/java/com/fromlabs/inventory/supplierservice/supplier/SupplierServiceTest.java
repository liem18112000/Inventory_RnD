package com.fromlabs.inventory.supplierservice.supplier;

import com.fromlabs.inventory.supplierservice.SupplierserviceApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SupplierserviceApplication.class)
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SupplierServiceTest {

    @Autowired
    private SupplierService service;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    //<editor-fold desc="Gte supplier by id">

    @Order(1)
    @DisplayName("Positive: get supplier by id")
    @Test
    void getByValidId() {
        final var supplier = this.service.getById(1L);
        assertNotNull(supplier);
        assertEquals(supplier.getId(), 1L);
    }

    @Order(2)
    @DisplayName("Negative: get supplier by negative id")
    @Test
    void getByNegativeId() {
        final var supplier = this.service.getById(-1L);
        assertNull(supplier);
    }

    @Order(3)
    @DisplayName("Negative: get supplier by not exist id")
    @Test
    void getByNotExistId() {
        final var supplier = this.service.getById((long) Integer.MAX_VALUE);
        assertNull(supplier);
    }

    //</editor-fold>

    //<editor-fold desc="Description">

    @Order(4)
    @DisplayName("Positive: get supplier by code")
    @Test
    void getByValidCode() {
        final var supplier = this.service.getByCode("API_Group_01");
        assertNotNull(supplier);
        assertEquals(supplier.getCode(), "API_Group_01");
    }

    @Order(5)
    @DisplayName("Negative: get supplier by not exist code")
    @Test
    void getByNotExistCode() {
        final var supplier = this.service.getByCode(String.valueOf(System.currentTimeMillis()));
        assertNull(supplier);
    }

    //</editor-fold>

    //<editor-fold desc="Get by name">

    @Order(6)
    @DisplayName("Positive: get supplier by name and client")
    @Test
    void getByNameAndClientId() {
        final var name = "API_Group_01";
        final var supplier = this.service.getByName(1L, name);
        assertTrue(supplier.stream().allMatch(s -> s.getClientId().equals(1L) && s.getName().equals(name)));
    }

    @Order(7)
    @DisplayName("Negative: get supplier by not exist name and client")
    @Test
    void getByNotExistNameAndClientId() {
        final var name = String.valueOf(System.currentTimeMillis());
        final var supplier = this.service.getByName(1L, name);
        assertTrue(supplier.isEmpty());
    }

    @Order(8)
    @DisplayName("Negative: get supplier by valid name and negative client")
    @Test
    void getByNameAndNegativeClientId() {
        final var name = "API_Group_01";
        final var supplier = this.service.getByName(-1L, name);
        assertTrue(supplier.isEmpty());
    }

    @Order(9)
    @DisplayName("Negative: get supplier by valid name and not exist client")
    @Test
    void getByNameAndNotExistClientId() {
        final var name = "API_Group_01";
        final var supplier = this.service.getByName((long) Integer.MAX_VALUE, name);
        assertTrue(supplier.isEmpty());
    }

    //</editor-fold>

    //<editor-fold desc="Get All supplier group">

    @Order(10)
    @DisplayName("Positive: get all suppliers by tenant id")
    @Test
    void getAllGroupByTenantId() {
        final var tenantId = 1L;
        final var suppliers = this.service.getAll(tenantId);
        assertTrue(suppliers.stream().allMatch(s -> s.getClientId().equals(tenantId)));
    }

    @Order(11)
    @DisplayName("Negative: get all supplier by negative tenant id")
    @Test
    void getAllGroupByNegativeTenantId() {
        final var tenantId = -1L;
        final var suppliers = this.service.getAll(tenantId);
        assertTrue(suppliers.isEmpty());
    }

    @Order(12)
    @DisplayName("Negative: get all supplier by not exist tenant id")
    @Test
    void getAllGroupByNotExistTenantId() {
        final var tenantId = (long) Integer.MAX_VALUE;
        final var suppliers = this.service.getAll(tenantId);
        assertTrue(suppliers.isEmpty());
    }

    //</editor-fold>

    @Test
    void getPage() {
    }

    @Test
    void testGetPage() {
    }

    @Test
    void testGetAll() {
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }
}