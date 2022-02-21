import axios from 'axios'
import { baseSupplierAPI } from '../constant'
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import { addActorNameAndRole } from "../core/utility/RequestActorConfig";
import { FilterRequestMapper } from "../core/models/mapper/ModelMapper";
import { compose } from "../core/utility/ComponentUtility";
import {mockImportSimple} from "../core/models/MockDataModel";

// Supplier base URL
const BaseURL = baseSupplierAPI()

/**
 * Supplier service
 */
export class SupplierService {

    /**
     * Default constructor
     */
    constructor() {
        this.mapper = new FilterRequestMapper();
    }

    /**
     * Get page of supplier group by filter
     * @param filter        Filter on name, description, code and createAt
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageGroup(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve([]);
        }

        const url = `${BaseURL}/group/page`;
        const body = this.mapper.toRequest(filter, page, rows, sortField, sortOrder);
        const config = { headers: getHeaderByGatewayStatus() };

        // Fetch supplier group data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get page of supplier child by filter
     * @param filter        Filter on name and code
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageChild(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve([]);
        }

        const url = `${BaseURL}/child/page`;
        const body = new FilterRequestMapper().toRequest(filter, page, rows, sortField, sortOrder);
        const config = { headers: getHeaderByGatewayStatus() };

        // Fetch supplier child data from api
        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get supplier by Id
     * @param id supplier Id
     * @param isMock Activate mock if true otherwise use real api call
     */
    getByID(id, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/${id}`
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .get(url, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Save supplier by request
     * @param supplier Supplier request
     * @param isMock Activate mock if true otherwise use real api call
     */
    saveSupplier(supplier, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}`;
        const body = addActorNameAndRole(supplier);
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Update supplier by request
     * @param supplier Supplier request
     * @param isMock Activate mock if true otherwise use real api call
     */
    updateSupplier(supplier, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}`;
        const body = addActorNameAndRole(supplier);
        const config = { headers: getHeaderByGatewayStatus({}) }

        return axios
            .put(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get page mat4rial by request
     * @param filter        Filter on name, ingredientId and code
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageMaterial(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve([]);
        }

        const url = `${BaseURL}/providable-material/page`;
        const body = new FilterRequestMapper().toRequest(filter, page, rows, sortField, sortOrder);
        const config = { headers: getHeaderByGatewayStatus() };

        // Fetch supplier child data from api
        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get page import by request
     * @param filter        Filter on name, ingredientId, supplierId and description
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageImport(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve([]);
        }

        const url = `${BaseURL}/import/page`;
        const body = new FilterRequestMapper().toRequest(filter, page, rows, sortField, sortOrder);
        const config = { headers: getHeaderByGatewayStatus() };

        // Fetch supplier child data from api
        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get page import detail by request
     * @param filter        Filter on name and code
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageImportDetail(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve([]);
        }

        const url = `${BaseURL}/import/detail/page`;
        const body = new FilterRequestMapper().toRequest(filter, page, rows, sortField, sortOrder);
        const config = { headers: getHeaderByGatewayStatus() };

        // Fetch supplier child data from api
        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get import by Id
     * @param id Import Id
     * @param isMock Activate mock if true otherwise use real api call
     */
    getImportByID(id, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/import/${id}`
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .get(url, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get import label-value
     * @param isMock Activate mock if true otherwise use real api call
     */
    getImportSimple(isMock = true) {
        if (isMock) {
            return mockImportSimple();
        }

        const url = `${BaseURL}/import/simple`
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .get(url, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get material by id
     * @param id Material ID
     * @param isMock Activate mock if true otherwise use real api call
     */
    getMaterialById(id, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/providable-material/${id}`
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .get(url, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Save import by request
     * @param imports Import request
     * @param isMock Activate mock if true otherwise use real api call
     */
    saveSupplierImport(imports, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/import`;
        const body = addActorNameAndRole(imports);
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Save supplier material by request
     * @param material material request
     * @param isMock Activate mock if true otherwise use real api call
     */
    saveSupplierMaterial(material, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/providable-material`;
        const body = addActorNameAndRole(material);
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Update import by request
     * @param imports Import request
     * @param isMock Activate mock if true otherwise use real api call
     */
    updateSupplierImport(imports, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/import`;
        const body = addActorNameAndRole(imports);
        const config = { headers: getHeaderByGatewayStatus({}) }

        return axios
            .put(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Update material by request
     * @param material Material request
     * @param isMock Activate mock if true otherwise use real api call
     */
    updateSupplierMaterial(material, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/providable-material`;
        const body = addActorNameAndRole(material);
        const config = { headers: getHeaderByGatewayStatus({}) }

        return axios
            .put(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get import detail by id
     * @param id Import detail Id
     * @param isMock Activate mock if true otherwise use real api call
     */
    getImportDetailByID(id, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/import/detail/${id}`
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .get(url, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Save import detail request
     * @param imports Import detail request
     * @param isMock Activate mock if true otherwise use real api call
     */
    saveSupplierImportDetail(imports, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/import/detail`;
        const body = addActorNameAndRole(imports);
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Update import detail by request
     * @param imports Import detail request
     * @param isMock Activate mock if true otherwise use real api call
     */
    updateSupplierImportDetail(imports, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const url = `${BaseURL}/import/detail`;
        const body = addActorNameAndRole(imports);
        const config = { headers: getHeaderByGatewayStatus({}) }

        return axios
            .put(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Delete import by id
     * @param id Import Id
     * @param isMock Activate mock if true otherwise use real api call
     * @returns {Promise<unknown>|Promise<>}
     */
    deleteSupplierImportDetail(id, isMock = true) {
        if (isMock) {
            return Promise.resolve(null);
        }

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
            )()
        }

        return axios
            .delete(`${BaseURL}/import/detail/${id}`, config)
    }
}
