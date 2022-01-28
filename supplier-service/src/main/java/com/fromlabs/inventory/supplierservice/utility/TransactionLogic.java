package com.fromlabs.inventory.supplierservice.utility;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.common.dto.SimpleDto;
import com.fromlabs.inventory.supplierservice.common.entity.BaseEntity;
import com.fromlabs.inventory.supplierservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.supplierservice.imports.ImportService;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportPageRequest;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportRequest;
import com.fromlabs.inventory.supplierservice.imports.details.ImportDetailService;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailPageRequest;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailRequest;
import com.fromlabs.inventory.supplierservice.imports.details.mapper.ImportDetailMapper;
import com.fromlabs.inventory.supplierservice.imports.details.specification.ImportDetailSpecification;
import com.fromlabs.inventory.supplierservice.imports.mapper.ImportMapper;
import com.fromlabs.inventory.supplierservice.imports.specification.ImportSpecification;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import com.fromlabs.inventory.supplierservice.supplier.SupplierService;
import com.fromlabs.inventory.supplierservice.supplier.beans.dto.SupplierDto;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierRequest;
import com.fromlabs.inventory.supplierservice.supplier.mapper.SupplierMapper;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialService;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.mapper.ProvidableMaterialMapper;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.specification.ProvidableMaterialSpecification;
import com.fromlabs.inventory.supplierservice.supplier.specification.SupplierSpecification;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.*;
import static org.springframework.http.ResponseEntity.*;

/**
 * <h1>Transaction Logic Layer</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     This the fourth layer in the segment of non-infrastructure service layer.
 *     In detail, it play as the most important segment service of all.
 *     Here is the business domain logic or high-level application logic is located
 * </p>
 *
 * <p>
 *     It is a mandatory layer as it must be implemented to keep the
 *     domain service layer or high-level application layer run
 * </p>
 *
 * <h2>Usages</h2>
 *
 * <p>Developer can implement the logic here clearly with the injection of all dependency of services</p>
 */
@Slf4j
@UtilityClass
public class TransactionLogic {

    //<editor-fold desc="SUPPLIER">

    /**
     * Get supplier by id
     * @param id                Entity ID
     * @param supplierService   SupplierService
     * @return                  SupplierDto
     */
    public ResponseEntity<?> getSupplierById(
            Long            id,
            SupplierService supplierService
    ) throws ObjectNotFoundException {
        // Check pre-condition
        assert nonNull(supplierService);

        // Get entity by id
        final var supplier = supplierService.getByIdWithException(id);

        // Convert entity to DTO, wrap it in OK status and return it
        return ok(SupplierMapper.toDto(supplier));
    }

    /**
     * Get supplier by code
     * @param code              Entity Code
     * @param supplierService   SupplierService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getSupplierByCode(
            String          code,
            SupplierService supplierService
    ) {
        // Check pre-condition
        assert nonNull(supplierService);

        // Get entity by code
        final var supplier = supplierService.getByCode(code);

        // Convert entity to DTO, wrap it in OK status and return it
        return ok(convertEntityToDTO(supplier));
    }

    /**
     * Convert an entity to DTO
     * @param supplier  SupplierEntity
     * @return          SupplierDto
     */
    private SupplierDto convertEntityToDTO(
            SupplierEntity supplier
    ) {
        return isNull(supplier) ? null : SupplierMapper.toDto(supplier);
    }

