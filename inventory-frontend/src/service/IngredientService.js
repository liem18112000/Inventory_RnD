import axios from 'axios'
import { baseIngredientAPI } from '../constant'
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import { addActorNameAndRole } from "../core/utility/RequestActorConfig";
import {
    mockGetConfigByIngredient,
    mockIngredient,
    mockIngredientItem,
    mockIngredientLabelValue,
    mockPageIngredientCategory,
    mockPageIngredientItem,
    mockPageIngredientType,
    mockPageInventory,
    mockSuggestTaxon,
    mockUpdateIngredientConfig
} from "../core/models/MockDataModel";
import { FilterRequestMapper } from "../core/models/mapper/ModelMapper";
import { compose } from "../core/utility/ComponentUtility";
import { authenticateWithApiKeyAndPrincipal, authorizeWithApiKey } from "../core/security/ApiKeyHeaderConfig";
import { handleExceptionWithSentry } from "../core/utility/integrations/SentryExceptionResolver";

// Ingredient base URL
const BaseURL = baseIngredientAPI()

/**
 * Ingredient service
 */
export class IngredientService {

    /**
     * Default constructor
     */
    constructor() {
        this.mapper = new FilterRequestMapper();
    }

    /**
     * Get page of category ingredient by filter
     * @param filter        Filter on name, description, code and createAt
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageCategory(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return mockPageIngredientCategory();
        }

        const url       = `${BaseURL}/category/page`;
        const body      = this.mapper.toRequest(filter, page, rows, sortField, sortOrder);
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        // Fetch ingredient category data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Sync items in inventory
     */
    syncInventory() {
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        return axios.post(`${BaseURL}/inventory/sync`, {}, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Get page of Ingredient type with filter
     * @param parentId      Ingredient Parent ID (Ingredient Category ID)
     * @param filter        Filter by name, description, code, unit, unit type, created at and update at
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageType(parentId, filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return mockPageIngredientType();
        }

        const url       = `${BaseURL}/type/page`;
        const request   = { ...filter, parentId: parentId };
        const body      = this.mapper.toRequest(request, page, rows, sortField, sortOrder);
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        // fetch ingredient type data from api 
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Get page of item by ingredient detail id (ingredient type id) with filter
     * @param ingredientId      Ingredient Detail ID (Ingredient Type id)
     * @param filter            Filter by name, description, code, unit, unit type and created at
     * @param page              Page
     * @param rows              Size per page
     * @param sortField         Sorting field (default field is id)
     * @param sortOrder         Sorting order (default order is desc)
     * @param isMock            Activate mock if true otherwise use real api call
     */
    getPageItem(ingredientId, filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return mockPageIngredientItem();
        }

        const url       = `${BaseURL}/item/page`;
        const request   = { ...filter, ingredientId: ingredientId };
        const body      = this.mapper.toRequest(request, page, rows, sortField, sortOrder);
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        // fetch ingredient item data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Get page of inventory statistics from inventory for each ingredient detail (ingredient type)
     * @param filter            Filter by name, description, code, unit, unit type and updated at
     * @param page              Page
     * @param rows              Size per page
     * @param sortField         Sorting field (default field is id)
     * @param sortOrder         Sorting order (default order is desc)
     * @param isMock            Activate mock if true otherwise use real api call
     */
    getPageInventory(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return mockPageInventory();
        }

        const url       = `${BaseURL}/inventory/page`;
        const body      = this.mapper.toRequest(filter, page, rows, sortField, sortOrder);
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        // fetch ingredient inventory data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Get list of label-value of ingredient detail (ingredient type)
     * Label will be ingredient detail name and value is its ID
     * @param isMock Activate mock if true otherwise use real api call
     */
    getInventoryIngredientDetail(isMock = true) {
        if (isMock) {
            return mockIngredientLabelValue();
        }

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authorizeWithApiKey
            )()
        }

        return axios.get(`${BaseURL}/type/simple`, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Get ingredient (both category and detail) by id
     * @param id        Ingredient id
     * @param isMock    Activate mock if true otherwise use real api call
     */
    getByID(id, isMock = true) {
        if (isMock) {
            return mockIngredient();
        }

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authorizeWithApiKey
            )()
        }

        return axios
            .get(`${BaseURL}/${id}`, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Get item by id
     * @param id        Item id
     * @param isMock    Activate mock if true otherwise use real api call
     */
    getItemByID(id, isMock = true) {
        if (isMock) {
            return mockIngredientItem();
        }

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authorizeWithApiKey
            )()
        }

        return axios
            .get(`${BaseURL}/item/${id}`, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Get all unit type as list
     * @param isMock    Activate mock if true otherwise use real api call
     */
    getUnitTypes(isMock = true) {
        if (isMock) {
            return Promise.resolve([
                "length",
                "volume",
                "area",
                "weight",
                "whole"
            ]);
        }

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authorizeWithApiKey
            )()
        }

        return axios.get(`${BaseURL}/unit-type`, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error))
    }

    /**
     * Get unit of a specific unit type
     * @param unitType      Measurement unit type
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getUnit(unitType, isMock = true) {
        if (isMock) {
            return Promise.resolve([
                "bottle",
                "can",
                "pack",
                "piece",
                "box"
            ]);
        }

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authorizeWithApiKey
            )()
        }

        return axios.get(`${BaseURL}/unit?unit=${unitType}`, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error))
    }

    /**
     * Save ingredient (both category and type)
     * @param ingredient    Ingredient need to be saved
     * @param isMock        Activate mock if true otherwise use real api call
     */
    saveIngredient(ingredient, isMock = true) {
        if (isMock) {
            return mockIngredient();
        }

        // Add actor role and actor name to request body as default
        const requestBody = addActorNameAndRole(ingredient);
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        return axios
            .post(`${BaseURL}`, requestBody, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Save item
     * @param item      Item need to be saved
     * @param isMock    Activate mock if true otherwise use real api call
     */
    saveItem(item, isMock = true) {
        if (isMock) {
            return mockPageIngredientItem();
        }

        // Add actor role and actor name to request body as default
        const requestBody = addActorNameAndRole(item);

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        return axios
            .post(`${BaseURL}/item`, requestBody, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Save item batch
     * @param items     Item need to be saved
     * @param isMock    Activate mock if true otherwise use real api call
     */
    saveItemBatch(items, isMock = true) {
        if (isMock) {
            return mockPageIngredientItem();
        }

        // Add actor role and actor name to request body as default
        const requestBody = addActorNameAndRole(items);

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        return axios
            .post(`${BaseURL}/item/batch`, requestBody, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Update ingredient (both category and type)
     * @param ingredient    Ingredient need to be updated
     * @param isMock        Activate mock if true otherwise use real api call
     */
    updateIngredient(ingredient, isMock = true) {
        if (isMock) {
            return mockIngredient();
        }

        // Add actor role and actor name to request body as default
        const requestBody = addActorNameAndRole(ingredient);
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        return axios
            .put(`${BaseURL}`, requestBody, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Update item
     * @param item      Item need to be updated
     * @param isMock    Activate mock if true otherwise use real api call
     */
    updateItem(item, isMock = true) {
        if (isMock) {
            return mockIngredientItem();
        }

        // Add actor role and actor name to request body as default
        const requestBody = addActorNameAndRole(item);

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        return axios
            .put(`${BaseURL}/item/${item.id}`, requestBody, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    /**
     * Delete ingredient by id
     * @param id        Ingredient ID
     * @param isMock    Activate mock if true otherwise use real api call
     */
    deleteIngredient(id, isMock = true) {
        if (isMock) {
            return Promise.resolve(null);
        }

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        return axios.delete(`${BaseURL}/${id}`, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    getSuggestTaxon(isMock = true) {
        if (isMock) {
            return mockSuggestTaxon();
        }

        const url = `${BaseURL}/restaurant/suggest`;
        const body = {}
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        // Fetch ingredient category data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    updateIngredientConfig(id, body, isMock = true) {
        if (isMock) {
            return mockUpdateIngredientConfig()
        }

        const url = `${BaseURL}/config/${id}`;
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }

        // Fetch ingredient category data from api
        return axios.put(url, body, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }

    getIngredientConfig(ingredientId, isMock = true) {
        if (isMock) {
            return mockGetConfigByIngredient()
        }

        const url = `${BaseURL}/${ingredientId}/config`;
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authorizeWithApiKey
            )()
        }

        // Fetch ingredient category data from api
        return axios.get(url, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error));
    }
}