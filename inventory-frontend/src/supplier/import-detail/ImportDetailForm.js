import React, { Component } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Sidebar } from 'primereact/sidebar';
import { InputTextarea } from 'primereact/inputtextarea';
import { sleep } from '../../core/utility/ComponentUtility';
import { Toast } from 'primereact/toast';
import { SupplierService } from '../../service/SupplierService';
import { IngredientService } from '../../service/IngredientService';
import { Dropdown } from 'primereact/dropdown';
import {handleExceptionWithSentryAndSendFeedback} from "../../core/utility/integrations/SentryExceptionResolver";

/**
 * Import detail form for save or update import detail form information
 */
export class ImportDetailForm extends Component {
    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            data: {
                id: null,
                tenantId: null,
                name: '',
                description: '',
                importId: null,
                ingredientId: null,
                quantity: 1,
            },
            isMock: false,
            visible: false,
            errors: {},
            editTitle: 'Edit Supplier Import',
            createTitle: 'New Supplier Import',
            ingredientList: []
        }
        this.supplierService = new SupplierService();
        this.ingredientService = new IngredientService();
    }

    /**
     * Function is called after component is required
     */
    componentDidMount() {
        this.ingredientService.getInventoryIngredientDetail(this.state.isMock).then(data => {
            this.setState({
                ...this.state, ingredientList: data
            })
        })
    }

    /**
     * Form action is activated when the form need to save or update supplier import information
     * @param id        Supplier Import detail ID
     * @param importId  ID of corresponding Supplier Child
     * @param isSave    True if save otherwise false
     */
    action = (id, importId, isSave = true) => {
        if (!isSave && id != null) {
            this.setUpdateInformation(id, importId);
        } else {
            this.setSaveInformation(importId);
        }
    }

    /**
     * Set up information to state
     * @param importId  ID of corresponding Supplier Import
     */
    setSaveInformation(importId) {
        this.setState({
            data: {
                id: null,
                tenantId: null,
                name: '',
                description: '',
                importId: importId,
                quantity: 1,
                ingredientId: null
            },
            id: null,
            visible: true,
            formHeader: this.state.createTitle
        })
    }

    /**
     * Get updated supplier group and set to update information state
     * @param importId  ID of corresponding Supplier Import
     * @param id    Supplier import detail ID
     */
    setUpdateInformation(id, importId) {
        this.supplierService.getImportDetailByID(id, this.state.isMock).then(data => {
            this.setState({
                data: {
                    id: data ? data.id : null,
                    tenantId: data ? data.tenantId : '',
                    name: data ? data.name : '',
                    description: data ? data.description : '',
                    importId: importId,
                    quantity: data ? data.quantity : '',
                    ingredientId: data ? data.ingredient.id : '',
                },
                id: data ? data.id : null,
                visible: true,
                formHeader: this.state.editTitle
            })
        }).catch(e => handleExceptionWithSentryAndSendFeedback(e, "Get import detail failed."));
    }

    /**
     * Check required field
     * @param field 
     * @returns {boolean}
     */
    requireField = (field) => {
        return field && field.length > 0;
    }

    requireNumberField = (field) => {
        return field !== null;
    }

    /**
     * Check the submit validation is valid
     * @returns {boolean}
     */
    isSubmitValid = () => {
        return !(this.state.errors.name || this.state.errors.ingredientId);
    }

    /**
     * Validating all field in the form (required fields)
     * @param callback The callback on processing the next transaction
     */
    validateSubmit = (callback) => {
        console.log(this.state.data);
        this.setState({
            ...this.state,
            errors: {
                name: !this.requireField(this.state.data.name) ? "Supplier import detail name is required" : null,
                ingredientId: !this.requireNumberField(this.state.data.ingredientId)
                    ? "Supplier import detail ingredient is required" : null,
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
                    <div className="p-col-12">
                        {this.state.errors.ingredientId ? this.state.errors.ingredientId : ""}
                    </div>
                </div>
            </div>
        )
    }

    /**
     * Handler all submit of save or update supplier import form information
     * @param e
     */
    handleSubmit = (e) => {
        console.log("handleSubmit");
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
            detail: this.state.formHeader === this.state.editTitle ? 'Update Success' : "Create Success",
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
            detail: this.state.formHeader === this.state.editTitle ? 'Update Fail' : "Create Fail",
            life: 1000
        });
    }

    /**
     * Retrieve response after submit form
     * @returns {Promise<AxiosResponse<*>|void>|Promise<{code: string, tenantId: number, name: string, accessAt: string, description: string, updateAt: string, id: number, createAt: string, activated: boolean}>}
     */
    getResponseAfterSubmit() {
        if (this.state.formHeader === this.state.editTitle) {
            console.log('Edit')
            return this.supplierService.updateSupplierImportDetail(this.state.data, this.state.isMock);
        } else {
            console.log('Save')
            return this.supplierService.saveSupplierImportDetail(this.state.data, this.state.isMock);
        }
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
                <h2>{this.state.formHeader}</h2>
                <div className="p-grid p-fluid">
                    <div className="p-col-12">
                        <label>* Ingredient</label>
                        <Dropdown value={this.state.data.ingredientId}
                            itemTemplate={item => item.label}
                            options={this.state.ingredientList}
                            onChange={(e) => {
                                this.setState({
                                    ...this.state, data: {
                                        ...this.state.data,
                                        ingredientId: e.value
                                    }
                                })
                            }}
                        />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.ingredientId}</div>
                    </div>
                    <div className="p-col-12">
                        <label>* Name</label>
                        <InputText value={this.state.data.name} placeholder="Enter name"
                            onChange={(e) => this.setState({ data: { ...this.state.data, name: e.target.value } })} />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.name}</div>
                    </div>
                    <div className="p-col-12">
                        <label>* Quantity</label>
                        <InputText
                            value={this.state.data.quantity}
                            placeholder="Enter quantity"
                            type="number"
                            min="1"
                            max="1000"
                            onChange={(e) => this.setState({ data: { ...this.state.data, quantity: e.target.value } })} />
                    </div>
                    <div className="p-col-12">
                        <label>Description</label>
                        <InputTextarea rows={5} value={this.state.data.description} placeholder="Enter description"
                            onChange={(e) => this.setState({ data: { ...this.state.data, description: e.target.value } })} />
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

