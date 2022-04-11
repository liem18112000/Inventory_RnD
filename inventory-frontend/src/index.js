import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

// ant design
import "antd/dist/antd.css";
import keycloakConfiguration from "./core/security/KeycloakConfiguration";
import { ReactKeycloakProvider } from '@react-keycloak/web'

ReactDOM.render(
  <React.StrictMode>
    <ReactKeycloakProvider authClient={keycloakConfiguration}>
      <App />
    </ReactKeycloakProvider>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
