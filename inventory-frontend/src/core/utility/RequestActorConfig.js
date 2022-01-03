import {isJson} from "./ComponentUtility";

export const defaultActorName = "inventory-keeper";
export const defaultActorRole = "inventory-keeper";

export const adminActorName = "client-admin";
export const adminActorRole = "client-admin";

export const superAdminActorName = "super-admin";
export const superAdminActorRole = "super-admin";

/**
 * Add actor role and actor name to current request
 * @param currentRequest    Current request
 * @param actorName         Actor name
 * @param actorRole         Actor role
 * @returns {{actorRole: string, actorName: string}|{}}
 */
export const addActorNameAndRole = (
    currentRequest = {},
    actorName = defaultActorName,
    actorRole = defaultActorRole
) => {

    if(!currentRequest || !isJson(currentRequest)) {
        console.warn("Request is either null or not a JSON.");
        return currentRequest;
    }

    if(!actorName || actorName.length === 0) {
        console.warn("Actor name is either null or blank");
        return currentRequest;
    }

    if(!actorRole || actorRole.length === 0) {
        console.warn("Actor role is either null or blank")
        return currentRequest;
    }

    console.log("Request add actor name and actor role");
    return {
        ...currentRequest,
        actorRole: actorRole,
        actorName: actorName
    };

}