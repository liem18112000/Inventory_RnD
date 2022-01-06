import React, { useRef } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Password } from 'primereact/password';
import { Checkbox } from 'primereact/checkbox';
import { Toast } from 'primereact/toast';
import '../assets/styles/FormDemo.css';

export const Login = ({onAuthenticate}) => {
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
        <div className="form-demo">
            <Toast ref={toast} />
            <div className="p-d-flex p-jc-center">
                <div className="card">
                    <h3 className="p-text-center title">Inventory System</h3>
                    <h4 className="p-text-center">Welcome back</h4>
                    <form className="p-fluid" onSubmit={handleSubmit}>
                        <div className="p-field">
                            <span className="p-float-label">
                                <InputText id="username" name="username" />
                                <label htmlFor="username">Username</label>
                            </span>
                        </div>
                        <div className="p-field">
                            <span className="p-float-label">
                                <Password id="password" name="password" toggleMask feedback={false} />
                                <label htmlFor="password" >Password</label>
                            </span>
                        </div>
                        <div className="p-field-checkbox">
                            <Checkbox inputId="accept" name="accept" />
                            <label htmlFor="accept" >Remember me</label>
                        </div>

                        <Button type="submit" label="Submit" className="p-mt-2" />
                    </form>
                </div>
            </div>
        </div>
    );
}