package com.fromlabs.inventory.supplierservice.imports.details;

import com.fromlabs.inventory.supplierservice.SupplierserviceApplication;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SupplierserviceApplication.class)
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImportDetailServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getById() {
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void getPage() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getByIdWithException() {
    }
}