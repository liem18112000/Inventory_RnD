import { mockSuggestTaxon } from "../core/models/MockDataModel";
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import axios from "axios";
import { baseIngredientAPI } from "../constant";

export default class DomainService {
    /**
     * Suggest taxon
     * @param isMock
     */
    suggestTaxon(isMock = true) {
        if (isMock) {
            return mockSuggestTaxon();
        }

        const url       = `${baseIngredientAPI()}/restaurant/suggest`;
        const config    = { headers: getHeaderByGatewayStatus() };

        // fetch ingredient suggest taxon data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }
}