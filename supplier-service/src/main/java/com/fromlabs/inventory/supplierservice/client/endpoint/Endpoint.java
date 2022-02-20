/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.client.endpoint;

import com.fromlabs.inventory.supplierservice.imports.beans.dto.ImportDto;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.fromlabs.inventory.supplierservice.config.AppConfig.ID;

/**
 * Supplier endpoint interface for internally expose API to other service
 */

public interface Endpoint {

    ImportDto getImportById(Long id);


}
