import React, { Component } from 'react';
import DomainService from '../../service/DomainService';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import '../../assets/styles/CardDemo.css';
import { getMediaSource } from "../../service/MediaService";
import { confirmDialog } from 'primereact/confirmdialog';
import { Toast } from 'primereact/toast';
import { InputNumber } from 'primereact/inputnumber';
import { sleep } from "../../core/utility/ComponentUtility";

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
        this.domainService = new DomainService();
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
            this.toast.show({
                severity: 'error', summary: 'Invalid input', detail: 'Confirm quantity ' +
                    'is either lower than ZERO or larger then max quantity', life: 1000
            });
        } else {
            // Confirm Suggestion
            this.domainService.confirmTaxon(this.state.data, this.state.confirmQuantity, this.state.isMock).then((data) => {
                if (data.confirmSuggestion) {
                    this.toast.show({
                        severity: 'success', summary: 'Confirmed',
                        detail: `You have confirm ${this.state.confirmQuantity}`, life: 1000
                    });
                    sleep().then(() => {
                        this.onHide("displayResponsive")
                        if (data.lowStockAlert) {
                            sleep().then(() => this.toast.show({
                                severity: 'warn', summary: 'Lowstock alert',
                                detail: `This taxon is low-stocked`, life: 1000
                            }));
                        }
                        this.setState({
                            ...this.state, confirmQuantity: 1
                        })
                    }).then(this.props.refreshData)
                } else {
                    this.toast.show({
                        severity: 'error', summary: 'Confirm failed',
                        detail: `There is an error in confirmation`, life: 1000
                    });
                }
            });

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
            reject: () => this.toast.show({ severity: 'info', summary: 'Cancel delete', detail: 'You have cancel the confirmation', life: 1000 })
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
                    footer={this.renderFooter('displayResponsive')}
                >
                    {data && <div className="taxon-card">
                        <img src={this.state.data.image} alt={"Taxon"} style={{ width: '90%', align: 'center' }}
                            onError={(e) => e.target.src = getMediaSource()} />
                        <div className="">

                            <div className="taxon-name">{data.recipe.name}</div>
                        </div>

                        {data.details.map((item, index) => (
                            <div key={"div_" + index} className="taxon-detail">
                                <span className="detail">Recipe Detail</span>
                                <ul>
                                    <li key={"recipeName_" + index}><span className="detail">Recipe name: </span>{item.name}</li>
                                    <li key={"recipeQuantity_" + index}><span className="detail">Recipe quantity: </span>
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

