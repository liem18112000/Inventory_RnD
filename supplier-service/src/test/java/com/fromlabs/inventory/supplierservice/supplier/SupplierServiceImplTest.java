package com.fromlabs.inventory.supplierservice.supplier;

import org.junit.jupiter.api.*;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SupplierServiceImplTest {

    @Mock
    private SupplierRepository repository;

    private SupplierService service;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void given_getById_when_idIsExist_then_getSupplier() {
        SupplierEntity entity = new SupplierEntity();
        entity.setId(1L);
        when(this.repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        this.service = new SupplierServiceImpl(this.repository);
        final var supplier = this.service.getById(1L);
        assertEquals(supplier.getId(), 1L);
    }

    @Test
    void given_getById_when_idIsNegative_then_supplierIsNull() {
        when(this.repository.findById(-1L)).thenReturn(Optional.empty());
        this.service = new SupplierServiceImpl(this.repository);
        final var supplier = this.service.getById(-1L);
        assertNull(supplier);
    }

    @Test
    void given_getById_when_idIsNotFound_then_supplierIsNull() {
        when(this.repository.findById((long) Integer.MAX_VALUE)).thenReturn(Optional.empty());
        this.service = new SupplierServiceImpl(this.repository);
        final var supplier = this.service.getById((long) Integer.MAX_VALUE);
        assertNull(supplier);
    }

    @Test
    void given_getByIdWithException_when_idIsExist_then_getSupplier() {
        SupplierEntity entity = new SupplierEntity();
        entity.setId(1L);
        when(this.repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        this.service = new SupplierServiceImpl(this.repository);
        final var supplier = assertDoesNotThrow(
                () -> this.service.getByIdWithException(1L));
        assertEquals(supplier.getId(), 1L);
    }

    @Test
    void given_getByIdWithException_when_idIsNegative_then_throwException() {
        when(this.repository.findById(-1L)).thenReturn(Optional.empty());
        this.service = new SupplierServiceImpl(this.repository);
        final var supplier = this.service.getById(-1L);
        assertNull(supplier);
    }

    @Test
    void given_getByIdWithException_when_idIsNotFound_then_supplierIsNull() {
        when(this.repository.findById((long) Integer.MAX_VALUE)).thenReturn(Optional.empty());
        this.service = new SupplierServiceImpl(this.repository);
        final var supplier = this.service.getById((long) Integer.MAX_VALUE);
        assertNull(supplier);
    }

    @Test
    void given_getByCode_when_codeIsExist_then_getSupplier() {
        final var code = "API_Group_01";
        SupplierEntity entity = new SupplierEntity();
        entity.setCode(code);
        when(this.repository. findByCode(code)).thenReturn(entity);
        final var supplier = this.service.getByCode(code);
        assertEquals(supplier.getCode(), code);
    }

    @Test
    void getByNotExistCode() {
        final var supplier = this.service.getByCode(String.valueOf(System.currentTimeMillis()));
        assertNull(supplier);
    }

    @Test
    void getByName() {
    }

    @Test
    void getPage() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getPageByFilter() {
    }

    @Test
    void getAllByFilter() {
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }
}