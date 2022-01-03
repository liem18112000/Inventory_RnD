/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.recipeservice.common.service;

import com.fromlabs.inventory.recipeservice.common.entity.BaseEntity;

import java.io.Serializable;

public interface RestApiService
        <ENTITY extends BaseEntity<ID>, ID extends Serializable>
        extends
            SpecificationService<ENTITY, ID>,
            PagingAndSortingWithSpecificationService<ENTITY, ID>
{
}
