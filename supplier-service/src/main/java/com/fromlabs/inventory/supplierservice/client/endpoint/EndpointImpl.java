package com.fromlabs.inventory.supplierservice.client.endpoint;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.config.ApiV1;
import com.fromlabs.inventory.supplierservice.imports.ImportService;
import com.fromlabs.inventory.supplierservice.imports.beans.dto.ImportDto;
import com.fromlabs.inventory.supplierservice.imports.details.ImportDetailService;
import com.fromlabs.inventory.supplierservice.imports.details.beans.dto.ImportDetailDto;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailRequest;
import com.fromlabs.inventory.supplierservice.imports.mapper.ImportMapper;
import com.fromlabs.inventory.supplierservice.utility.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static com.fromlabs.inventory.supplierservice.imports.details.specification.ImportDetailSpecification.hasImport;
import static com.fromlabs.inventory.supplierservice.imports.details.specification.ImportDetailSpecification.hasIngredientId;

@Slf4j
@RestController
@RequestMapping(value = "endpoint/supplier/" + ApiV1.URI_API)
public class EndpointImpl implements Endpoint {

    private final ImportService importService;
    private final ImportDetailService importDetailService;
    private final IngredientClient ingredientClient;
    public static final String SERVICE_PATH = "/endpoint/supplier/" + ApiV1.URI_API + "/";

    public EndpointImpl(
            ImportService importService,
            ImportDetailService importDetailService,
            IngredientClient ingredientClient
    ) {
        this.importService = importService;
        this.importDetailService = importDetailService;
        this.ingredientClient = ingredientClient;
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

    @PostMapping("import/add-items")
    public ImportDetailDto onAddNewItem(
            @RequestBody ImportDetailRequest request) {
        final var importEntity = this.importService.getById(request.getImportId());
        if (Objects.isNull(importEntity)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        final var spec = hasImport(importEntity)
                .and(hasIngredientId(request.getIngredientId()));
        final var details = this.importDetailService.getAll(spec);
        if (details.isEmpty()) {
            return (ImportDetailDto) TransactionLogic.saveImportDetail(request, importService,
                    importDetailService, ingredientClient).getBody();
        }
        if (details.size() > 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        request.setId(details.get(0).getId());
        request.setQuantity(request.getQuantity() + details.get(0).getQuantity());
        return (ImportDetailDto) TransactionLogic.updateImportDetail(request,
                importDetailService, ingredientClient).getBody();
    }
}
