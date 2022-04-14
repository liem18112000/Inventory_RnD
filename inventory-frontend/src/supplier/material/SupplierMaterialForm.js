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
 * Recipe form for save or update recipe form information
 */
export class SupplierMaterialForm extends Component {
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
                supplierId: null,
                ingredientId: null,
                minimumQuantity: 1,
                maximumQuantity: 1,
            },
            isMock: false,
            visible: false,
            errors: {},
            editTitle: 'Edit Supplier Material',
            createTitle: 'New Supplier Material',
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
     * Form action is activated when the form need to save or update recipe child information
     * @param id        Recipe Detail ID
     * @param recipeId  ID of corresponding Recipe Child
     * @param isSave    True if save otherwise false
     */
    action = (id, supplierId, isSave = true) => {
        if (!isSave && id != null) {
            this.setUpdateInformation(id, supplierId);
        } else {
            this.setSaveInformation(supplierId);
        }
    }

    /**
     * Set up information to state
     * @param supplierId  ID of corresponding Recipe Child
     */
    setSaveInformation(supplierId) {
        this.setState({
            data: {
                id: null,
                tenantId: null,
                name: '',
                description: '',
                code: '',
                supplierId: supplierId,
                ingredientId: null,
                minimumQuantity: 1,
                maximumQuantity: 1,
            },
            id: null,
            visible: true,
            formHeader: this.state.createTitle
        })
    }

    /**
     * Get updated recipe group and set to update information state
     * @param supplierId  ID of corresponding Recipe Child
     * @param id    Recipe detail ID
     */
    setUpdateInformation(id, supplierId) {
        this.supplierService.getMaterialById(id, this.state.isMock).then(data => {
            this.setState({
                data: {
                    id: data ? data.id : null,
                    tenantId: data ? data.tenantId : '',
                    name: data ? data.name : '',
                    description: data ? data.description : '',
                    code: data ? data.code : '',
                    supplierId: supplierId,
                    ingredientId: data ? data.ingredient.id : '',
                    minimumQuantity: data ? data.minimumQuantity : '',
                    maximumQuantity: data ? data.maximumQuantity : '',
                },
                id: data ? data.id : null,
                visible: true,
                formHeader: this.state.editTitle
            })
        }).catch(e => handleExceptionWithSentryAndSendFeedback(e, "Get material failed."));
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
        return field !== null && field !== "";
    }
    checkMinMax = (min, max) => {
        return min <= max
    }
    /**
     * Check the submit validation is valid
     * @returns {boolean}
     */
    isSubmitValid = () => {
        return !(this.state.errors.name || this.state.errors.code || this.state.errors.ingredientId);
    }

    /**
     * Validating all field in the form (required fields)
     * @param callback The callback on processing the next transaction
     */
    validateSubmit = (callback) => {
        this.setState({
            ...this.state,
            errors: {
                name: !this.requireField(this.state.data.name) ? "Supplier material name is required" : null,
                ingredientId: !this.requireNumberField(this.state.data.ingredientId) ? "Supplier material ingredient is required" : null,
                // code: !this.requireNumberField(this.state.data.code) ? "Supplier material code is required" : null,
                min: !this.requireNumberField(this.state.data.minimumQuantity) ? "Supplier material minimum quantity is required" : null,
                max: !this.requireNumberField(this.state.data.maximumQuantity) ? "Supplier material maximum quantity is required" : null,
                minimumQuantity: !this.checkMinMax(this.state.data.minimumQuantity, this.state.data.maximumQuantity) ? "Min must be less than or equal max" : null,
                maximumQuantity: !this.checkMinMax(this.state.data.minimumQuantity, this.state.data.maximumQuantity) ? "Max must be more than or equal min" : null,
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
                    <div className="p-col-12">
                        {this.state.errors.code ? this.state.errors.code : ""}
                    </div>
                    <div className="p-col-12">
                        {this.state.errors.min ? this.state.errors.min : ""}
                    </div>
                    <div className="p-col-12">
                        {this.state.errors.max ? this.state.errors.max : ""}
                    </div>
                    <div className="p-col-12">
                        {this.state.errors.minimumQuantity ? this.state.errors.minimumQuantity : ""}
                    </div>
                    <div className="p-col-12">
                        {this.state.errors.maximumQuantity ? this.state.errors.maximumQuantity : ""}
                    </div>
                </div>
            </div>
        )
    }

    /**
     * Handler all submit of save or update recipe form information
     * @param e
     */
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
            return this.supplierService.updateSupplierMaterial(this.state.data, this.state.isMock);
        } else {
            console.log('Save')
            return this.supplierService.saveSupplierMaterial(this.state.data, this.state.isMock);
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
                        <label>* Name</label>
                        <InputText value={this.state.data.name} placeholder="Enter name"
                            onChange={(e) => this.setState({ data: { ...this.state.data, name: e.target.value } })} />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.name}</div>
                    </div>
                    <div className="p-col-12">
                        <label>* Minimum Quatity</label>
                        <InputText
                            value={this.state.data.minimumQuantity}
                            placeholder="Enter min quantity"
                            type="number"
                            min="1"
                            max="1000"
                            onChange={(e) => this.setState({ data: { ...this.state.data, minimumQuantity: e.target.value } })} />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.minimumQuantity}</div>
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.min}</div>
                    </div>
                    <div className="p-col-12">
                        <label>* Maximum Quatity</label>
                        <InputText
                            value={this.state.data.maximumQuantity}
                            placeholder="Enter max quantity"
                            type="number"
                            min="1"
                            max="1000"
                            onChange={(e) => this.setState({ data: { ...this.state.data, maximumQuantity: e.target.value } })} />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.maximumQuantity}</div>
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.max}</div>
                    </div>
                    <div className="p-col-12">
                        <label>* Ingredient Name </label>
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