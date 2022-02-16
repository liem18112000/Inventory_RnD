import React, { Component } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Sidebar } from 'primereact/sidebar';
import classNames from 'classnames';
import { Toast } from 'primereact/toast';
import { sleep } from "../../core/utility/ComponentUtility";
import { NotificationService } from '../../service/NotificationService';
import moment from 'moment';

export class EventForm extends Component {

    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            data: {
                id: null,
                name: '',
                eventType: '',
                occurAt: '',
                description: '',
                active: true
            },
            isMock: false,
            visible: false,
            errors: {},
        }
        this.notificationService = new NotificationService();
    }

    /**
     * Function is called after component is required
     */
    componentDidMount() {
    }

    action = (id) => {
        this.setUpdateInformation(id);
    }

    setUpdateInformation(id) {
        this.notificationService.getEventById(id, this.state.isMock).then(data => {
            this.setState({
                data: {
                    id: data ? data.id : null,
                    name: data ? data.name : '',
                    eventType: data ? data.eventType : '',
                    occurAt: data ? data.occurAt : '',
                    description: data ? data.description : '',
                },
                id: data ? data.id : null,
                visible: true,
            })
        })
    }

    /**
     * Check required field
     * @param field 
     * @returns {boolean}
     */
    requireField = (field) => {
        return field && field.length > 0;
    }

    /**
     * Check the submit validation is valid
     * @returns {boolean}
     */
    isSubmitValid = () => {
        console.log(this.state.errors);
        return !(this.state.errors.name || this.state.errors.code);
    }

    /**
     * Validating all field in the form (required fields)
     * @param callback The callback on processing the next transaction
     */
    validateSubmit(callback) {
        this.setState({
            ...this.state,
            errors: {
                name: !this.requireField(this.state.data.name) ? "Event name is required" : null,
            }
        }, callback)
    }

    /**
     * Show and reader validation fail errors
     * @returns {JSX.Element}
     */
    showValidateMessage = () => {
        return (
            <div className="p-flex p-flex-column" style={{ flex: '1' }}>
                <div className="p-text-center">
                    <h3>Fail Validation</h3>
                </div>
                <div className="p-grid p-fluid">
                    <div className="p-col-12">
                        {this.state.errors.name ? this.state.errors.name : ""}
                    </div>
                </div>
            </div>
        )
    }

    handleSubmit = (e) => {
        this.validateSubmit(() => {
            // If validation is valid, call API and get response
            if (this.isSubmitValid()) {
                let response = this.getResponseAfterSubmit();
                this.handleAfterSubmit(response);
            }
            // Otherwise, handle fail validation
            else {
                this.handleFailValidation();
            }
        })
    }

    /**
     * Handle fail validation
     */
    handleFailValidation() {
        this.toast.show({
            severity: 'error', summary: 'Validate Fail',
            content: this.showValidateMessage(),
            life: 1000
        });
    }

    /**
     * Handle event based on submit responses
     * @param response
     */
    handleAfterSubmit(response) {
        response
            .then(res => {
                if (!res) {
                    this.handleFailSubmit();
                } else {
                    this.handleSuccessSubmit();
                }
            })
    }

    /**
     * Handle success response
     */
    handleSuccessSubmit() {
        this.toast.show({
            severity: 'success', summary: 'Submit Success',
            detail: 'Update Success',
            life: 1000
        });

        sleep(500).then(() => {
            this.props.refreshData();
            this.onHide();
        })
    }

    /**
     * Handle fail or error response
     */
    handleFailSubmit() {
        this.toast.show({
            severity: 'error', summary: 'Submit Fail',
            detail: 'Update Fail',
            life: 1000
        });
    }

    /**
     * Retrieve response after submit form
     */
    getResponseAfterSubmit() {
        return this.notificationService.updateEvent(this.state.data, this.state.isMock);
    }

    /**
     * Call on form close
     */
    onHide = () => {
        this.setState({ visible: false, errors: {} });
    }

    /**
     * Render
     * @returns {JSX.Element}
     */
    render() {
        return (
            <Sidebar visible={this.state.visible} style={{ overflowY: "auto", width: "40em" }} position="right" blockScroll={true} baseZIndex={1000000} onHide={this.onHide} >
                <Toast ref={(el) => this.toast = el} />
                <h2>Edit event</h2>
                <div className="p-grid p-fluid">
                    <div className="p-col-12">
                        <label>* Name</label>
                        <InputText value={this.state.data.name} placeholder="Enter name"
                            onChange={(e) => this.setState({ data: { ...this.state.data, name: e.target.value } })} />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.name}</div>
                    </div>
                    <div className="p-col-12">
                        <label>Event Type</label>
                        <InputText value={this.state.data.eventType} disabled
                            onChange={(e) => this.setState({ data: { ...this.state.data, eventType: e.target.value } })} />
                    </div>
                    <div className="p-col-12">
                        <label>Occur At</label>
                        <InputText value={moment(this.state.data.occurAt).format('HH:mm-A-ddd-DD/MMM/YYYY')} disabled
                            onChange={(e) => this.setState({ data: { ...this.state.data, occurAt: e.target.value } })} />
                    </div>
                </div>
                <div className="p-grid">
                    <div className="p-col-12 p-r p-margin-top-30 p-line-top">
                        <Button label="Submit" icon="pi pi-check" onClick={this.handleSubmit} />
                        <Button label="Cancel" icon="pi-md-close" className="p-button-secondary" onClick={this.onHide} />
                    </div>
                </div>
            </Sidebar>
        );
    }
}

