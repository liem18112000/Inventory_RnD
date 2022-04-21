import { useKeycloak } from "@react-keycloak/web";
import { Login } from "../../login/Login";
import * as Sentry from "@sentry/react";

const KeycloakPrivateRoute = ({ children }) => {

    const { keycloak } = useKeycloak();

    const isLoggedIn = keycloak.authenticated;

    if (isLoggedIn) {
        Sentry.setUser(keycloak.tokenParsed);
    }

    return isLoggedIn ? children : <Login />;
};

export default KeycloakPrivateRoute;