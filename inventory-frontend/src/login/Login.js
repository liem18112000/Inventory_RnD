import React, { useEffect, useState } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Password } from 'primereact/password';
import { Checkbox } from 'primereact/checkbox';
import { Dialog } from 'primereact/dialog';
import { Divider } from 'primereact/divider';
import '../assets/styles/FormDemo.css';

export const Login = () => {
    const [showMessage, setShowMessage] = useState(false);
    const [formData, setFormData] = useState({});

    const dialogFooter = <div className="p-d-flex p-jc-center"><Button label="OK" className="p-button-text" autoFocus onClick={() => setShowMessage(false)} /></div>;
    const passwordHeader = <h6>Pick a password</h6>;
    const passwordFooter = (
        <React.Fragment>
            <Divider />
            <p className="p-mt-2">Suggestions</p>
            <ul className="p-pl-2 p-ml-2 p-mt-0" style={{ lineHeight: '1.5' }}>
                <li>At least one lowercase</li>
                <li>At least one uppercase</li>
                <li>At least one numeric</li>
                <li>Minimum 8 characters</li>
            </ul>
        </React.Fragment>
    );

    const handleSubmit = e => {
        e.preventDefault();
        console.log(e.target.email.value);

        if (!e.target.email.value) {
            alert("Email is required");
        } else if (!e.target.email.value) {
            alert("Valid email is required");
        } else if (!e.target.password.value) {
            alert("Password is required");
        } else if (
            e.target.email.value === "admin-acc" &&
            e.target.password.value === "123456"
        ) {
            alert("Successfully logged in");
            e.target.email.value = "";
            e.target.password.value = "";
        } else {
            alert("Wrong email or password combination");
        }
    };

    const handleClick = e => {
        e.preventDefault();

        alert("Goes to registration page");
    };

    return (
        <div className="form-demo">
            <Dialog visible={showMessage} onHide={() => setShowMessage(false)} position="top" footer={dialogFooter} showHeader={false} breakpoints={{ '960px': '80vw' }} style={{ width: '30vw' }}>
                <div className="p-d-flex p-ai-center p-dir-col p-pt-6 p-px-3">
                    <i className="pi pi-check-circle" style={{ fontSize: '5rem', color: 'var(--green-500)' }}></i>
                    <h5>Registration Successful!</h5>
                    <p style={{ lineHeight: 1.5, textIndent: '1rem' }}>
                        Your account is registered under name <b>{formData.name}</b> ; it'll be valid next 30 days without activation. Please check <b>{formData.email}</b> for activation instructions.
                    </p>
                </div>
            </Dialog>

            <div className="p-d-flex p-jc-center">
                <div className="card">
                    <h3 className="p-text-center title">Inventory System</h3>
                    <h4 className="p-text-center">Welcome back</h4>
                    <form className="p-fluid">
                        <div className="p-field">
                            <span className="p-float-label">
                                <InputText id="username" onSubmit={handleSubmit} />
                                <label htmlFor="username">Username</label>
                            </span>
                        </div>
                        <div className="p-field">
                            <span className="p-float-label">
                                <Password id="password" name="password" toggleMask
                                    header={passwordHeader} footer={passwordFooter} onSubmit={handleSubmit} />
                                <label htmlFor="password" >Password</label>
                            </span>
                        </div>
                        <div className="p-field-checkbox">
                            <Checkbox inputId="accept" name="accept" />
                            <label htmlFor="accept" >Remember me</label>
                        </div>

                        <Button type="submit" label="Submit" className="p-mt-2" onSubmit={handleClick} />
                    </form>
                </div>
            </div>
        </div>
    );
}