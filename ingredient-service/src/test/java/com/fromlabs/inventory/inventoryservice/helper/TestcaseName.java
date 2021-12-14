/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.helper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestcaseName {
    //<editor-fold desc="INGREDIENT">

    public final String GET_INGREDIENT_BY_ID                = "Get ingredient by id";
    public final String GET_INGREDIENT_BY_CODE              = "Get ingredient by code";
    public final String GET_INGREDIENT_BY_NAME              = "Get ingredient by name";
    public final String GET_INGREDIENT_PAGE                 = "Get ingredient page with filter";
    public final String GET_INGREDIENT_ALL                  = "Get ingredient list";
    public final String GET_INGREDIENT_ALL_BY_PARENT        = "Get ingredient list by parent";

    public final String GET_INGREDIENT_CONFIG_BY_ID         = "Get ingredient config by id";
    public final String GET_INGREDIENT_CONFIG_BY_INGREDIENT = "Get ingredient config by ingredient id";
    public final String GET_INGREDIENT_CONFIG_ALL           = "Get ingredient config list";
    public final String GET_INGREDIENT_CONFIG_PAGE          = "Get ingredient config page";

    //</editor-fold>

    //<editor-fold desc="INVENTORY">

    public final String GET_INVENTORY_BY_ID                = "Get inventory by id";
    public final String GET_INVENTORY_BY_NAME              = "Get inventory by name";
    public final String GET_INVENTORY_BY_INGREDIENT        = "Get inventory by ingredient";
    public final String GET_INVENTORY_PAGE                 = "Get inventory page with filter";
    public final String GET_INVENTORY_PAGE_WITH_CLIENT_ID  = "Get inventory page with filter on client id";
    public final String GET_INVENTORY_ALL                  = "Get inventory list";

    //</editor-fold>

}
