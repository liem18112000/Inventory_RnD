package com.fromlabs.inventory.supplierservice.client.endpoint;

import com.fromlabs.inventory.supplierservice.config.ApiV1;
import com.fromlabs.inventory.supplierservice.imports.ImportService;
import com.fromlabs.inventory.supplierservice.imports.beans.dto.ImportDto;
import com.fromlabs.inventory.supplierservice.imports.mapper.ImportMapper;
import com.fromlabs.inventory.supplierservice.utility.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "endpoint/supplier/" + ApiV1.URI_API)
public class EndpointImpl implements Endpoint {

    private final ImportService importService;
    public static final String SERVICE_PATH = "/endpoint/supplier/" + ApiV1.URI_API + "/";

    public EndpointImpl(ImportService importService) {
        this.importService = importService;
    }

    /**
     * Log out the path for endpoint request
     * @param method    HttpMethod
     * @param subPath   Endpoint sub path
     * @return          String
     */
    private String path(HttpMethod method, String subPath) {
        return TransactionLogic.path(method, SERVICE_PATH, subPath, ApiV1.VERSION);
    }

    @RequestMapping(value = "import/{id:\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public ImportDto getImportById(@PathVariable Long id) {
        log.info(path(HttpMethod.GET, "import/".concat(String.valueOf(id))));
        final var importEntity = this.importService.getById(id);
        if (Objects.isNull(importEntity)) {
            return null;
        }
        return ImportMapper.toDto(importEntity);
    }
}
