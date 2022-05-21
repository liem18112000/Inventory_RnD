package com.fromlabs.inventory.inventoryservice.ingredient.track.beans.dto;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.event.beans.dto.IngredientEventDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientHistoryDtoTest {

    @Test
    public void test() {
        final var dto = IngredientHistoryDto.builder()
                .ingredient(IngredientDto.builder().build())
                .event(IngredientEventDto.builder().build())
                .extraInformation(new Object())
                .build();
        Assertions.assertNotNull(dto.getIngredient());
        Assertions.assertNotNull(dto.getEvent());
        Assertions.assertNotNull(dto.getExtraInformation());
    }

}