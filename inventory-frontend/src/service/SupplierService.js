import axios from 'axios'
import { baseSupplierAPI } from '../constant'
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import { addActorNameAndRole } from "../core/utility/RequestActorConfig";
import { FilterRequestMapper } from "../core/models/mapper/ModelMapper";
import { compose } from "../core/utility/ComponentUtility";

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

    getPageChild(parentId, filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve([]);
        }

        const url = `${BaseURL}/child/page`;
        const request = { ...filter, parentId: parentId };
        const body = new FilterRequestMapper().toRequest(request, filter, page, rows, sortField, sortOrder);
        const config = { headers: getHeaderByGatewayStatus() };

        // Fetch supplier child data from api
        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

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
}