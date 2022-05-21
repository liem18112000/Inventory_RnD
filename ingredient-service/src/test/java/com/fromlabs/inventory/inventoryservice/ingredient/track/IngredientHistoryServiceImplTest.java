package com.fromlabs.inventory.inventoryservice.ingredient.track;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification;
import com.fromlabs.inventory.inventoryservice.common.specifications.SearchCriteria;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.fromlabs.inventory.inventoryservice.common.specifications.SearchCriteria.criteriaEqual;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientHistoryServiceImplTest {

    @Mock
    private IngredientHistoryRepository repository;

    @Test
    void getById() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        Mockito.when(this.repository.findById(entity.getId()))
                .thenReturn(Optional.of(entity));
        final var service = new IngredientHistoryServiceImpl(this.repository);
        final var actual = service.getById(entity.getId());
        Assertions.assertEquals(entity.getId(), actual.getId());
    }

    @Test
    void getPage() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        final var spec = BaseSpecification
                .<IngredientHistoryEntity>Spec(criteriaEqual(
                        "clientId", entity.getClientId()));
        final var pageable = Pageable.ofSize(10);
        Mockito.when(this.repository
                        .findAll(spec, pageable))
                .thenReturn(new PageImpl<>(List.of(entity)));
        final var service = new IngredientHistoryServiceImpl(this.repository);
        final var actuals = service
                .getPage(spec, pageable);
        actuals.forEach(actual ->
                Assertions.assertEquals(entity.getClientId(), actual.getClientId()));
    }

    @Test
    void getAll() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        Mockito.when(this.repository
                        .findAllByClientId(entity.getClientId()))
                .thenReturn(List.of(entity));
        final var service = new IngredientHistoryServiceImpl(this.repository);
        final var actuals = service
                .getAll(entity.getClientId());
        actuals.forEach(actual ->
                Assertions.assertEquals(entity.getClientId(), actual.getClientId()));
    }

    @Test
    void getAllBySpec() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        final var spec = BaseSpecification
                .<IngredientHistoryEntity>Spec(criteriaEqual(
                        "clientId", entity.getClientId()));
        Mockito.when(this.repository
                        .findAll(spec))
                .thenReturn(List.of(entity));
        final var service = new IngredientHistoryServiceImpl(this.repository);
        final var actuals = service
                .getAll(spec);
        actuals.forEach(actual ->
                Assertions.assertEquals(entity.getClientId(), actual.getClientId()));
    }

    @Test
    void getByIdWithException() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        Mockito.when(this.repository.findById(entity.getId()))
                .thenReturn(Optional.of(entity));
        final var service = new IngredientHistoryServiceImpl(this.repository);
        final var actual = Assertions.assertDoesNotThrow(
                () -> service.getByIdWithException(entity.getId()));
        Assertions.assertEquals(entity.getId(), actual.getId());
    }

    @Test
    void getByIdWithExceptionWhenObjectIsNull() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        Mockito.when(this.repository.findById(entity.getId()))
                .thenReturn(Optional.of(entity));
        final var service = new IngredientHistoryServiceImpl(this.repository);
        final var actual = Assertions.assertDoesNotThrow(
                () -> service.getByIdWithException(entity.getId()));
        Assertions.assertEquals(entity.getId(), actual.getId());
    }

    @Test
    void getByIngredient() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        var ingredient = new IngredientEntity();
        ingredient.setId(2L);
        Mockito.when(this.repository.findAllByClientIdAndIngredient(
                entity.getClientId(), ingredient))
                .thenReturn(List.of(entity));
        final var service = new IngredientHistoryServiceImpl(this.repository);
        final var actuals = service.getByIngredient(
                        entity.getClientId(), ingredient);
    }

    @Test
    void save() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        Mockito.when(this.repository.save(entity))
                .thenReturn(entity);
        final var service = new IngredientHistoryServiceImpl(this.repository);
        final var actual = Assertions.assertDoesNotThrow(
                () -> service.save(entity));
        Assertions.assertEquals(entity.getId(), actual.getId());
    }

    @Test
    void delete() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        final var service = new IngredientHistoryServiceImpl(this.repository);
        Assertions.assertDoesNotThrow(
                () -> service.delete(entity));
    }
}