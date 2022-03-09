import React, { Component } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Sidebar } from 'primereact/sidebar';
import { InputTextarea } from 'primereact/inputtextarea';
import { RecipeService } from '../../service/RecipeService';
import { Dropdown } from 'primereact/dropdown';
import { IngredientService } from '../../service/IngredientService';
import { sleep } from '../../core/utility/ComponentUtility';
import { Toast } from 'primereact/toast';

/**
 * Recipe form for save or update recipe form information
 */
export class RecipeDetailForm extends Component {
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
                quantity: 1,
                code: '',
                ingredientId: null,
                recipeId: null
            },
            isMock: false,
            visible: false,
            errors: {},
            editTitle: 'Edit Recipe Detail',
            createTitle: 'New Recipe Detail',
            ingredientList: []
        }
        this.recipeService = new RecipeService();
        this.ingredientService = new IngredientService();
    }

    /**
     * Function is called after component is required
     */
    componentDidMount() {
        this.ingredientService.getInventoryIngredientDetail(this.state.isMock).then(data => {
            this.setState({
                ingredientList: data
            })
        })
    }

    /**
     * Form action is activated when the form need to save or update recipe child information
     * @param id        Recipe Detail ID
     * @param recipeId  ID of corresponding Recipe Child
     * @param isSave    True if save otherwise false
     */
    action = (id, recipeId, isSave = true) => {
        if (!isSave && id != null) {
            this.setUpdateInformation(id, recipeId);
        } else {
            this.setSaveInformation(recipeId);
        }
    }

    /**
     * Set up information to state
     * @param recipeId  ID of corresponding Recipe Child
     */
    setSaveInformation(recipeId) {
        this.setState({
            data: {
                id: null,
                tenantId: null,
                name: '',
                description: '',
                quantity: 1,
                code: '',
                ingredientId: null,
                recipeId: recipeId,
            },
            id: null,
            visible: true,
            formHeader: this.state.createTitle
        })
    }

    /**
     * Get updated recipe group and set to update information state
     * @param recipeId  ID of corresponding Recipe Child
     * @param id    Recipe detail ID
     */
    setUpdateInformation(id, recipeId) {
        this.recipeService.getDetailByID(id, this.state.isMock).then(data => {
            this.setState({
                data: {
                    id: data ? data.id : null,
                    tenantId: data ? data.tenantId : '',
                    name: data ? data.name : '',
                    description: data ? data.description : '',
                    quantity: data ? data.quantity : '',
                    code: data ? data.code : '',
                    ingredientId: data ? data.ingredient.id : '',
                    recipeId: recipeId,
                },
                id: data ? data.id : null,
                visible: true,
                formHeader: this.state.editTitle
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

    requireNumberField = (field) => {
        return field !== null && field !== "";
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
                name: !this.requireField(this.state.data.name) ? "Recipe detail name is required" : null,
                code: !this.requireField(this.state.data.code) ? "Recipe detail code is required" : null,
                quantity: !this.requireNumberField(this.state.data.quantity) ? "Recipe detail quantity is required" : null,
                ingredientId: !this.state.data.ingredientId ? "Ingredient is required" : null,
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
                        {this.state.errors.code ? this.state.errors.code : ""}
                    </div>
                    <div className="p-col-12">
                        {this.state.errors.quantity ? this.state.errors.quantity : ""}
                    </div>
                    <div className="p-col-12">
                        {this.state.errors.ingredientId ? this.state.errors.ingredientId : ""}
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
            return this.recipeService.updateDetail(this.state.data, this.state.isMock);
        } else {
            console.log('Save')
            return this.recipeService.saveDetail(this.state.data, this.state.isMock);
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
                        <label>* Quantity</label>
                        <InputText
                            value={this.state.data.quantity}
                            placeholder="Enter quantity"
                            type="number"
                            min="1"
                            max="1000"
                            onChange={(e) => this.setState({ data: { ...this.state.data, quantity: e.target.value } })} />
                        <div className="p-form-error" style={{ color: "red" }}>{this.state.errors.quantity}</div>
                    </div>
                    <div className="p-col-12">
                        <label>* Ingredient </label>
                        <Dropdown value={this.state.data.ingredientId}
                            placeholder="Ingredient"
                            options={this.state.ingredientList}
                            onChange={(e) => {
                                console.log(e.target);
                                this.setState({ data: { ...this.state.data, ingredientId: e.target.value } })
                            }} />
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