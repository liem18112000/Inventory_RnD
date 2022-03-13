import React, { Component } from 'react';
import { classNames } from 'primereact/utils';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { SplitButton } from 'primereact/splitbutton';
import { Button } from "primereact/button";
import { Fieldset } from "primereact/fieldset";

import { IngredientService } from '../service/IngredientService.js'

import '../assets/styles/TableDemo.css'
import { Dropdown } from 'primereact/dropdown';
import moment from 'moment';
import { handleGetPage } from '../core/handlers/ApiLoadContentHandler.js';
import { Toast } from 'primereact/toast';

export class IngredientInventory extends Component {

    constructor(props) {
        super(props);
        this.state = {
            ingredient: [],
            selectedUnit: null,
            selectedUnitType: null,
            // --paginator state--
            page: 0,
            rows: 10,
            total: 0,
            sortField: "",
            sortOrder: 0,
            // --paginator state--
            filter: {
                name: "",
                ingredientId: "",
                description: "",
                updateAt: "",
                unit: "",
                unitType: "",
            },
            isMock: false,
            loading: false,
            ingredientList: []
        };

        this.ingredientService = new IngredientService();
    }

    componentDidMount() {
        this.setState({ loading: true });
        this.getPageInventories();
        this.ingredientService.getUnitTypes(this.state.isMock).then(ut => {
            this.setState({
                unitTypes: ut
            })
        });
        // get ingredient detail list for inventory filter
        this.ingredientService.getInventoryIngredientDetail(this.state.isMock).then(data => {
            this.setState({
                ingredientList: data
            })
        })
    };

    itemTemplate(item) {
        return (
            <div className="ingredient-item">
                <div>{item.label}</div>
            </div>
        );
    }

    getPageInventories = () => {
        console.log(this.state.filter)
        this.ingredientService.syncInventory().then(() => {
            this.ingredientService
                .getPageInventory(
                    this.state.filter,
                    this.state.page,
                    this.state.rows,
                    this.state.sortField,
                    this.state.sortOrder,
                    this.state.isMock
                )
                .then(data => handleGetPage(data, this.toast))
                .then(data => this.setState(
                    {
                        ingredient: data.content,
                        loading: false,
                        total: data.totalElements,
                        page: data.pageable.pageNumber,
                        rows: data.pageable.pageSize
                    })
                );
        })

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
                ingredientId: "",
                description: "",
                updateAt: "",
                unit: "",
                unitType: "",
            },
            selectedIngredientList: {
                value: null,
                label: null
            }
        }, () => {
            this.setState({ loading: true });
            this.getPageInventories();
            this.toast.show({ severity: 'info', summary: 'Clear', detail: 'Clear filter', life: 1000 });
        })
    }

    applyFilter = () => {
        this.setState({ loading: true });
        this.getPageInventories();
        this.toast.show({ severity: 'info', summary: 'Search', detail: 'Search data content', life: 1000 });
    }

    descriptionBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span>{rowData.description}</span>
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

    nameBodyTemplate(rowData) {
        return (
            <React.Fragment>
                {rowData.name}
            </React.Fragment>
        );
    }

    updatedAtBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle' }}>{moment(rowData.updatedAt).format('HH:mm A ddd DD/MMM/YYYY')}</span>
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
                this.getPageInventories();
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
                this.setState({ loading: true });
                this.getPageInventories();
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
            this.getPageInventories()
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
                this.getPageInventories()
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
                <div className="table-header" >
                    <span className="p-input-icon-left">
                        <SplitButton className="table-control-length p-button-constrast" label="Refresh" icon="pi pi-refresh"
                            onClick={this.onRefresh} model={tableLengthOptions}>
                        </SplitButton>
                    </span>
                </div>
            </>
        )

        return (
            <div className="datatable-doc-demo">
                <Toast ref={(el) => this.toast = el} />
                <Fieldset legend="Ingredient Inventory" toggleable>
                    <div className="p-grid p-fluid">
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <InputText
                                placeholder="Name"
                                value={this.state.filter.name}
                                onChange={(e) => this.setFilter({ ...this.state.filter, name: e.target.value })}
                            />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <Dropdown value={this.state.filter.ingredientId}
                                placeholder="Ingredient"
                                options={this.state.ingredientList}
                                itemTemplate={this.itemTemplate}
                                onChange={(e) => {
                                    console.log(e.target);
                                    this.setState({ filter: { ...this.state.filter, ingredientId: e.target.value } })
                                }} />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <InputText
                                placeholder="Description"
                                value={this.state.filter.description}
                                onChange={(e) => this.setFilter({ ...this.state.filter, description: e.target.value })}
                            />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <InputText
                                placeholder="Update At"
                                value={this.state.filter.updateAt}
                                onChange={(e) => this.setFilter({ ...this.state.filter, updateAt: e.target.value })}
                            />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <Dropdown value={this.state.filter.unitType}
                                defaultValue=""
                                placeholder="Unit Types"
                                options={this.state.unitTypes}
                                onChange={(e) => {
                                    this.setState({
                                        filter: { ...this.state.filter, unitType: e.target.value }
                                    },
                                        () => {
                                            this.ingredientService.getUnit(this.state.filter.unitType, false).then(u => this.setState({
                                                units: u
                                            }));
                                        }
                                    )
                                }} />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <Dropdown value={this.state.filter.unit}
                                defaultValue=""
                                placeholder="Unit"
                                options={this.state.units}
                                disabled={this.state.filter.unitType === "" ? "true" : ""}
                                onChange={(e) => this.setState({ filter: { ...this.state.filter, unit: e.target.value } })} />
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
                    value={this.state.ingredient}
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

                    emptyMessage="No ingredient categories found"
                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                >
                    <Column field="name" header="Name" body={this.nameBodyTemplate} sortable />
                    <Column field="description" header="Description" body={this.descriptionBodyTemplate} sortable />
                    <Column field="quantity" header="Quantity" body={this.quantityBodyTemplate} sortable />
                    <Column field="unitType" header="Unit Type" body={this.unitTypeBodyTemplate} sortable />
                    <Column field="unit" header="Unit" body={this.unitBodyTemplate} sortable />
                    <Column sortField="updateAt" filterField="updateAt" header="Update At" body={this.updatedAtBodyTemplate} sortable />
                </DataTable>
            </div>
        );
    }
}