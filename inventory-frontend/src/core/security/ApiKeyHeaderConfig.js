/**
 * Config to add x-api-key with API_KEY value to authorize request
 * @param currentHeader current header
 */
import {API_KEY, API_KEY_HEADER, API_PRINCIPAL, API_PRINCIPAL_HEADER} from "../../constant";
import { isJson } from "../utility/ComponentUtility";

/**
 * Validate the pre-condition of system configuration and current header
 * @param currentHeader
 */
const validatePreConditionForAuthorize = (currentHeader) => {

    // If api key configuration is not found
    if (!API_KEY) {
        console.error("Api key configuration is not found");
        throw new Error("Api key configuration is not found");
    }

    // If api key configuration is not found
    if (!API_KEY_HEADER) {
        console.error("Key header configuration is not found");
        throw new Error("Key header configuration is not found");
    }

    // If current header is not a JSON
    if (!isJson(currentHeader)) {
        console.error("Current is not a JSON", currentHeader);
        throw new Error("Current is not a JSON");
    }
}

/**
 * Validate the pre-condition of system configuration and current header
 * @param currentHeader
 */
const validatePreconditionForAuthenticate = (currentHeader) => {

    validatePreConditionForAuthorize(currentHeader);

    if (!API_PRINCIPAL) {
        console.error("Api principal configuration is not found");
        throw new Error("Api principal configuration is not found");
    }

    if (!API_PRINCIPAL_HEADER) {
        console.error("Principal header configuration is not found");
        throw new Error("Principal header configuration is not found");
    }
}

export const authorizeWithApiKey = (currentHeader = {}) => {

    // Validate the pre-condition of system configuration and current header
    validatePreConditionForAuthorize(currentHeader);

    // Add api key header
    currentHeader[API_KEY_HEADER] = API_KEY;
    return currentHeader;
}

export const authenticateWithApiKeyAndPrincipal = (currentHeader = {}) => {

    // Authorize header
    currentHeader = authorizeWithApiKey(currentHeader);

    // Validate for authenticate
    validatePreconditionForAuthenticate(currentHeader);

    // Add api principal to header
    currentHeader[API_PRINCIPAL_HEADER] = API_PRINCIPAL;
    return currentHeader
}