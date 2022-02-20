/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.client.supplier;

import com.fromlabs.inventory.inventoryservice.client.supplier.beans.ImportDto;
import com.fromlabs.inventory.inventoryservice.config.ApiV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Client for supplier module
 * @author Liem
 */
@FeignClient(value = "${services.supplier-service.name}")
@RequestMapping(value = "endpoint/supplier/" + ApiV1.URI_API)
public interface SupplierClient {

    @GetMapping("import/{id:\\d+}")
    ImportDto getImportById(@PathVariable Long id);
}
