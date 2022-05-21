package com.fromlabs.inventory.inventoryservice.ingredient.beans.unit;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static com.fromlabs.inventory.inventoryservice.ingredient.beans.unit.IngredientUnit.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientUnitTest {

    @Test
    public void unitTypes() {
        Assertions.assertEquals(
                Arrays.asList(LENGTH, VOLUME, AREA, WEIGHT, WHOLE),
                IngredientUnit.unitTypes());
    }

    @Test
    public void getWholeUnit() {
        Assertions.assertTrue(Arrays.stream(WholeUnit.values()).findAny().isPresent());
    }

    @Test
    public void getAreaUnit() {
        Assertions.assertTrue(Arrays.stream(AreaUnit.values()).findAny().isPresent());
    }

    @Test
    public void getWeightUnit() {
        Assertions.assertTrue(Arrays.stream(WeightUnit.values()).findAny().isPresent());
    }

    @Test
    public void getVolumeUnit() {
        Assertions.assertTrue(Arrays.stream(VolumeUnit.values()).findAny().isPresent());
    }

    @Test
    public void getLengthUnit() {
        Assertions.assertTrue(Arrays.stream(LengthUnit.values()).findAny().isPresent());
    }
}