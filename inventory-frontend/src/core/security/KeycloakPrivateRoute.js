import { useKeycloak } from "@react-keycloak/web";

const KeycloakPrivateRoute = ({ children }) => {

    const { keycloak } = useKeycloak();

    const isLoggedIn = keycloak.authenticated;

    return isLoggedIn ? children : null;
};

export default KeycloakPrivateRoute;