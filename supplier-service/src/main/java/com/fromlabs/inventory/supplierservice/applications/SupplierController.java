package com.fromlabs.inventory.supplierservice.applications;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.imports.ImportService;
import com.fromlabs.inventory.supplierservice.supplier.SupplierService;
import com.fromlabs.inventory.supplierservice.supplier.beans.*;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialService;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.ProvidableMaterialPageRequest;
import com.fromlabs.inventory.supplierservice.utility.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fromlabs.inventory.supplierservice.config.AppConfig.*;
import static com.fromlabs.inventory.supplierservice.config.versions.ApiV1.*;
import static com.fromlabs.inventory.supplierservice.utility.TemplateProcessDirector.*;

/**
 * SupplierController is REST controller which responsible for
 * all C.R.U.D operations of ...
 * @see <a href="https://spring.io/guides/tutorials/rest/">REST Controller example : Building REST services with Spring</a>
 */
@Slf4j
@RestController
@RequestMapping(value = "${application.base-url}/" + URI_API, produces = MIME_API)
public class SupplierController implements ApplicationController {

    //<editor-fold desc="SETUP">

    private final SupplierService           supplierService;
    private final ImportService             importService;
    private final ProvidableMaterialService providableMaterialService;
    private final IngredientClient          ingredientClient;

    public static final String SERVICE_PATH = "/supplier/";

    private String path(HttpMethod method, String subPath) {
        return TransactionLogic.path(method, SERVICE_PATH, subPath, VERSION);
    }

    public SupplierController(
            SupplierService             supplierService,
            ImportService               importService,
            ProvidableMaterialService   providableMaterialService,
            IngredientClient            ingredientClient
    ) {
        this.supplierService            = supplierService;
        this.importService              = importService;
        this.providableMaterialService  = providableMaterialService;
        this.ingredientClient           = ingredientClient;
        this.trackControllerDependencyInjectionInformation(
                supplierService,
                importService,
                providableMaterialService
        );
    }

    /**
     * Track dependency injection information
     * @param supplierService           SupplierService
     * @param importService             ImportService
     * @param providableMaterialService ProvidableMaterialService
     */
    private void trackControllerDependencyInjectionInformation(
            SupplierService             supplierService,
            ImportService               importService,
            ProvidableMaterialService   providableMaterialService
    ) {
        log.info("Supplier service : {}",           supplierService.getClass().getName());
        log.info("Import service : {}",             importService.getClass().getName());
        log.info("Providable Material service : {}",providableMaterialService.getClass().getName());
        log.info("Application controller : {}",     this.getClass().getName());
    }

    //</editor-fold>

    //<editor-fold desc="SUPPLIER">

    //<editor-fold desc="GROUP">

    /**
     * Get page supplier group with filter
     *
     * @param tenantId Tenant ID
     * @param request  SupplierPageRequest
     * @return ResponseEntity
     */
    @PostMapping("group/page")
    public ResponseEntity<?> getPageSupplierGroup(
            @RequestHeader(TENANT_ID) Long      tenantId,
            @RequestBody SupplierPageRequest    request
    ) {
        log.info(path(HttpMethod.POST, "category/page"));
        return (ResponseEntity<?>) buildGetPageSupplierGroupTemplateProcess(tenantId, request, supplierService).run();
    }

    /**
     * Get all supplier group as list with filter
     *
     * @param tenantId Tenant ID
     * @param request  SupplierPageRequest
     * @return ResponseEntity
     */
    @GetMapping("group/all")
    public ResponseEntity<?> getAllSupplierGroup(
            @RequestHeader(TENANT_ID) Long      tenantId,
            @RequestBody SupplierPageRequest    request
    ) {
        log.info(path(HttpMethod.GET, "group/all"));
        return (ResponseEntity<?>) getListSupplierGroupTemplateProcess(tenantId, request, supplierService).run();
    }

    @GetMapping("group/all/simple")
    public ResponseEntity<?> getAllSupplierGroupLabelValue(
            @RequestHeader(TENANT_ID) Long      tenantId
    ) {
        log.info(path(HttpMethod.GET, "group/all"));
        return (ResponseEntity<?>) buildGetSupplierGroupWithLabelValueTemplateProcess(tenantId, supplierService).run();
    }

    //</editor-fold>

    //<editor-fold desc="CHILD">

    /**
     * Get page supplier child with filter
     *
     * @param tenantId Tenant ID
     * @param request  SupplierPageRequest
     * @return ResponseEntity
     */
    @PostMapping("child/page")
    public ResponseEntity<?> getPageSupplierChild(
            @RequestHeader(TENANT_ID) Long      tenantId,
            @RequestBody SupplierPageRequest    request
    ) {
        log.info(path(HttpMethod.POST, "child/page"));
        return (ResponseEntity<?>) buildGetPageSupplierChildTemplateProcess(tenantId, request, supplierService).run();
    }

