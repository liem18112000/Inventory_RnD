import React, { Component } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Sidebar } from 'primereact/sidebar';
import { InputTextarea } from 'primereact/inputtextarea';
import { IngredientService } from '../../service/IngredientService';
import { Dropdown } from 'primereact/dropdown';
import { Toast } from 'primereact/toast';
import { sleep } from '../../core/utility/ComponentUtility';

/**
 * Ingredient form for save or update ingredient form information
 */
export class IngredientTypeForm extends Component {

    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            data: {
                id: null,
                parentId: null,
                name: '',
                tenantId: '',
                description: '',
                unit: '',
                unitType: '',
                code: ''
            },
            isMock: false,
            visible: false,
            errors: {},
            editTitle: 'Edit Ingredient Detail',
            createTitle: 'New Ingredient Detail'
        }
        this.ingredientService = new IngredientService();
        console.log(props);
    }

    /**
     * Function is called after component is required
     */
    componentDidMount() {
    }

    /**
     * Form action is activated when the form need to save or update ingredient detail information
     * @param id        Ingredient Type ID
     * @param parentId  ID of corresponding Ingredient Category
     * @param isSave    True if save otherwise false
     */
    action = (id, parentId, isSave = true) => {
        this.ingredientService.getUnitTypes(this.state.isMock).then(ut => this.setState({
            unitTypes: ut
        }));
        if (!isSave && id != null) {
            this.setUpdateInformation(id, parentId);
        } else {
            this.setSaveInformation(parentId);
        }
    }

    /**
     * Set up information to state
     */
    setSaveInformation(parentId) {
        this.setState({
            data: {
                id: null,
                parentId: parentId,
                name: '',
                tenantId: '',
                description: '',
                unit: '',
                unitType: '',
                code: ''
            },
            id: null,
            // unitType: null,
            visible: true,
            formHeader: this.state.createTitle
        })
    }

    /**
     * Get updated ingredient category and set to update information state
     * @param id    Ingredient Category id
     */
    setUpdateInformation(id, parentId) {
        this.ingredientService.getByID(id, this.state.isMock).then(data => {
            this.setState({
                data: {
                    id: data ? data.id : null,
                    parentId: parentId,
                    name: data ? data.name : '',
                    tenantId: data ? data.tenantId : '',
                    description: data ? data.description : '',
                    unit: data ? data.unit : '',
                    unitType: data ? data.unitType : '',
                    code: data ? data.code : ''
                },
                id: data ? data.id : null,
                // unitType: data ? data.unitType : null,
                visible: true,
                formHeader: this.state.editTitle
            }, () => {
                if (data.unitType) {
                    this.ingredientService.getUnit(data.unitType, false).then(u => this.setState({
                        units: u
                    }))
                }
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
        return !(this.state.errors.name || this.state.errors.code || this.state.errors.unitType || this.state.errors.unit);
    }

    /**
     * Validating all field in the form (required fields)
     * @param callback The callback on processing the next transaction
     */
    validateSubmit(callback) {
        this.setState({
            ...this.state,
            errors: {
                name: !this.requireField(this.state.data.name) ? "Ingredient detail name is required" : null,
                code: !this.requireField(this.state.data.code) ? "Ingredient detail code is required" : null,
                unitType: !this.requireField(this.state.data.unitType) ? "Ingredient detail unit type is required" : null,
                unit: !this.requireField(this.state.data.unit) ? "Ingredient detail unit is required" : null,
            }
        }, callback);
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
                        {this.state.errors.code ? this.state.errors.code : ""}
                    </div>
                    <div className="p-col-12">
                        {this.state.errors.unitType ? this.state.errors.unitType : ""}
                    </div>
                    <div className="p-col-12">
                        {this.state.errors.unit ? this.state.errors.unit : ""}
                    </div>
                </div>
            </div>
        )
    }

    /**
    * Handler all submit of save or update ingredient form information
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
            return this.ingredientService.updateIngredient(this.state.data, this.state.isMock);
        } else {
            console.log('Save')
            return this.ingredientService.saveIngredient(this.state.data, this.state.isMock);
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
                        <label>* Code</label>
                        <InputText value={this.state.data.code} placeholder="Enter code"
                            onChange={(e) => this.setState({ data: { ...this.state.data, code: e.target.value } })} />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.code}</div>
                    </div>
                    <div className="p-col-12">
                        <label>* Unit Type </label>
                        <Dropdown value={this.state.data.unitType}
                            options={this.state.unitTypes}
                            onChange={(e) => {
                                this.setState({
                                    data: { ...this.state.data, unitType: e.target.value }
                                },
                                    () => {
                                        this.ingredientService.getUnit(this.state.data.unitType, false).then(u => this.setState({
                                            units: u
                                        }));
                                    }
                                )
                            }} />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.unitType}</div>
                    </div>
                    <div className="p-col-12">
                        <label>* Unit </label>
                        <Dropdown value={this.state.data.unit}
                            options={this.state.units} onChange={(e) => this.setState({ data: { ...this.state.data, unit: e.target.value } })} />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.unit}</div>
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