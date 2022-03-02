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
    confirmTaxon(request, confirmQuantity, isMock = true) {
        console.log(request);
        if (isMock) {
            return mockSuggestTaxon();
        }

        if (confirmQuantity <= 0) {
            throw Error("Quantity must be equal or larger than 1");
        }

        const url       = `${baseIngredientAPI()}/restaurant/confirm`;
        const config    = {
            headers: getHeaderByGatewayStatus(),
            params: { quantity: confirmQuantity }
        };

        // fetch ingredient suggest taxon data from api
        return axios.post(url, request, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }
}