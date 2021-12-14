package com.fromlabs.inventory.supplierservice.utility;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.common.dto.SimpleDto;
import com.fromlabs.inventory.supplierservice.common.entity.BaseEntity;
import com.fromlabs.inventory.supplierservice.common.exception.FailTransactionException;
import com.fromlabs.inventory.supplierservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import com.fromlabs.inventory.supplierservice.supplier.SupplierService;
import com.fromlabs.inventory.supplierservice.supplier.beans.SupplierDto;
import com.fromlabs.inventory.supplierservice.supplier.beans.SupplierPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.SupplierRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialService;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.ProvidableMaterialDto;
import com.fromlabs.inventory.supplierservice.supplier.specification.SupplierSpecification;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

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
    ) {
        // Check pre-condition
        assert nonNull(supplierService);

        // Get entity by id
        final var supplier = supplierService.getById(id);

        // Convert entity to DTO, wrap it in OK status and return it
        return ok(SupplierDto.from(supplier));
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
        final var supplier = supplierService.get(code);

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
        return isNull(supplier) ? null : SupplierDto.from(supplier);
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
        final var suppliers = supplierService.get(clientId, name);

        // Convert list of entity to DTO, wrap it in OK response and return it
        return ok(SupplierDto.from(suppliers));
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
        final var filterEntity = SupplierEntity.from(request);

        // Get page of entity from filter entity
        final var entityPage = supplierService.getPage(
                // Get specification from filter entity
                SupplierSpecification.filter(filterEntity, parent),
                // Get pageable from request
                request.getPageable());

        // Convert page of entity to page of DTO, wrap it in OK response and return it
        return ok(SupplierDto.from(entityPage));
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
        final var filterEntity = SupplierEntity.from(request);

        // Get page of entity from filter entity
        final var suppliers = supplierService.getAll(
                // Get specification from filter entity
                SupplierSpecification.filter(filterEntity, parent));

        // Convert list of entity to list of DTO, wrap it in OK response and return it
        return ok(SupplierDto.from(suppliers));
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
    @SneakyThrows
    public ResponseEntity<?> saveSupplier(
            SupplierRequest request,
            SupplierService supplierService
    ) {
        // Check pre-conditions
        assert nonNull(request);
        assert nonNull(supplierService);

        // Convert request to saved entity
        final var supplier      = SupplierEntity.from(request);
        final var savedSupplier = supplierService.save(supplier);

        // Save supplier with CREATED status
        return status(HttpStatus.CREATED).body(savedSupplier);
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
     * @return                  ProvidableMaterialDto
     */
    @SneakyThrows
    public ProvidableMaterialDto getProvidableMaterialById(
            Long id,
            ProvidableMaterialService service,
            IngredientClient ingredientClient
    ) {
        // Check pre-condition
        assert nonNull(service);

        // Get material by id
        final var material = service.getById(id);

        if(isNull(material)) throw new ObjectNotFoundException("Providable material is not found by id : {}" + id);

        // Convert entity to DTO and return it
        return ProvidableMaterialDto.from(material, ingredientClient);
    }

    /**
     * Get all providable material by tenant id
     * @param tenantId          Tenant id
     * @param service           ProvidableMaterialService
     * @param ingredientClient  IngredientClient
     * @return                  List&lt;ProvidableMaterialDto&gt;
     */
    public List<ProvidableMaterialDto> getAllProvidableMaterialByTenantId(
            Long                        tenantId,
            ProvidableMaterialService   service,
            IngredientClient            ingredientClient
    ) {
        // Check pre-condition
        assert nonNull(service);

        // Get material list by tenant id
        final var materials = service.getAll(tenantId);

        // Convert entity to DTO and return it
        return ProvidableMaterialDto.from(materials, ingredientClient);
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
