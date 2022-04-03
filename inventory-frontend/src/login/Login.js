import React, { useRef } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Password } from 'primereact/password';
// import { Checkbox } from 'primereact/checkbox';
import { Toast } from 'primereact/toast';
import '../assets/styles/FormDemo.css';
// import 'bootstrap/dist/css/bootstrap.min.css';
import '../assets/styles/Test.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import '../assets/styles/Login.css';
export const Login = ({ onAuthenticate }) => {
    const toast = useRef(null);

    const handleSubmit = e => {
        e.preventDefault();
        if (!e.target.username.value) {
            toast.current.show({ severity: 'warn', summary: 'Warning', detail: 'Username is required', life: 1000 });
        } else if (!e.target.password.value) {
            toast.current.show({ severity: 'warn', summary: 'Warning', detail: 'Password is required', life: 1000 });
        } else {
            const username = e.target.username.value;
            const password = e.target.password.value
            onAuthenticate(username, password)
        }
    };

    return (
        <div className="maincontainer">
            <Toast ref={toast} />
            <div class="container-fluid">
                <div class="row no-gutter">
                    <div class="col-md-6 bg-light">
                        <div class="login d-flex align-items-center py-5">
                            <div class="container">
                                <div class="row">
                                    <div class="col-lg-10 col-xl-7 mx-auto">
                                        <img class="logo-image" alt="" src="https://res.cloudinary.com/ieltstinder/image/upload/v1648977464/Group_95_w4o4cb.png"></img>
                                        <h4 class="display-5">Login</h4>
                                        <form className="p-fluid" onSubmit={handleSubmit}>
                                            <div className="p-field">
                                                {/* <label htmlFor="email" className="block text-900 font-medium mb-2">Username</label> */}
                                                <h6>Username</h6>
                                                <span >
                                                    <InputText placeholder="Username" id="username" name="username" />
                                                </span>
                                            </div>
                                            <div className="p-field">
                                                {/* <label htmlFor="email" className="block text-900 font-medium mb-2">Password</label> */}
                                                <h6>Password</h6>
                                                <span>
                                                    <Password placeholder="Password" id="password" name="password" toggleMask feedback={false} />
                                                </span>
                                            </div>

                                            <label><input type="checkbox" /> Remember Me</label>
                                            <Button type="submit" label="Login" className="p-mt-2" />
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 d-none d-md-flex bg-image"></div>
                </div>
            </div>
        </div>
    );
}