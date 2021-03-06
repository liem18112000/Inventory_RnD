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
import { Dropdown } from 'primereact/dropdown';

import { Link } from 'react-router-dom'
import moment from 'moment';
import { IngredientTypeForm } from './IngredientTypeForm';
import { handleGetPage } from "../../core/handlers/ApiLoadContentHandler";
import { Toast } from 'primereact/toast';
import { confirmDialog } from 'primereact/confirmdialog';

export class IngredientType extends Component {

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
                code: "",
                description: "",
                createAt: "",
                unit: "",
                unitType: "",
            },
            isMock: false,
            loading: false
        };

        this.ingredientService = new IngredientService();
        // console.log(props.location.state);
    }

    componentDidMount() {
        this.setState({ loading: true });
        this.getPageTypes();
        this.ingredientService.getUnitTypes(this.state.isMock).then(ut => this.setState({
            unitTypes: ut
        }));
    };

    getPageTypes = () => {
        this.ingredientService.syncInventory().then(() => {
            this.ingredientService
                .getPageType(
                    this.props.match.params.id,
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
                code: "",
                description: "",
                createAt: "",
                unit: "",
                unitType: "",
            }
        }, () => {
            this.setState({ loading: true });
            this.getPageTypes();
            this.toast.show({ severity: 'info', summary: 'Clear', detail: 'Clear filter', life: 1000 });
        })
    }

    applyFilter = () => {
        this.setState({ loading: true });
        this.getPageTypes();
        this.toast.show({ severity: 'info', summary: 'Search', detail: 'Search data content', life: 1000 });
    }

    descriptionBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Description</span>
                <span>{rowData.description}</span>
            </React.Fragment>
        );
    }

    /**
     * Confim dialog for delete function
     * @param {*} rowData 
     */
    confirmDelete(rowData) {
        confirmDialog({
            message: 'Do you want to delete this type?',
            header: 'Delete Confirmation',
            icon: 'pi pi-info-circle',
            acceptClassName: 'p-button-danger',
            accept: () => this.ingredientService.deleteIngredient(rowData.id, this.state.isMock).then(this.getPageTypes),
            reject: () => this.toast.show({ severity: 'info', summary: 'Cancel delete', detail: 'You have cancel delete', life: 1000 })
        });
    }

    actionBodyTemplate(rowData, form) {
        let items = [
            {
                label: 'Edit',
                icon: 'pi pi-pencil',
                command: (e) => { form.action(rowData.id, this.props.match.params.id, this.state.isMock) }
            },
            {
                label: 'Ingredient History',
                icon: 'pi pi-external-link',
                command: (e) => {
                    this.props.history.push({
                        pathname: `history/${rowData.id}`,
                        state: {
                            cateId: this.props.match.params.id,
                            unitType: rowData.unitType,
                            unit: rowData.unit
                        }
                    })
                }
            },
            {
                label: 'Delete',
                icon: 'pi pi-trash',
                command: (e) => { this.confirmDelete(rowData) }
            },
        ];

        return (
            <React.Fragment>
                <span className="p-column-title">Action</span>
                <div className="card">
                    {/* <SplitButton label="View" onClick={() => window.location.replace(
                        `type/${rowData.id}`
                    )} model={items}></SplitButton> */}
                    <SplitButton label="View" onClick={() => this.props.history.push({
                        pathname: `type/${rowData.id}`,
                        state: {
                            cateId: this.props.match.params.id,
                            unitType: rowData.unitType,
                            unit: rowData.unit
                        }
                    })} model={items}></SplitButton>
                </div>
            </React.Fragment>
        );
    }

    unitBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Unit</span>
                <span className={classNames('customer-badge', 'status-' + rowData.status)}>{rowData.unit}</span>
            </React.Fragment>
        );
    }

    unitTypeBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Unit Type</span>
                <span className={classNames('customer-badge', 'status-' + rowData.status)}>{rowData.unitType}</span>
            </React.Fragment>
        );
    }

    codeBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Code</span>
                {rowData.code}
            </React.Fragment>
        );
    }

    categoryBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Categories</span>
                <span style={{ verticalAlign: 'middle' }}>{rowData.name}</span>
            </React.Fragment>
        );
    }

    createAtBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Active</span>
                <span style={{ verticalAlign: 'middle' }}>{moment(rowData.createAt).format('HH:mm A ddd DD/MMM/YYYY')}</span>
            </React.Fragment>
        );
    }

    quantityBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Quantity</span>
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
                this.getPageTypes();
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
                this.getPageTypes();
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
            this.getPageTypes()
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
                this.getPageTypes();
                this.toast.show({
                    severity: 'info', summary: 'Reset page size',
                    detail: 'Page size is set to ' + l, life: 1000
                });
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
                        <i className="pi pi-plus" />
                        <Button
                            className="blue-btn"
                            style={{ marginRight: '0.5rem' }}
                            icon="pi pi-plus"
                            iconPos="left"
                            label="New type"
                            onClick={() => this.form.action(null, this.props.match.params.id, true)}
                        />
                        <SplitButton className="table-control-length p-button-constrast" label="Refresh" icon="pi pi-refresh"
                            onClick={this.onRefresh} model={tableLengthOptions}>
                        </SplitButton>
                    </span>
                    <span className="p-input-icon-left" style={{ fontSize: "17px" }}>
                        <Link to='/ingredient'>Back to Ingredient Categories</Link>
                    </span>
                </div>
            </>
        )

        return (
            <div className="datatable-doc-demo">
                <Toast ref={(el) => this.toast = el} />
                <IngredientTypeForm ref={el => this.form = el}
                    refreshData={() => this.getPageTypes()}
                />
                <Fieldset legend="Ingredient Detail" toggleable>
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
                                placeholder="Category"
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
                            <InputText
                                placeholder="Create At"
                                value={this.state.filter.createAt}
                                onChange={(e) => this.setFilter({ ...this.state.filter, createAt: e.target.value })}
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
                // rowsPerPageOptions={[2, 5, 10, 25, 50]}
                >
                    <Column field="code" header="Code" body={this.codeBodyTemplate} sortable />
                    <Column sortField="name" filterField="name" header="Name" body={this.categoryBodyTemplate} sortable />
                    <Column sortField="createAt" filterField="createAt" header="Create At" body={this.createAtBodyTemplate} sortable />
                    <Column field="quantity" header="Quantity" body={this.quantityBodyTemplate} sortable />
                    <Column field="unitType" header="Unit Type" body={this.unitTypeBodyTemplate} sortable />
                    <Column field="unit" header="Unit" body={this.unitBodyTemplate} sortable />
                    <Column field="description" header="Description" body={this.descriptionBodyTemplate} sortable />
                    <Column header="Action" body={(rowData) => this.actionBodyTemplate(rowData, this.form)} />
                </DataTable>
            </div>
        );
    }
}