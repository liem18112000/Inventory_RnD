/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.beans.unit;

import java.util.Arrays;
import java.util.List;

/**
 * Ingredient unit type class
 */
public class IngredientUnit {

    static public final String LENGTH   = "length";
    static public final String VOLUME   = "volume";
    static public final String AREA     = "area";
    static public final String WEIGHT   = "weight";
    static public final String WHOLE    = "whole";
    static public final String GENERIC  = "generic";

    public static List<String> unitTypes() {
        return Arrays.asList(LENGTH, VOLUME, AREA, WEIGHT, WHOLE);
    }

    //<editor-fold desc="UNITS">
    /**
     * Whole unit
     */
    public enum WholeUnit {
        bottle,
        can,
        pack,
        piece,
        box
    }

    /**
     * Area unit
     */
    public enum AreaUnit {
        square_centimeter,
        square_meter
    }

    /**
     * Weight unit
     */
    public enum WeightUnit {
        gram,
        kilogram
    }

    /**
     * Volume unit
     */
    public enum VolumeUnit {
        mililiter,
        liter
    }

    /**
     * Length unit
     */
    public enum LengthUnit {
        centimeter,
        meter
    }
    //</editor-fold>
}
