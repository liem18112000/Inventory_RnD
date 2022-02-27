import React, { Component } from 'react';
import { ProductService } from '../../service/ProductService';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import '../../assets/styles/CardDemo.css';
import { getMediaSource } from "../../service/MediaService";

export class SuggestTaxonForm extends Component {

    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            isMock: false,
            errors: {},
            displayResponsive: false,
            position: 'center'
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
                <Button label="Cancel" icon="pi pi-times" onClick={() => this.onHide(name)} className="p-button-text" />
                <Button label="Save" icon="pi pi-check" onClick={() => this.onHide(name)} autoFocus />
            </div>
        );
    }


    /**
     * Render
     * @returns {JSX.Element}
     */
    render() {
        const data = this.state.data;

        return (
            <Dialog header="Taxon Detail" visible={this.state.displayResponsive}
                onHide={() => this.onHide('displayResponsive')}
                breakpoints={{ '768px': '55%' }}
                style={{ width: '25vw', overflowY: 'hidden', textAlign: 'center' }}
                footer={this.renderFooter('displayResponsive')}
            >
                {data && <div className="taxon-card">
                    <img src={this.state.data.image} style={{ width: '90%', align: 'center' }}
                        onError={(e) => e.target.src = getMediaSource()} alt="" />
                    <div className="">

                        <div className="taxon-name">{data.recipe.name}</div>
                    </div>
                    {/* <DataTable value={data.details}>
                        <Column field="name" header="Name"></Column>
                        <Column field="quantity" header="Quantity"></Column>
                    </DataTable> */}

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
        );
    }
}

