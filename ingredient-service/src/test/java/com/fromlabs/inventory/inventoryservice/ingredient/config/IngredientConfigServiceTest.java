package com.fromlabs.inventory.inventoryservice.ingredient.config;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.common.specifications.BaseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.fromlabs.inventory.inventoryservice.common.specifications.SearchCriteria.criteriaEqual;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IngredientConfigServiceTest {

    @Mock
    private IngredientConfigRepository repository;

    @Test
    public void getPageWithClientId() {
        var config = new IngredientConfigEntity();
        final var pageable = Pageable.ofSize(10);
        config.setClientId(1L);
        when(this.repository.findAllByClientId(
                config.getClientId(), pageable))
                .thenReturn(new PageImpl<>(List.of(config)));
        var service = new IngredientConfigServiceImpl(repository);
        final var page = service.getPage(
                config.getClientId(), pageable);
        page.forEach(item -> {
            Assertions.assertEquals(config.getClientId(), item.getClientId());
        });
    }

    @Test
    public void getPageWithSpec() {
        var config = new IngredientConfigEntity();
        config.setClientId(1L);
        final var pageable = Pageable.ofSize(10);
        final var spec = BaseSpecification.
                <IngredientConfigEntity>Spec(criteriaEqual(
                        "clientId", config.getClientId()));
        when(this.repository.findAll(
                spec, pageable))
                .thenReturn(new PageImpl<>(List.of(config)));
        var service = new IngredientConfigServiceImpl(repository);
        final var page = service.getPage(
                spec, pageable);
        page.forEach(item -> {
            Assertions.assertEquals(config.getClientId(), item.getClientId());
        });
    }

    @Test
    public void getAllWithSpec() {
        var config = new IngredientConfigEntity();
        config.setClientId(1L);
        final var spec = BaseSpecification.
                <IngredientConfigEntity>Spec(criteriaEqual(
                "clientId", config.getClientId()));
        when(this.repository.findAll(spec))
                .thenReturn(List.of(config));
        var service = new IngredientConfigServiceImpl(repository);
        final var list = service.getAll(spec);
        list.forEach(item -> {
            Assertions.assertEquals(config.getClientId(), item.getClientId());
        });
    }
}
