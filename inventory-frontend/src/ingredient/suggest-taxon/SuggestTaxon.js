import React, { Component } from 'react';
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Button } from 'primereact/button';
import { ProductService } from '../../service/ProductService';
import { IngredientService } from '../../service/IngredientService'
import '../../assets/styles/DataViewDemo.css';
import { SuggestTaxonForm } from './SuggestTaxonDetail';
import { handleGetPage } from "../../core/handlers/ApiLoadContentHandler";
import { PagingDataModelMapper } from "../../core/models/mapper/ModelMapper";
import { getMediaSource } from '../../service/MediaService';

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
        };

        this.productService = new ProductService();
        this.ingredientService = new IngredientService();
        this.itemTemplate = this.itemTemplate.bind(this);
        this.onSortChange = this.onSortChange.bind(this);
    }

    componentDidMount() {
        // this.productService.getProducts().then(data => this.setState({ products: data }));
        this.suggestTaxon()
    }

    suggestTaxon = () => {
        const { isMock } = this.state;
        this.ingredientService
            .getSuggestTaxon(isMock)
            .then(data => this.setState({ ...this.state, data: data }));
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
                    <img src={getMediaSource()}
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
            <div className="p-col-12 p-md-3">
                <div className="product-grid-item card">
                    <div className="product-grid-item-content">
                        <img src={getMediaSource()}
                            onError={(e) => e.target.src = getMediaSource()}
                            alt={data.name} />
                        <div className="product-name">{data.recipe.name}</div>
                    </div>
                    <div className="product-grid-item-bottom">
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
                <div className="card">
                    <SuggestTaxonForm ref={el => this.form = el} />
                    <DataView value={this.state.data} layout={this.state.layout} header={header}
                        itemTemplate={this.itemTemplate} paginator rows={12}
                        sortOrder={this.state.sortOrder} sortField={this.state.sortField} />
                </div>
            </div>
        );
    }
}