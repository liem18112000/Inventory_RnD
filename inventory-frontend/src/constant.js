
/*
    Export global constant
*/
export const VERSION            = process.env.REACT_APP_VERSION.trim()
export const TENANT_ID          = process.env.REACT_APP_TENANT_ID.trim()
export const BASE_URL           = process.env.REACT_APP_BASE_URL.trim()
export const ENABLE_GATEWAY     = process.env.REACT_APP_ENABLE_GATEWAY.trim()

export const INGREDIENT_PATH    = process.env.REACT_APP_INGREDIENT_PATH.trim()
export const INGREDIENT_URL     = process.env.REACT_APP_INGREDIENT_URL.trim()
export const RECIPE_PATH        = process.env.REACT_APP_RECIPE_PATH.trim()
export const RECIPE_URL         = process.env.REACT_APP_RECIPE_URL.trim()
export const SUPPLIER_PATH      = process.env.REACT_APP_SUPPLIER_PATH.trim()
export const SUPPLIER_URL       = process.env.REACT_APP_SUPPLIER_URL.trim()
export const NOTIFICATION_PATH  = process.env.REACT_APP_NOTIFICATION_PATH.trim()
export const NOTIFICATION_URL   = process.env.REACT_APP_NOTIFICATION_URL.trim()

export const API_KEY            = process.env.REACT_APP_API_KEY.trim()
export const API_PRINCIPAL      = process.env.REACT_APP_API_PRINCIPAL.trim()
export const API_KEY_HEADER             = "x-api-key"
export const API_PRINCIPAL_HEADER       = "x-principal"

export const KEYCLOAK_REALM      = process.env.REACT_APP_KEYCLOAK_REALM.trim()
export const KEYCLOAK_CLIENT     = process.env.REACT_APP_KEYCLOAK_CLIENT.trim()
export const KEYCLOAK_SERVER     = process.env.REACT_APP_KEYCLOAK_SERVER.trim()

/**
 * Base api for Ingredient service
 * @returns Ingredient service url (domain/path-to-service/version)
 */
export function baseIngredientAPI () {
    if(ENABLE_GATEWAY && ENABLE_GATEWAY === 'true'){
        return `${BASE_URL}/${INGREDIENT_PATH}/${VERSION}`
    }
    return `${INGREDIENT_URL}/${INGREDIENT_PATH}/${VERSION}`
} 

/**
 * Base api for Recipe service
 * @returns Recipe service url (domain/path-to-service/version)
 */
export function baseRecipeAPI () {
    if(ENABLE_GATEWAY && ENABLE_GATEWAY === 'true'){
        return `${BASE_URL}/${RECIPE_PATH}/${VERSION}`
    }
    return `${RECIPE_URL}/${RECIPE_PATH}/${VERSION}`
} 

/**
 * Base api for Supplier service
 * @returns Supplier service url (domain/path-to-service/version)
 */
export function baseSupplierAPI() {
    if(ENABLE_GATEWAY && ENABLE_GATEWAY === 'true'){
        return `${BASE_URL}/${SUPPLIER_PATH}/${VERSION}`
    }
    return `${SUPPLIER_URL}/${SUPPLIER_PATH}/${VERSION}`
}

/**
 * Base api for Notification service
 * @returns Notification service url (domain/path-to-service/version)
 */
export function baseNotificationAPI() {
    if(ENABLE_GATEWAY && ENABLE_GATEWAY === 'true'){
        return `${BASE_URL}/${NOTIFICATION_PATH}/${VERSION}`
    }
    return `${NOTIFICATION_URL}/${NOTIFICATION_PATH}/${VERSION}`
}