    /**
     * Get supplier by name and client id
     * @param clientId          Tenant ID
     * @param name              Supplier name
     * @param supplierService   SupplierService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getSupplierByName(
            Long            clientId,
            String          name,
            SupplierService supplierService
    ) {
        // Check pre-condition
        assert nonNull(supplierService);

        // Get list of supplier by name and client id
        final var suppliers = supplierService.getByName(clientId, name);

        // Convert list of entity to DTO, wrap it in OK response and return it
        return ok(SupplierMapper.toDto(suppliers));
    }

    /**
     * Get all supplier group as list by client id
     * @param clientId          Client ID
     * @param supplierService   SupplierService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getSupplierGroupWithLabelValue(
            Long            clientId,
            SupplierService supplierService
    ) {
        // Check pre-conditions
        assert nonNull(supplierService);

        // Get list of supplier group by specification
        final var specification = SupplierSpecification.hasClientId(clientId)
                                                        .and(SupplierSpecification.hasGroup(true));
        final var suppliers = supplierService.getAll(specification);

        // Convert supplier group list to simple DTO list and return
        return ok(convertSupplierListToSimpleDtoList(suppliers));
    }

    /**
     * Get supplier child with label-value by parent id
     * @param clientId          Tenant ID
     * @param parentId          Supplier Group ID
     * @param supplierService   SupplierService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getSupplierChildWithLabelValue(
            Long            clientId,
            Long            parentId,
            SupplierService supplierService
    ) {
        // Check pre-conditions
        assert nonNull(supplierService);
        assert nonNull(parentId);

        // Get list of supplier child by specification
        final var parent = requireNonNull(supplierService.getById(parentId));
        final var specification = SupplierSpecification.hasClientId(clientId)
                                                        .and(SupplierSpecification.hasParent(parent));
        final var suppliers = supplierService.getAll(specification);

        // Convert supplier child list to simple DTO list and return
        return ok(convertSupplierListToSimpleDtoList(suppliers));
    }

    /**
     * Convert supplier list to label-value list
     * @param suppliers List&lt;SupplierEntity&gt;
     * @return          List&lt;SimpleDto&gt;
     */
    public List<SimpleDto> convertSupplierListToSimpleDtoList(
            List<SupplierEntity> suppliers
    ) {
        return suppliers.stream().map(TransactionLogic::convertSupplierToSimpleDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert supplier to simple DTO
     * @param supplier  SupplierEntity
     * @return          SimpleDto
     */
    public SimpleDto convertSupplierToSimpleDto(
            SupplierEntity supplier
    ) {
        // Return empty simple dto if supplier is null
        if(isNull(supplier)) return SimpleDto.builder().build();

        // Create simple dto builder
        var dto = convertEntityToSimpleDto(supplier);

        // If children is not null and not empty set children simple dto as options
        final var children = supplier.getChildren();
        if(nonNull(children) && !children.isEmpty()) dto.setOptions(convertSupplierListToSimpleDtoList(children));;

        return dto;
    }

    /**
     * Convert entity to simple dto
     * if entity is null return empty dto
     * @param entity    BaseEntity
     * @return          SimpleDto
     */
    public SimpleDto convertEntityToSimpleDto(
            BaseEntity<?> entity
    ) {
        if(isNull(entity)) return SimpleDto.builder().build();
        return SimpleDto.builder().label(entity.getName()).value(entity.getId()).build();
    }

    /**
     * Get supplier page by filter
     * @param request           SupplierPageRequest
     * @param supplierService   SupplierService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getSupplierPage(
            SupplierPageRequest request,
            SupplierService     supplierService
    ) {
        // Check pre-condition
        assert nonNull(supplierService);
        assert nonNull(request);

        // Get filter entity and parent entity
        final var parent = getSupplierParentById(request.getParentId(), supplierService);
        final var filterEntity = SupplierMapper.toEntity(request);

        // Get page of entity from filter entity
        final var entityPage = supplierService.getPage(
                // Get specification from filter entity
                SupplierSpecification.filter(filterEntity, parent),
                // Get pageable from request
                request.getPageable());

        // Convert page of entity to page of DTO, wrap it in OK response and return it
        return ok(SupplierMapper.toDto(entityPage));
    }

    /**
     * Get supplier list by filter
     * @param request           SupplierPageRequest
     * @param supplierService   SupplierService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getSupplierList(
            SupplierPageRequest request,
            SupplierService     supplierService
    ) {
        // Check pre-condition
        assert nonNull(supplierService);
        assert nonNull(request);

        // Get filter entity and parent entity
        final var parent = getSupplierParentById(request.getParentId(), supplierService);
        final var filterEntity = SupplierMapper.toEntity(request);

        // Get page of entity from filter entity
        final var suppliers = supplierService.getAll(
                // Get specification from filter entity
                SupplierSpecification.filter(filterEntity, parent));

        // Convert list of entity to list of DTO, wrap it in OK response and return it
        return ok(SupplierMapper.toDto(suppliers));
    }

    /**
     * Get parent id with parentId
     * @param parentId          Supplier parent id
     * @param supplierService   SupplierService
     * @return                  SupplierEntity
     */
    public SupplierEntity getSupplierParentById(
            Long            parentId,
            SupplierService supplierService
    ) {
        return isNull(parentId) ? null : supplierService.getById(parentId);
    }

    /**
     * Update supplier (both types)
     * @param request           SupplierRequest
     * @param supplierService   SupplierService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> saveSupplier(
            @NotNull final SupplierRequest request,
            @NotNull final SupplierService supplierService
    ) {
        // Convert request to saved entity
        final var supplier      = SupplierMapper.toEntity(request);

        // Set parent for save child supplier
        if (nonNull(request.getParentId())) {
            final var parent = supplierService.getById(request.getParentId());
            supplier.setParent(parent);
        }
        final var savedSupplier = supplierService.save(supplier);

        // Save supplier with CREATED status
        return status(HttpStatus.CREATED).body(savedSupplier);
    }

    /**
     * Update supplier by request
     * @param request           SupplierRequest
     * @param supplierService   SupplierService
     * @return ResponseEntity
     */
    public ResponseEntity<?> updateSupplier(
            @NotNull final SupplierRequest request,
            @NotNull final SupplierService supplierService
    ) {
        // Pre-condition
        assert nonNull(request.getId());

        // Convert request to saved entity
        final var supplier= supplierService.getById(request.getId()).update(request);
        final var updatedSupplier = supplierService.save(supplier);

        // Save supplier with CREATED status
        return ok(updatedSupplier);
    }

    /**
     * Delete supplier by ID
     * @param id                Supplier ID
     * @param supplierService   SupplierService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> deleteSupplier(
            Long id,
            SupplierService supplierService
    ) {
        // Check pre-conditions
        assert nonNull(supplierService);

        // Get supplier by id
        final var supplier = supplierService.getById(id);

        // Delete supplier by id
        supplierService.delete(supplier);

        return status(HttpStatus.NO_CONTENT).build();
    }

    //</editor-fold>

    //<editor-fold desc="PROVIDABLE MATERIAL">

    /**
     * Get providable material by id
     * @param id                Entity ID
     * @param service           ProvidableMaterialService
     * @param ingredientClient  IngredientClient
     * @return                  ResponseEntity
     */
    @SneakyThrows
    public ResponseEntity<?> getProvidableMaterialById(
            Long id,
            ProvidableMaterialService service,
            IngredientClient ingredientClient
    ) {
        // Check pre-condition
        assert nonNull(service);

        // Get material by id
        final var material = service.getById(id);

        if(isNull(material)) {
            throw new ObjectNotFoundException(
                    "Providable material is not found by id : {}" + id);
        }

        // Convert entity to DTO and return it
        return ok(ProvidableMaterialMapper.toDto(material, ingredientClient));
    }

    /**
     * Get page material with request
     * @param request                   ProvidableMaterialPageRequest
     * @param supplierService           SupplierService
     * @param providableMaterialService ProvidableMaterialService
     * @param ingredientClient          IngredientClient
     * @return ResponseEntity
     */
    public ResponseEntity<?> getPageProvidableMaterial(
            @NotNull final ProvidableMaterialPageRequest   request,
            @NotNull final SupplierService                 supplierService,
            @NotNull final ProvidableMaterialService       providableMaterialService,
            @NotNull final IngredientClient                ingredientClient
    ) {
        // Prepare for all data
        final var supplierEntity = nonNull(request.getSupplierId()) ?
                supplierService.getById(request.getSupplierId()) : null;
        final var entity = ProvidableMaterialMapper.toEntity(request, supplierEntity);
        final var specification = ProvidableMaterialSpecification
                .filter(entity, supplierEntity);

        // Get page entities
        final var pages = providableMaterialService.getPage(
                specification, request.getPageable());

        // Convert to DTO and return result
        return ok(ProvidableMaterialMapper.toDto(pages, ingredientClient));
    }

    /**
     * Get all providable material by tenant id
     * @param tenantId          Tenant id
     * @param service           ProvidableMaterialService
     * @param ingredientClient  IngredientClient
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getAllProvidableMaterialByTenantId(
            Long                        tenantId,
            ProvidableMaterialService   service,
            IngredientClient            ingredientClient
    ) {
        // Check pre-condition
        assert nonNull(service);

        // Get material list by tenant id
        final var materials = service.getAll(tenantId);

        // Convert entity to DTO and return it
        return ok(ProvidableMaterialMapper.toDto(materials, ingredientClient));
    }

    /**
     * Save material by request
     * @param request                   ProvidableMaterialRequest
     * @param supplierService           SupplierService
     * @param providableMaterialService ProvidableMaterialService
     * @param ingredientClient          IngredientClient
     * @return ResponseEntity
     */
    public ResponseEntity<?> saveProvidableMaterial(
            @NotNull final ProvidableMaterialRequest    request,
            @NotNull final SupplierService              supplierService,
            @NotNull final ProvidableMaterialService    providableMaterialService,
            @NotNull final IngredientClient             ingredientClient
    ) {
        final var supplier = supplierService.getById(request.getSupplierId());
        final var entity = ProvidableMaterialMapper.toEntity(request, supplier);
        final var savedEntity = providableMaterialService.save(entity);
        final var dto = ProvidableMaterialMapper.toDto(savedEntity, ingredientClient);
        return status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Update material by request
     * @param request                   ProvidableMaterialRequest
     * @param providableMaterialService ProvidableMaterialService
     * @param ingredientClient          IngredientClient
     * @return ResponseEntity
     */
    public ResponseEntity<?> updateProvidableMaterial(
            @NotNull final ProvidableMaterialRequest    request,
            @NotNull final ProvidableMaterialService    providableMaterialService,
            @NotNull final IngredientClient             ingredientClient
    ) {
        final var entity = providableMaterialService.getById(request.getId())
                .update(request);
        final var savedEntity = providableMaterialService.save(entity);
        final var dto = ProvidableMaterialMapper.toDto(savedEntity, ingredientClient);
        return status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Delete material by id
     * @param id        Entity id
     * @param service   ProvidableMaterialService
     * @return ResponseEntity
     */
    public ResponseEntity<?> deleteProvidableMaterial(
            @NotNull final Long id,
            @NotNull final ProvidableMaterialService service
    ) {
        // Get entity by id
        final var entity = service.getById(id);

        // Delete entity
        service.delete(entity);

        return noContent().build();
    }

    //</editor-fold>

    //<editor-fold desc="IMPORT">

    /**
     * Get import by id
     * @param id            Import Unique ID
     * @param importService importService
     * @return              ImportDto
     * @throws ObjectNotFoundException when import is n ot found with id
     */
    public ResponseEntity<?> getImportById(
            @NotNull final Long id,
            @NotNull final ImportService importService
    ) throws ObjectNotFoundException {

        // Get import by id
        final var importEntity = importService.getByIdWithException(id);

        // Convert to DTO and return
        return ok(ImportMapper.toDto(importEntity));
    }

    public ResponseEntity<?> getPageImport(
            @NotNull final ImportPageRequest request,
            @NotNull final SupplierService supplierService,
            @NotNull final ImportService importService
    ) {
        // Prepare for all data
        final var supplier = nonNull(request.getSupplierId()) ?
                supplierService.getById(request.getSupplierId()) : null;
        final var entity = ImportMapper.toEntity(request);
        final var specification = ImportSpecification.filter(entity, supplier);

        // Get import page entities
        final var importPageEntities = importService.getPage(
                specification, request.getPageable());

        // Convert to DTO and return result
        return ok(ImportMapper.toDto(importPageEntities));
    }

    /**
     * Save import by request
     * @param request           ImportRequest
     * @param supplierService   SupplierService
     * @param importService     ImportService
     * @return                  ImportDto
     */
    public ResponseEntity<?> saveImport(
            @NotNull final ImportRequest request,
            @NotNull final SupplierService supplierService,
            @NotNull final ImportService importService
    ) {
        // Pre-condition
        assert nonNull(request.getSupplierId());

        // Obtain supplier and import entity from request
        final var supplierEntity = supplierService.getById(request.getSupplierId());
        final var importEntity = ImportMapper.toEntity(request, supplierEntity);

        // Save import entity
        final var savedImport = importService.save(importEntity);

        // Convert to DTO and return
        return status(HttpStatus.CREATED).body(ImportMapper.toDto(savedImport));
    }

    /**
     * Update import
     * @param request ImportRequest
     * @param supplierService SupplierService
     * @param importService ImportService
     * @return ResponseEntity
     */
    public ResponseEntity<?> updateImport(
            @NotNull final ImportRequest request,
            @NotNull final SupplierService supplierService,
            @NotNull final ImportService importService
    ) {
        // Pre-condition
        assert nonNull(request.getId());
        assert nonNull(request.getSupplierId());

        // Update import information from request
        final var importEntity = importService.getById(request.getId()).update(request);

        // Update import entity
        final var updatedImport = importService.save(importEntity);

        // Convert to DTO and return
        return ok(ImportMapper.toDto(updatedImport));
    }

    //</editor-fold>

    //<editor-fold desc="IMPORT DETAIL">

    /**
     * Get import detail by id
     * @param id Import detail ID
     * @param importDetailService  ImportDetailService
     * @param ingredientClient IngredientClient
     * @return ResponseEntity
     */
    public ResponseEntity<?> getImportDetailById(
            @NotNull final Long id,
            @NotNull final ImportDetailService importDetailService,
            @NotNull final IngredientClient ingredientClient
    ) {
        final var entity = importDetailService.getById(id);
        final var dto = ImportDetailMapper.toDto(entity, ingredientClient);
        return ok(dto);
    }

    /**
     * Get page import detail by request
     * @param request ImportDetailPageRequest
     * @param importService ImportService
     * @param importDetailService ImportDetailService
     * @param ingredientClient IngredientClient
     * @return ResponseEntity
     */
    public ResponseEntity<?> getImportDetailPage(
            @NotNull final ImportDetailPageRequest request,
            @NotNull final ImportService importService,
            @NotNull final ImportDetailService importDetailService,
            @NotNull final IngredientClient ingredientClient
    ) {
        final var importEntity = importService.getById(request.getImportId());
        final var specification = ImportDetailSpecification
                .filter(request, importEntity);
        final var importDetails = importDetailService.getPage(
                specification, request.getPageable());
        final var dto = ImportDetailMapper.toDto(importDetails, ingredientClient);
        return ok(dto);
    }

    /**
     * Save import detail by request
     * @param request ImportDetailRequest
     * @param importService ImportService
     * @param importDetailService ImportDetailService
     * @param ingredientClient IngredientClient
     * @return ResponseEntity
     */
    public ResponseEntity<?> saveImportDetail(
            @NotNull final ImportDetailRequest request,
            @NotNull final ImportService importService,
            @NotNull final ImportDetailService importDetailService,
            @NotNull final IngredientClient ingredientClient
    ) {
        // Check pre-condition
        assert nonNull(request.getImportId());

        // Build entity for saving
        final var importEntity = importService.getById(request.getImportId());
        final var entity = ImportDetailMapper.toEntity(request, importEntity);

        // Save entity and convent result to DTO
        final var savedEntity = importDetailService.save(entity);
        final var dto = ImportDetailMapper.toDto(savedEntity, ingredientClient);
        return status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Update import detail by request
     * @param request ImportDetailRequest
     * @param importDetailService ImportDetailService
     * @param ingredientClient IngredientClient
     * @return ResponseEntity
     */
    public ResponseEntity<?> updateImportDetail(
            @NotNull final ImportDetailRequest request,
            @NotNull final ImportDetailService importDetailService,
            @NotNull final IngredientClient ingredientClient
    ) {
        // Check pre-condition
        assert nonNull(request.getId());
        assert nonNull(request.getImportId());

        // Build entity for update
        final var entity = importDetailService.getById(request.getId())
                .update(request);

        // Update entity and convent result to DTO
        final var updatedEntity = importDetailService.save(entity);
        final var dto = ImportDetailMapper.toDto(updatedEntity, ingredientClient);

        return ok(dto);
    }

    /**
     * Delete import detail by id
     * @param id Import detail id
     * @param importDetailService ImportDetailService
     * @return ResponseEntity
     */
    public ResponseEntity<?> deleteImportDetailById(
            @NotNull final Long id,
            @NotNull final ImportDetailService importDetailService
    ) {
        final var importDetail = importDetailService.getById(id);
        importDetailService.delete(importDetail);
        return noContent().build();
    }

    //</editor-fold>

    /**
     * Log out Http path with extra information of service
     * @param method        HTTP method
     * @param servicePath   Service path
     * @param subPath       Sub path
     * @return              String
     */
    public String path(HttpMethod method, String servicePath, String subPath, String apiVersion) {
        return method.name().concat(" ")
                .concat(InetAddress.getLoopbackAddress().getCanonicalHostName().trim())
                .concat(servicePath.trim()).concat(subPath.trim()).concat(" ")
                .concat(apiVersion.trim()).trim();
    }
}
