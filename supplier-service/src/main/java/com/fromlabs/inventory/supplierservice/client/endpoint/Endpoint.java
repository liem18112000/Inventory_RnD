/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.client.endpoint;

import com.fromlabs.inventory.supplierservice.imports.beans.dto.ImportDto;
import com.fromlabs.inventory.supplierservice.imports.details.beans.dto.ImportDetailDto;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailRequest;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.dto.ProvidableMaterialDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.fromlabs.inventory.supplierservice.config.AppConfig.ID;

/**
 * Supplier endpoint interface for internally expose API to other service
 */

public interface Endpoint {

    ImportDto getImportById(Long id);

    ImportDetailDto onAddNewItem(ImportDetailRequest request);

    boolean isIngredientCanBeProvidable(Long importId, Long ingredientId, float quantity);

}