    /**
     * Get all supplier child as list with filter
     *
     * @param tenantId Tenant ID
     * @param request  SupplierPageRequest
     * @return ResponseEntity
     */
    @GetMapping("child/all")
    public ResponseEntity<?> getAllSupplierChild(
            @RequestHeader(TENANT_ID) Long      tenantId,
            @RequestBody SupplierPageRequest    request
    ) {
        log.info(path(HttpMethod.GET, "child/all"));
        return (ResponseEntity<?>) getListSupplierChildTemplateProcess(tenantId, request, supplierService).run();
    }

    /**
     * Get all child supplier by tenant id and parent id
     * @param tenantId  Tenant Id
     * @param parentId  Supplier group Id
     * @return          ResponseEntity
     */
    @GetMapping("child/all/simple")
    public ResponseEntity<?> getAllSupplierChildLabelValue(
            @RequestHeader(TENANT_ID) Long      tenantId,
            @RequestParam(PARENT_ID)  Long      parentId
    ) {
        log.info(path(HttpMethod.GET, "child/all"));
        return (ResponseEntity<?>) buildGetSupplierChildWithLabelValueTemplateProcess(tenantId, parentId, supplierService).run();
    }

    //</editor-fold>

    //<editor-fold desc="GENERAL">

    /**
     * Get all supplier (with both types) by name as list
     *
     * @param tenantId Tenant ID
     * @param name     Suppler name
     * @return ResponseEntity
     */
    @GetMapping("name/all")
    public ResponseEntity<?> getAllSupplierByName(
            @RequestHeader(TENANT_ID) Long  tenantId,
            @RequestParam(NAME) String      name
    ) {
        log.info(path(HttpMethod.GET, "name/all"));
        return (ResponseEntity<?>) buildGetSupplierByNameTemplateProcess(tenantId, name, supplierService).run();
    }

    /**
     * Get supplier (ith both types) by CODE
     *
     * @param code Supplier Code
     * @return ResponseEntity
     */
    @GetMapping("code")
    public ResponseEntity<?> getSupplierByCode(
            @RequestParam(CODE) String code
    ) {
        log.info(path(HttpMethod.GET, "code"));
        return (ResponseEntity<?>) buildGetSupplierByCodeTemplateProcess(code, supplierService).run();
    }

    /**
     * Get supplier (with both types) with ID
     *
     * @param id Unique Entity ID
     * @return ResponseEntity
     */
    @GetMapping("{id:\\d+}")
    public ResponseEntity<?> getSupplierById(
            @PathVariable(ID) Long id
    ) {
        log.info(path(HttpMethod.GET, String.valueOf(id)));
        return (ResponseEntity<?>) buildGetSupplierByIdTemplateProcess(id, supplierService).run();
    }

    /**
     * Save supplier (with both types)
     *
     * @param tenantId Tenant ID
     * @param request  SupplierRequest
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<?> saveSupplier(
            @RequestHeader(TENANT_ID) Long  tenantId,
            @RequestBody SupplierRequest    request
    ) {
        log.info(path(HttpMethod.POST, ""));
        return (ResponseEntity<?>) buildSaveSupplierTemplateProcess(tenantId, request, supplierService).run();
    }

    /**
     * Update supplier (with both types)
     *
     * @param tenantId Tenant ID
     * @param request  SupplierRequest
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity<?> updateSupplier(
            @RequestHeader(TENANT_ID) Long  tenantId,
            @RequestBody SupplierRequest    request
    ) {
        log.info(path(HttpMethod.PUT, ""));
        return (ResponseEntity<?>) buildUpdateSupplierTemplateProcess(tenantId, request, supplierService).run();
    }

    /**
     * Delete supplier (with both types) by id
     *
     * @param id Unique Entity ID
     * @return ResponseEntity
     */
    @DeleteMapping("{id:\\d+}")
    public ResponseEntity<?> deleteSupplier(
            @PathVariable(ID) Long id
    ) {
        log.info(path(HttpMethod.DELETE, String.valueOf(id)));
        return (ResponseEntity<?>) buildDeleteSupplierTemplateProcess(id, supplierService).run();
    }

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="PROVIDABLE MATERIAL">

    /**
     * Get providable material by id
     *
     * @param id Unique Entity ID
     * @return ResponseEntity
     */
    @GetMapping("providable-material/{id:\\d+}")
    public ResponseEntity<?> getProvidableMaterialById(
            @PathVariable(ID) Long id
    ) {
        return (ResponseEntity<?>) buildGetProvidableMaterialById(id, providableMaterialService, ingredientClient).run();
    }

    /**
     * Get all providable material as list b tenant id
     *
     * @param tenantId Tenant ID
     * @return ResponseEntity
     */
    @GetMapping("providable-material/all")
    public ResponseEntity<?> getAllProvidableMaterialByTenantId(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        return (ResponseEntity<?>) buildGetAllProvidableMaterialByTenantId(tenantId, providableMaterialService, ingredientClient).run();
    }

    /**
     * Get page providable material with filter
     *
     * @param tenantId Tenant ID
     * @param request  ProvidableMaterialPageRequest
     * @return ResponseEntity
     */
    public ResponseEntity<?> getPageProvidableMaterialWithFilter(
            Long                            tenantId,
            ProvidableMaterialPageRequest   request
    ) {
        return null;
    }

    //</editor-fold>
}
