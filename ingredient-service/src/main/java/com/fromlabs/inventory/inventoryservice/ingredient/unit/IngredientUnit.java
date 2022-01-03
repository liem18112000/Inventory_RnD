/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.unit;

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

    //<editor-fold desc="CONVERTER">
    public Float covertGramToKilogram(Float gram){
        if(gram <= 0) return 0f;
        return gram / 1000;
    }

    public Float covertKiloramToGram(Float kiliogram){
        if(kiliogram <= 0) return 0f;
        return kiliogram * 1000;
    }

    public Float covertMililiterToLiter(Float mililiter){
        if(mililiter <= 0) return 0f;
        return mililiter / 1000;
    }

    public Float covertLiterToMililiter(Float liter){
        if(liter <= 0) return 0f;
        return liter * 1000;
    }

    public Float covertCentimeterToMeter(Float centimeter){
        if(centimeter <= 0) return 0f;
        return centimeter / 100;
    }

    public Float covertMeterToCentimeter(Float meter){
        if(meter <= 0) return 0f;
        return meter * 100;
    }
    //</editor-fold>
}
