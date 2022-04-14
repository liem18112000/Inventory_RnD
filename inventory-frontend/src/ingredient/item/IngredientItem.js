import React, { Component } from 'react';
import { classNames } from 'primereact/utils';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { SplitButton } from 'primereact/splitbutton';
import { Button } from "primereact/button";
import { Fieldset } from "primereact/fieldset";

import { IngredientService } from '../../service/IngredientService'

import '../../assets/styles/TableDemo.css'

import { Link, withRouter } from 'react-router-dom'
import moment from 'moment';
import { IngredientItemForm } from './IngredientItemForm';
import { handleGetPage } from "../../core/handlers/ApiLoadContentHandler";
import { Toast } from 'primereact/toast';
import { PagingDataModelMapper } from "../../core/models/mapper/ModelMapper";
import { Calendar } from 'primereact/calendar';

class IngredientItem extends Component {

    constructor(props) {
        super(props);
        this.state = {
            content: [],
            selectedUnit: null,
            selectedUnitType: null,
            filter: {
                name: "",
                code: "",
                description: "",
                expiredAt: "",
            },
            cateId: props.location.state.cateId,
            // --paginator state--
            page: 0,
            rows: 10,
            total: 0,
            sortField: "",
            sortOrder: 0,
            // --paginator state--
            isMock: false,
            isBatch: false,
            loading: false
        };
        this.ingredientService = new IngredientService();
        this.mapper = new PagingDataModelMapper();
    }

    componentDidMount() {
        this.setState({ loading: true });
        this.getPageItems();
    };

    getPageItems = () => {
        const { filter, page, rows, sortField, sortOrder, isMock } = this.state;
        const ingredientId = this.props.match.params.id;
        this.ingredientService
            .getPageItem(ingredientId, filter, page, rows, sortField, sortOrder, isMock)
            .then(data => handleGetPage(data, this.toast))
            .then(data => this.mapper.toModel(data))
            .then(data => this.setState({ ...this.state, ...data }));
    };

    setFilter = (filter) => {
        this.setState({
            ...this.state,
            filter: filter
        })
    }

    resetFilter = () => {
        this.setState({
            ...this.state,
            filter: {
                name: "",
                code: "",
                description: "",
                expiredAt: ""
            }
        }, () => {
            this.setState({ loading: true });
            this.getPageItems();
            this.toast.show({ severity: 'info', summary: 'Clear', detail: 'Clear filter', life: 1000 });
        })
    }

    applyFilter = () => {
        this.setState({ loading: true });
        this.getPageItems();
        this.toast.show({ severity: 'info', summary: 'Search', detail: 'Search data content', life: 1000 });
    }

    descriptionBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span>{rowData.description}</span>
            </React.Fragment>
        );
    }

    importBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span>{rowData.imports.code}</span>
            </React.Fragment>
        );
    }

    actionBodyTemplate(rowData, form) {
        return (
            <React.Fragment>
                <div className="card">
                    <Button
                        icon="pi pi-pencil"
                        label="Edit"
                        onClick={(e) => { form.action(rowData.id, false, false) }} />
                </div>
            </React.Fragment>
        );
    }

    unitBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className={classNames('customer-badge', 'status-' + rowData.status)}>{rowData.unit}</span>
            </React.Fragment>
        );
    }

    unitTypeBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className={classNames('customer-badge', 'status-' + rowData.status)}>{rowData.unitType}</span>
            </React.Fragment>
        );
    }

    codeBodyTemplate(rowData) {
        return (
            <React.Fragment>
                {rowData.code}
            </React.Fragment>
        );
    }

    categoryBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle' }}>{rowData.name}</span>
            </React.Fragment>
        );
    }

    expiredAtBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle' }}>{moment(rowData.expiredAt).format('DD/MMM/YYYY')}</span>
            </React.Fragment>
        );
    }

    quantityBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span>{rowData.quantity}</span>
            </React.Fragment>
        );
    }

    onPage = e => {
        this.setState(
            {
                loading: true,
                page: e.page
            },
            () => {
                this.getPageItems();
            }
        );
    };

    onSort = e => {
        this.setState(
            {
                loading: true,
                sortField: e.sortField,
                sortOrder: e.sortOrder
            },
            () => {
                this.getPageItems();
                this.toast.show({
                    severity: 'info',
                    summary: 'Sort',
                    detail: 'Sort ' + e.sortField + " " + (e.sortOrder === 1 ? "ascending" : "descending"),
                    life: 1000
                });
            }
        );
    };

    onRefresh = () => {
        this.setState({
            ...this.state,
            loading: true
        }, () => {
            this.getPageItems()
            this.toast.show({ severity: 'info', summary: 'Refresh', detail: 'Refresh datatable', life: 1000 });
        })
    }

    onChangePageLength = l => {
        this.setState(
            {
                rows: l,
                page: 0
            },
            () => {
                this.setState({ loading: true });
                this.getPageItems();
                this.toast.show({ severity: 'info', summary: 'Reset page size', detail: 'Page size is set to ' + l, life: 1000 });
            }
        );
    };

    getTablePageReport = (page, rows, total) => {
        if (total > 0) {
            let first = (page * rows) + 1;
            let last = page * rows + rows;
            if (last > total) last = total;
            return `Showing ${first} to ${last} of ${total} entries`;
        }
        return ''
    };

    render() {
        // const header = this.renderHeader();
        let tableLengthOptions = [
            {
                label: '5',
                command: () => {
                    this.onChangePageLength(5);
                }
            },
            {
                label: '10',
                command: () => {
                    this.onChangePageLength(10);
                }
            },
            {
                label: '25',
                command: () => {
                    this.onChangePageLength(25);
                }
            },
            {
                label: '50',
                command: () => {
                    this.onChangePageLength(50);
                }
            },
            {
                label: '100',
                command: () => {
                    this.onChangePageLength(100);
                }
            }
        ];

        let header = (
            <>
                <div className="table-header">
                    <span className="p-input-icon-left">
                        <i className="pi pi-plus" />
                        <Button
                            className="blue-btn"
                            style={{ marginRight: '0.5rem' }}
                            icon="pi pi-plus"
                            iconPos="left"
                            label="New item"
                            onClick={() => this.form.action(null, true, false)}
                        />
                        <Button
                            className="blue-btn"
                            style={{ marginRight: '0.5rem' }}
                            icon="pi pi-plus"
                            iconPos="left"
                            label="New item batch"
                            onClick={() => this.form.action(null, true, true)}
                        />
                        <SplitButton className="table-control-length p-button-constrast" label="Refresh" icon="pi pi-refresh"
                            onClick={this.onRefresh} model={tableLengthOptions}>
                        </SplitButton>
                    </span>
                    <span className="p-input-icon-left" style={{ fontSize: "17px" }}>
                        <Link to={`/ingredient/${this.state.cateId}`}> Back to Ingredient Types</Link>
                    </span>
                </div>
            </>
        )

        return (
            <div className="datatable-doc-demo">
                <Toast ref={(el) => this.toast = el} />
                <IngredientItemForm ref={el => this.form = el}
                    refreshData={() => this.getPageItems()}
                    id={this.props.match.params.id}
                    unitType={this.props.location.state.unitType}
                    unit={this.props.location.state.unit}
                />
                <Fieldset legend="Ingredient Item" toggleable>
                    <div className="p-grid p-fluid">
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <InputText
                                placeholder="Code"
                                value={this.state.filter.code}
                                onChange={(e) => this.setFilter({ ...this.state.filter, code: e.target.value })}
                            />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <InputText
                                placeholder="Name"
                                value={this.state.filter.name}
                                onChange={(e) => this.setFilter({ ...this.state.filter, name: e.target.value })}
                            />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <InputText
                                placeholder="Description"
                                value={this.state.filter.description}
                                onChange={(e) => this.setFilter({ ...this.state.filter, description: e.target.value })}
                            />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            {/* <InputText
                                placeholder="Expired At"
                                value={this.state.filter.expiredAt}
                                onChange={(e) => this.setFilter({ ...this.state.filter, expiredAt: e.target.value })}
                            /> */}
                            <Calendar
                                dateFormat="yy-mm-dd"
                                placeholder="Expired At"
                                id="basic" value={this.state.filter.expiredAt}
                                onChange={(e) => this.setFilter({ ...this.state.filter, expiredAt: e.target.value })} />
                        </div>
                    </div>
                    <div className="p-d-flex p-jc-center">
                        <div className="p-mr-2">
                            <Button
                                className="blue-btn"
                                icon="pi pi-filter"
                                iconPos="left"
                                label="Search"
                                onClick={() => this.applyFilter()} />
                        </div>
                        <div>
                            <Button
                                className="p-button-warning"
                                icon="pi pi-trash"
                                iconPos="left"
                                label="Clear"
                                onClick={() => this.resetFilter()}
                            />
                        </div>
                    </div>
                </Fieldset>
                <DataTable ref={(el) => this.dt = el}
                    lazy={true}
                    first={this.state.page * this.state.rows}
                    value={this.state.content}
                    loading={this.state.loading}
                    header={header}
                    className="p-datatable-customers"
                    dataKey="id"
                    rowHover

                    // ---Paginator--- 
                    paginator={true}
                    onPage={this.onPage}
                    onSort={this.onSort}
                    rows={this.state.rows}
                    totalRecords={this.state.total}
                    // ---Paginator--- 
                    sortField={this.state.sortField}
                    sortOrder={this.state.sortOrder}

                    emptyMessage="No item found"
                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                >
                    <Column field="code" header="Code" body={this.codeBodyTemplate} sortable />
                    <Column sortField="name" filterField="name" header="Name" body={this.categoryBodyTemplate} sortable />
                    <Column sortField="expiredAt" filterField="expiredAt" header="Expired At" body={this.expiredAtBodyTemplate} sortable />
                    <Column field="unitType" header="Unit Type" body={this.unitTypeBodyTemplate} />
                    <Column field="unit" header="Unit" body={this.unitBodyTemplate} />
                    <Column field="imports" header="Import" body={this.importBodyTemplate} />
                    <Column field="description" header="Description" body={this.descriptionBodyTemplate} sortable />
                    {/* <Column header="Action" body={(rowData) => this.actionBodyTemplate(rowData, this.form)} /> */}
                </DataTable>
            </div>
        );
    }
}

export default withRouter(IngredientItem)

