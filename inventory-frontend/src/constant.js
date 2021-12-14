
/*
    Export global constant
*/
export const TENANT_ID          = process.env.REACT_APP_TENANT_ID.trim()
export const BASE_URL           = process.env.REACT_APP_BASE_URL.trim()
export const VERSION            = process.env.REACT_APP_VERSION.trim()
export const INGREDIENT_PATH    = process.env.REACT_APP_INGREDIENT_PATH.trim()
export const RECIPE_PATH        = process.env.REACT_APP_RECIPE_PATH.trim()
export const SUPPLIER_PATH      = process.env.REACT_APP_SUPPLIER_PATH.trim()
export const NOTIFICATION_PATH  = process.env.REACT_APP_NOTIFICATION_PATH.trim()
export const ENABLE_GATEWAY     = process.env.REACT_APP_ENABLE_GATEWAY.trim()

/**
 * Base api for Ingredient service
 * @returns Ingredient service url (domain/path-to-service/version)
 */
export function baseAPI () {
    return BASE_URL + "/" + INGREDIENT_PATH + "/" + VERSION
} 

/**
 * Base api for Recipe service
 * @returns Recipe service url (domain/path-to-service/version)
 */
export function baseRecipeAPI () {
    return BASE_URL + "/" + RECIPE_PATH + "/" + VERSION
} 

/**
 * Base api for Supplier service
 * @returns Supplier service url (domain/path-to-service/version)
 */
export function baseSupplierAPI() {
    return BASE_URL + "/" + SUPPLIER_PATH + "/" + VERSION
}

/**
 * Base api for Notification service
 * @returns Notification service url (domain/path-to-service/version)
 */
export function baseNotificationAPI() {
    return BASE_URL + "/" + NOTIFICATION_PATH + "/" + VERSION
}