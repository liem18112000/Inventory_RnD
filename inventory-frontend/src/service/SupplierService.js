import axios from 'axios'
import { baseSupplierAPI } from '../constant'
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import { addActorNameAndRole } from "../core/utility/RequestActorConfig";
import { FilterRequestMapper } from "../core/models/mapper/ModelMapper";

// Ingredient base URL
const BaseURL = baseSupplierAPI()

/**
 * Ingredient service
 */
export const SupplierService = {

    getPageGroup(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve([]);
        }

        const url = `${BaseURL}/group/page`;
        const body = new FilterRequestMapper().toRequest(filter, page, rows, sortField, sortOrder);
        const config = { headers: getHeaderByGatewayStatus() };

        // Fetch supplier group data from api
        return axios
            .post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    },

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
    },

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
    },

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
    },

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