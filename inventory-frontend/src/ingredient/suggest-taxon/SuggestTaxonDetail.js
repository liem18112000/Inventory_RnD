import React, { Component } from 'react';
import { ProductService } from '../../service/ProductService';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import '../../assets/styles/CardDemo.css';
import { getMediaSource } from "../../service/MediaService";
import { confirmDialog } from 'primereact/confirmdialog';
import { Toast } from 'primereact/toast';
import { InputNumber } from 'primereact/inputnumber';
import {sleep} from "../../core/utility/ComponentUtility";

export class SuggestTaxonDetail extends Component {

    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            confirmQuantity: 1,
            isMock: false,
            errors: {},
            displayResponsive: false,
            position: 'center',
        }
        this.productService = new ProductService();
        this.onClick = this.onClick.bind(this);
        this.onHide = this.onHide.bind(this);
    }

    /**
     * Function is called after component is required
     */
    componentDidMount() {

    }

    action = (data) => {
        this.setViewInformation(data);
    }

    setViewInformation(data) {
        this.setState({
            data: data,
            displayResponsive: true,
        })
    }

    onClick(name, position) {
        let state = {
            [`${name}`]: true
        };

        if (position) {
            state = {
                ...state,
                position
            }
        }

        this.setState(state);
    }

    onHide(name) {
        this.setState({
            [`${name}`]: false
        });
    }

    renderFooter(name) {
        return (
            <div>
                <Button label="Cancel" icon="pi pi-times"
                    onClick={() => this.onHide(name)} className="p-button-text" />
                <Button label="Save" icon="pi pi-check"
                    onClick={() => this.confirmDelete(name)} autoFocus />
            </div>
        );
    }

    checkMinMax = (input) => {
        return !(input < 1 || input > this.state.data.taxonQuantity);
    }

    confirmSuggestion() {
        if (!this.checkMinMax(this.state.confirmQuantity)) {
            this.toast.show({ severity: 'error', summary: 'Invalid input', detail: 'Confirm quantity ' +
                    'is either lower than ZERO or larger then max quantity', life: 1000 });
        } else {
            // Confirm Suggestion
            this.toast.show({ severity: 'success', summary: 'Confirmed',
                detail: `You have confirm ${this.state.confirmQuantity}`, life: 1000 });
            sleep(500).then(() => {
                this.onHide("displayResponsive")
                this.setState({
                    ...this.state, confirmQuantity: 1
                })
            })
        }

    }

    confirmDelete() {
        confirmDialog({
            message:
                <InputNumber
                    inputId="integeronly"
                    style={{ width: '15vw' }}
                    placeholder="Enter the number"
                    value={this.state.confirmQuantity}
                    showButtons
                    onValueChange={(e) => this.setState({ ...this.state, confirmQuantity: e.value })}
                />,
            header: 'Confirm the suggestion',
            acceptClassName: 'p-button-primary',
            accept: () => this.confirmSuggestion(),
            reject: () => this.toast.show({ severity: 'info', summary: 'Cancel delete', detail: 'You have cancel delete', life: 1000 })
        });
    }

    /**
     * Render
     * @returns {JSX.Element}
     */
    render() {
        const data = this.state.data;

        return (
            <>
                <Toast ref={(el) => this.toast = el} />
                <Dialog header="Taxon Detail" visible={this.state.displayResponsive}
                    onHide={() => this.onHide('displayResponsive')}
                    breakpoints={{ '768px': '55%' }}
                    style={{ width: '25vw', overflowY: 'hidden', textAlign: 'center' }}
                    onHide={() => this.onHide('displayResponsive')}
                    footer={this.renderFooter('displayResponsive')}
                >
                    {data && <div className="taxon-card">
                        <img src={this.state.data.image} style={{ width: '90%', align: 'center' }}
                            onError={(e) => e.target.src = getMediaSource()} />
                        <div className="">

                            <div className="taxon-name">{data.recipe.name}</div>
                        </div>

                        {data.details.map((item, index) => (
                            <div className="taxon-detail">
                                <span className="detail">Recipe Detail</span>
                                <ul>
                                    <li key="1"><span className="detail">Recipe name: </span>{item.name}</li>
                                    <li key="2"><span className="detail">Recipe quantity: </span>
                                        {item.quantity} {item.ingredient.unit}</li>
                                </ul>
                            </div>
                        ))}

                        <div className="taxon-quantity">
                            <span className=""><span className="detail">Taxon quantity: </span> {data.taxonQuantity}</span>
                        </div>
                        <br></br>
                    </div>}
                </Dialog>
            </>
        );
    }
}

