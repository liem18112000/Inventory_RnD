import { mockSuggestTaxon } from "../core/models/MockDataModel";
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import axios from "axios";
import { baseIngredientAPI } from "../constant";

export default class DomainService {
    
    /**
     * Suggest taxon
     * @param request
     * @param isMock
     */
    confirmTaxon(request, isMock = true) {
        if (isMock) {
            return mockSuggestTaxon();
        }

        if (!request.quantity <= 0) {
            throw Error("Quantity must be equal or larger than 1");
        }

        if (!request.details || !(request.constructor === Array) || request.details.length === 0) {
            throw Error("details is null or not array or an empty array")
        }

        const url       = `${baseIngredientAPI()}/restaurant/confirm`;
        const config    = {
            headers: getHeaderByGatewayStatus(),
            params: { quantity: request.quantity }
        };

        // fetch ingredient suggest taxon data from api
        return axios.post(url, request, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }
}