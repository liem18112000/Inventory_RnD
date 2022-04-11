import { useKeycloak } from "@react-keycloak/web";
import { Login } from "../../login/Login";

const KeycloakPrivateRoute = ({ children }) => {

    const { keycloak } = useKeycloak();

    const isLoggedIn = keycloak.authenticated;

    return isLoggedIn ? children : <Login />;
};

export default KeycloakPrivateRoute;