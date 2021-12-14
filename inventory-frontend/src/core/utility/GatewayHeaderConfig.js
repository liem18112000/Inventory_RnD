import {ENABLE_GATEWAY, TENANT_ID} from "../../constant";
import {GatewayConfigurationError} from "./GatewayConfigurationError";

/**
 * Mutate header with default tenant id if gateway is not activated
 * Developer can check the variable ENABLE_GATEWAY in constant.js
 * The ENABLE_GATEWAY config must be set. Otherwise, an error will be thrown
 * @param currentHeader Current api call headers
 * @returns {{}|{headers: {tenantId: string}}}
 */
export function getHeaderByGatewayStatus(currentHeader = {}) {

    console.log("Current headers : ", currentHeader);

    // Gateway config is not found
    if(!ENABLE_GATEWAY) {
        console.error("Enable gateway configuration is not found")
        throw new GatewayConfigurationError("Enable gateway configuration is not found")
    }

    // If gateway is npt enabled
    if(ENABLE_GATEWAY !== "true"){
        if(!TENANT_ID){
            console.error("Header is mutated as gateway is not enabled - default tenant is not found")
            throw new GatewayConfigurationError(
                "Header is mutated as gateway is not enabled - default tenant is not found")
        }
        console.log("Header is mutated as gateway is not enabled - default tenantId : ", TENANT_ID)
        return {
            ...currentHeader,
            tenantId: TENANT_ID
        }
    }

    // Otherwise, Return current header if gate way is activated
    return currentHeader
}