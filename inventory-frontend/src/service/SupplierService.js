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
export const IngredientService = {

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
    },

    getPageChild(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve([]);
        }

        const url = `${BaseURL}/child/page`;
        const body = this.mapper.toRequest(filter, page, rows, sortField, sortOrder);
        const config = { headers: getHeaderByGatewayStatus() };

        // Fetch supplier child data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    },

    getByID(id, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        const headers = getHeaderByGatewayStatus({})

        return axios
            .get(`${BaseURL}/${id}`, {
                headers: headers
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    },

    saveSupplier(ingredient, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        // Add actor role and actor name to request body as default
        const requestBody = addActorNameAndRole(ingredient);

        return axios
            .post(`${BaseURL}`, requestBody, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    },

    uppdateSupplier(ingredient, isMock = true) {
        if (isMock) {
            return Promise.resolve({});
        }

        // Add actor role and actor name to request body as default
        const requestBody = addActorNameAndRole(ingredient);

        return axios
            .push(`${BaseURL}`, requestBody, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }
}