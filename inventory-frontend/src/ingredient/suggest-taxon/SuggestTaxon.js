import React, { Component } from 'react';
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Button } from 'primereact/button';
import { IngredientService } from '../../service/IngredientService'
import '../../assets/styles/DataViewDemo.css';
import { SuggestTaxonDetail } from './SuggestTaxonDetail';
import { getMediaSource } from '../../service/MediaService';
import {Toast} from "primereact/toast";

export class SuggestTaxon extends Component {

    constructor(props) {
        super(props);
        this.state = {
            products: null,
            data: [],
            selectedImg: '',
            layout: 'grid',
            sortKey: null,
            sortOrder: null,
            sortField: null,
            isMock: false,
            loading: true,
        };

        this.ingredientService = new IngredientService();
        this.itemTemplate = this.itemTemplate.bind(this);
        this.onSortChange = this.onSortChange.bind(this);
    }

    componentDidMount() {
        this.setState({ loading: true });
        this.suggestTaxon()
    }

    handleSuggestTaxonFailed() {
        this.toast.show({
            severity: "warn",
            summary: 'Load Fail',
            detail:'Service get data failed',
            life: 1000
        });
    }

    suggestTaxon = () => {
        const { isMock } = this.state;
        this.ingredientService
            .getSuggestTaxon(isMock)
            .then(data =>
            {
                if (!data || data.length <= 0) {
                    this.handleSuggestTaxonFailed();
                    this.setState({
                        ...this.state,
                        loading: false
                    })
                } else {
                    this.setState({
                        ...this.state,
                        loading: false,
                        data: data.map(elem => {
                            return {
                                ...elem,
                                image: !elem.recipe.image ? getMediaSource() : elem.recipe.image
                            }
                        })
                    })
                }
            }
            ).then(() => console.log(this.state.data));
    }

    onSortChange(event) {
        const value = event.value;

        if (value.indexOf('!') === 0) {
            this.setState({
                sortOrder: -1,
                sortField: value.substring(1, value.length),
                sortKey: value
            });
        }
        else {
            this.setState({
                sortOrder: 1,
                sortField: value,
                sortKey: value
            });
        }
    }

    renderListItem(data, form) {
        return (
            <div className="p-col-12">
                <div className="product-list-item">
                    <img src={data.image}
                        onError={(e) => e.target.src = getMediaSource()}
                        alt={data.name} />
                    <div className="product-list-detail">
                        <div className="product-name">{data.recipe.name}</div>
                    </div>
                    <div className="product-list-action">
                        <span className="product-price">Quantity: {data.taxonQuantity}</span>
                        <Button label="Detail" onClick={(e) => { form.action(data) }}></Button>
                    </div>
                </div>
            </div>
        );
    }

    renderGridItem(data, form) {
        return (
            <div className="p-col-12 p-xxl-3 p-lg-4 p-md-6 p-sm-12">
                <div className="product-grid-item card"
                     style={{ height: 'auto', minHeight: "98%", position: "relative", paddingBottom: "2%" }}>
                    <img src={data.image}
                        onError={(e) => e.target.src = getMediaSource()}
                        alt={data.name} />
                    <div className="product-grid-item-content" style={{marginBottom: "50px"}}>
                        <div className="product-name">{data.recipe.name}</div>
                    </div>
                    <div className="product-grid-item-bottom"
                         style={{position: "absolute", bottom: "2%", display: "flex", justifyContent: "space-between", width: "100%"}}>
                        <span className="product-price">Quantity: {data.taxonQuantity}</span>
                        <Button label="Detail" onClick={(e) => { form.action(data) }}></Button>
                    </div>
                </div>
            </div >
        );
    }

    itemTemplate(product, layout) {
        if (!product) {
            return;
        }

        // return this.renderGridItem(product);
        if (layout === 'list')
            return this.renderListItem(product, this.form);
        else if (layout === 'grid')
            return this.renderGridItem(product, this.form);
    }

    renderHeader() {
        return (
            <div className="p-grid p-nogutter">
                <div className="p-col-6" style={{ textAlign: 'left' }}>
                    {/* <Dropdown options={this.sortOptions} value={this.state.sortKey} optionLabel="label" placeholder="Sort By Price" onChange={this.onSortChange} /> */}
                </div>
                <div className="p-col-6" style={{ textAlign: 'right' }}>
                    <DataViewLayoutOptions layout={this.state.layout} onChange={(e) => this.setState({ layout: e.value })} />
                </div>
            </div>
        );
    }

    render() {
        const header = this.renderHeader();

        return (
            <div className="dataview-demo">
                <Toast ref={(el) => this.toast = el} />
                <div className="card">
                    <SuggestTaxonDetail bell={this.props.bell} ref={el => this.form = el} refreshData={() => this.suggestTaxon()} />
                    <DataView value={this.state.data} layout={this.state.layout} header={header} loading={this.state.loading}
                        itemTemplate={this.itemTemplate} paginator rows={12}
                        sortOrder={this.state.sortOrder} sortField={this.state.sortField} />
                </div>
            </div>
        );
    }
}