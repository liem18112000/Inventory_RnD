import React, { Component } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Sidebar } from 'primereact/sidebar';
import { IngredientService } from '../../service/IngredientService';
import { Toast } from 'primereact/toast';
import { sleep } from '../../core/utility/ComponentUtility';

/**
 * Ingredient form for save or update ingredient form information
 */
export class IngredientTypeConfig extends Component {

    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            id: null,
            data: {
                minimumQuantity: 1,
                maximumQuantity: 1,
            },
            isMock: false,
            visible: false,
            errors: {},
        }
        this.ingredientService = new IngredientService();
    }

    /**
     * Function is called after component is required
     */
    componentDidMount() {
    }

    action = (ingredientId) => {
        console.log(ingredientId)
        this.ingredientService.getIngredientConfig(ingredientId, this.state.isMock)
            .then(res => this.setState({
                ...this.state,
                visible: true,
                id: res?.id,
                data: {
                    minimumQuantity: res?.minimumQuantity,
                    maximumQuantity: res?.maximumQuantity
                },
            }))
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
        return !(this.state.errors.min || this.state.errors.max || this.state.errors.minimumQuantity || this.state.errors.maximumQuantity);
    }

    /**
     * Validating all field in the form (required fields)
     * @param callback The callback on processing the next transaction
     */
    validateSubmit(callback) {
        this.setState({
            ...this.state,
            errors: {
                min: !this.requireNumberField(this.state.data.minimumQuantity) ? "Supplier material minimum quantity is required" : null,
                max: !this.requireNumberField(this.state.data.maximumQuantity) ? "Supplier material maximum quantity is required" : null,
                minimumQuantity: !this.checkMinMax(this.state.data.minimumQuantity, this.state.data.maximumQuantity) ? "Min must be less than or equal max" : null,
                maximumQuantity: !this.checkMinMax(this.state.data.minimumQuantity, this.state.data.maximumQuantity) ? "Max must be more than or equal min" : null,
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
            detail: 'Config Success',
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
            detail: 'Save Fail',
            life: 1000
        });
    }

    /**
     * Retrieve response after submit form
     */
    getResponseAfterSubmit() {
        console.log(this.state)
        return this.ingredientService.updateIngredientConfig(this.state.id, this.state.data, this.state.isMock);
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
                <h2>Config Ingredient Detail</h2>
                <div className="p-grid p-fluid">
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
                </div>
                <div className="p-grid" style={{ marginTop: 20 }}>
                    <div className="p-col-12 p-r p-margin-top-30 p-line-top">
                        <Button label="Submit" icon="pi pi-check" onClick={this.handleSubmit} />
                        <Button label="Cancel" icon="pi-md-close" className="p-button-secondary" onClick={this.onHide} />
                    </div>
                </div>
            </Sidebar>
        );
    }
}