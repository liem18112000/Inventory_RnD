import Keycloak from "keycloak-js";
import {
    KEYCLOAK_REALM,
    KEYCLOAK_CLIENT,
    KEYCLOAK_SERVER
} from "../../constant";

const keycloakConfiguration = new Keycloak({
    url: `${KEYCLOAK_SERVER}/auth`,
    realm: KEYCLOAK_REALM,
    clientId: KEYCLOAK_CLIENT,
    release: "inventory@1.1.0"
});

export default keycloakConfiguration;