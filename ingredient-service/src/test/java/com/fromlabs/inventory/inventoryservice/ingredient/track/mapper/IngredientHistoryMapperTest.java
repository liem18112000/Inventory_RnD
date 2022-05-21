package com.fromlabs.inventory.inventoryservice.ingredient.track.mapper;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.event.IngredientEventEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.event.IngredientEventService;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatus;
import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientHistoryMapperTest {

    @Autowired
    private IngredientEventService eventService;

    @Mock
    private IngredientService ingredientService;

    @Test
    public void toDto() {
        var entity = new IngredientHistoryEntity();
        final var timestamp = Instant.now().toString();
        var event = new IngredientEventEntity();
        final var ingredient = new IngredientEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setActorName("ActorName");
        entity.setActorRole("ActorRole");
        entity.setDescription("Description");
        entity.setEventStatus("success");
        entity.setEvent(event);
        entity.setExtraInformationClass("ExtraInformationClass");
        entity.setExtraInformation("ExtraInformation");
        entity.setTrackTimestamp(timestamp);
        entity.setActive(true);
        entity.setUpdateAt(timestamp);
        entity.setIngredient(ingredient);

        final var dto = IngredientHistoryMapper
                .toDto(entity, this.eventService);
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals(1L, dto.getTenantId());
        Assertions.assertEquals("Name", dto.getName());
        Assertions.assertEquals("ActorName", dto.getActorName());
        Assertions.assertEquals("ActorRole", dto.getActorRole());
        Assertions.assertEquals("Description", dto.getDescription());
        Assertions.assertEquals(timestamp, dto.getTrackTimestamp());
    }

    @Test
    void toDtoList() {
        var entity = new IngredientHistoryEntity();
        final var timestamp = Instant.now().toString();
        var event = new IngredientEventEntity();
        final var ingredient = new IngredientEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setActorName("ActorName");
        entity.setActorRole("ActorRole");
        entity.setDescription("Description");
        entity.setEventStatus("success");
        entity.setEvent(event);
        entity.setExtraInformationClass("ExtraInformationClass");
        entity.setExtraInformation("ExtraInformation");
        entity.setTrackTimestamp(timestamp);
        entity.setActive(true);
        entity.setUpdateAt(timestamp);
        entity.setIngredient(ingredient);

        final var dtos = IngredientHistoryMapper
                .toDto(List.of(entity), this.eventService);
        dtos.forEach(dto -> {
            Assertions.assertEquals(1L, dto.getId());
            Assertions.assertEquals(1L, dto.getTenantId());
            Assertions.assertEquals("Name", dto.getName());
            Assertions.assertEquals("ActorName", dto.getActorName());
            Assertions.assertEquals("ActorRole", dto.getActorRole());
            Assertions.assertEquals("Description", dto.getDescription());
            Assertions.assertEquals(timestamp, dto.getTrackTimestamp());
        });
    }

    @Test
    void testToDtoPage() {
        var entity = new IngredientHistoryEntity();
        final var timestamp = Instant.now().toString();
        var event = new IngredientEventEntity();
        final var ingredient = new IngredientEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setActorName("ActorName");
        entity.setActorRole("ActorRole");
        entity.setDescription("Description");
        entity.setEventStatus("success");
        entity.setEvent(event);
        entity.setExtraInformationClass("ExtraInformationClass");
        entity.setExtraInformation("ExtraInformation");
        entity.setTrackTimestamp(timestamp);
        entity.setActive(true);
        entity.setUpdateAt(timestamp);
        entity.setIngredient(ingredient);

        final var dtos = IngredientHistoryMapper
                .toDto(new PageImpl<>(List.of(entity)), this.eventService);
        dtos.forEach(dto -> {
            Assertions.assertEquals(1L, dto.getId());
            Assertions.assertEquals(1L, dto.getTenantId());
            Assertions.assertEquals("Name", dto.getName());
            Assertions.assertEquals("ActorName", dto.getActorName());
            Assertions.assertEquals("ActorRole", dto.getActorRole());
            Assertions.assertEquals("Description", dto.getDescription());
            Assertions.assertEquals(timestamp, dto.getTrackTimestamp());
        });
    }

    @Test
    void toEntity() {
        var ingredient = new IngredientEntity();
        ingredient.setId(1L);
        when(this.ingredientService.getById(ingredient.getId()))
                .thenReturn(ingredient);
        var request = new IngredientRequest();
        request.setId(1L);
        request.setClientId(1L);
        request.setName("Name");
        request.setActorName("ActorName");
        request.setActorRole("ActorRole");

        var event = new IngredientEventEntity();
        var status = IngredientEventStatus.SUCCESS;

    }

    @Test
    void testToEntity() {
    }

    @Test
    void testToEntity1() {
    }
}