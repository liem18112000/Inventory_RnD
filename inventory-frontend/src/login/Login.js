import React from 'react';
import { Button } from 'primereact/button';
import '../assets/styles/FormDemo.css';
import '../assets/styles/Test.css';
import '../assets/styles/Login.css';
import {useKeycloak} from "@react-keycloak/web";
export const Login = () => {
    const { keycloak } = useKeycloak();
    return (
        <div className="maincontainer">
            <div className="container-fluid">
                <div className="row no-gutter">
                    <div className="col-md-6 bg-light">
                        <div className="login d-flex align-items-center py-5">
                            <div className="container">
                                <div className="row">
                                    <div className="col-lg-10 col-xl-7 mx-auto">
                                        <img className="logo-image" alt="" src="https://res.cloudinary.com/ieltstinder/image/upload/v1648977464/Group_95_w4o4cb.png"/>
                                        <h4 className="display-5">Login</h4>
                                        <Button label="Please login to continue" onClick={() => keycloak.login()} className="p-mt-2" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-6 d-none d-md-flex bg-image"></div>
                </div>
            </div>
        </div>
    );
}