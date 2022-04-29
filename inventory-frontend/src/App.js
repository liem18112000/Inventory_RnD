import 'primereact/resources/themes/md-light-indigo/theme.css'
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import { createBrowserHistory } from 'history';
import { Router, Switch } from 'react-router';
import React from "react";
import KeycloakPrivateRoute from "./core/security/KeycloakPrivateRoute";
import {useKeycloak} from "@react-keycloak/web";
import * as Sentry from "@sentry/react";
import {sentryConfiguration} from "./core/utility/SentryConfiguration";
import {renderRoute} from "./config/Routes";

export const history = createBrowserHistory();

function App() {

  Sentry.init(sentryConfiguration);

  const { keycloak } = useKeycloak();

  const routes = renderRoute();

  return (
    <Router history={history}>
      <Switch>
        <KeycloakPrivateRoute keycloak={keycloak}>
          {routes}
        </KeycloakPrivateRoute>
      </Switch>
    </Router>
  )
}

export default Sentry.withProfiler(App);
