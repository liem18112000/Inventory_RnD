import { mockSendPeriodStatistics, mockSuggestTaxon } from "../core/models/MockDataModel";
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import axios from "axios";
import { baseIngredientAPI } from "../constant";
import { compose } from "../core/utility/ComponentUtility";
import { authenticateWithApiKeyAndPrincipal } from "../core/security/ApiKeyHeaderConfig";
import {handleExceptionWithSentry} from "../core/utility/integrations/SentryExceptionResolver";

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

        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )(),
            params: { quantity: confirmQuantity }
        }

        // fetch ingredient suggest taxon data from api
        return axios.post(url, request, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error, true));
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
        const config = {
            headers: compose(
                getHeaderByGatewayStatus,
                authenticateWithApiKeyAndPrincipal
            )()
        }
        const request   = {};

        return axios.post(url, request, config)
            .then(res => res.data)
            .catch(error => handleExceptionWithSentry(error, true));
    }
}