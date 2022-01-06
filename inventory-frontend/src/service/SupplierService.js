import axios from 'axios'
import { baseIngredientAPI } from '../constant'
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import { addActorNameAndRole } from "../core/utility/RequestActorConfig";
import {
    mockIngredient,
    mockIngredientItem,
    mockIngredientLabelValue,
    mockPageIngredientCategory,
    mockPageIngredientItem,
    mockPageIngredientType,
    mockPageInventory
} from "../core/models/MockDataModel";
import { FilterRequestMapper } from "../core/models/mapper/ModelMapper";

// Ingredient base URL
const BaseURL = baseIngredientAPI()

/**
 * Ingredient service
 */
export const IngredientService = {

    getPageCategory(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return mockPageIngredientCategory();
        }

        const url = `${BaseURL}/category/page`;
        const body = this.mapper.toRequest(filter, page, rows, sortField, sortOrder);
        const config = { headers: getHeaderByGatewayStatus() };

        // Fetch ingredient category data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    },

    getByID(id, isMock = true) {
        if (isMock) {
            return mockIngredient();
        }

        const headers = getHeaderByGatewayStatus({})

        return axios
            .get(`${BaseURL}/${id}`, {
                headers: headers
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    },

    saveIngredient(ingredient, isMock = true) {
        if (isMock) {
            return mockIngredient();
        }

        // Add actor role and actor name to request body as default
        const requestBody = addActorNameAndRole(ingredient);

        return axios
            .post(`${BaseURL}`, requestBody, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }
}