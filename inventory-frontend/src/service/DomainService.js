import {mockSendPeriodStatistics, mockSuggestTaxon} from "../core/models/MockDataModel";
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import axios from "axios";
import { baseIngredientAPI } from "../constant";

export default class DomainService {
    
    /**
     * Suggest taxon
     * @param request
     * @param confirmQuantity
     * @param isMock
     */
    confirmTaxon(request, confirmQuantity, isMock = true) {
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

    /**
     * Send period statistics
     * @param isMock
     */
    sendPeriodStatistics(isMock = true) {
        if (isMock) {
            return mockSendPeriodStatistics();
        }

        const url       = `${baseIngredientAPI()}/restaurant/statistics`;
        const config    = { headers: getHeaderByGatewayStatus() };
        const request   = {};

        return axios.post(url, request, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }
}