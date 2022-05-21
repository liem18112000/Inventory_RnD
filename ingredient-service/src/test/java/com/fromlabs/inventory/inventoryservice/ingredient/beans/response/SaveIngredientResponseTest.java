package com.fromlabs.inventory.inventoryservice.ingredient.beans.response;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SaveIngredientResponseTest {

    @Test
    public void test() {
        var ingredient = IngredientDto.builder().build();
        var config = new IngredientConfigEntity();
        var response = SaveIngredientResponse.builder()
                .ingredient(ingredient).config(config).build();
        Assertions.assertEquals(ingredient, response.getIngredient());
        Assertions.assertEquals(config, response.getConfig());
    }
}