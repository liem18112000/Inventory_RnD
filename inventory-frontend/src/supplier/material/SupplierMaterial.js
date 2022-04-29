import React, { Component } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { SplitButton } from 'primereact/splitbutton';
import { Fieldset } from 'primereact/fieldset';

import '../../assets/styles/TableDemo.css';
import moment from 'moment';
import { Button } from "primereact/button";
import 'primeflex/primeflex.css';
import { Link } from 'react-router-dom';
import { handleGetPage } from "../../core/handlers/ApiLoadContentHandler";
import { Toast } from "primereact/toast";
import { confirmDialog } from 'primereact/confirmdialog';
import { SupplierMaterialForm } from './SupplierMaterialForm';
import { SupplierService } from '../../service/SupplierService';
import { IngredientService } from '../../service/IngredientService';
import { Dropdown } from 'primereact/dropdown';
import { Calendar } from 'primereact/calendar';
import { convertDateToEnCADate } from '../../core/utility/ComponentUtility';

export class SupplierMaterial extends Component {
    constructor(props) {
        super(props);
        this.state = {
            supplierMaterial: [],
            ingredientList: [],
            selectedStatus: null,
            // --paginator state--
            page: 0,
            rows: 10,
            total: 0,
            sortField: "",
            sortOrder: 0,
            // --paginator state--
            filter: {
                supplierId: props.match.params.id,
                ingredientId: null,
                name: "",
                code: "",
            },
            supplierGroupId: props.location.state.supplierGroupId,
            isMock: false,
            loading: false,
            panelCollapsed: true
        };
        this.supplierService = new SupplierService();
        this.ingredientService = new IngredientService();
        console.log(props);
    }

    componentDidMount() {
        this.setState({ loading: true });
        this.getPageMaterials();
        this.ingredientService.getInventoryIngredientDetail(this.state.isMock).then(data => {
            this.setState({
                ...this.state, ingredientList: data
            })
        })
    };

    getPageMaterials = () => {
        this.supplierService
            .getPageMaterial(
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
                    supplierMaterial: data.content,
                    loading: false,
                    total: data.totalElements,
                    page: data.pageable.pageNumber,
                    rows: data.pageable.pageSize
                })
            );
    }

    descriptionBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span>{rowData.description}</span>
            </React.Fragment>
        );
    }

    deleteMaterial(materialId) {
        if (!materialId) {
            this.toast.show({
                severity: 'warning', summary: 'Delete failed',
                detail: 'Material id is not set', life: 3000
            })
        } else {
            this.supplierService.deleteMaterial(materialId, this.state.isMock)
                .then(res => {
                    if (res) {
                        this.toast.show({
                            severity: 'success', summary: 'Delete success',
                            detail: 'Material has been deleted', life: 1000
                        })
                        this.getPageMaterials()
                    } else {
                        this.toast.show({
                            severity: 'error', summary: 'Delete failed',
                            detail: 'Material caught an unknown error.', life: 5000
                        })
                    }
                })
        }
    }

    confirmDelete(rowData) {
        confirmDialog({
            message: 'Do you want to delete this detail?',
            header: 'Delete Confirmation',
            icon: 'pi pi-info-circle',
            acceptClassName: 'p-button-danger',
            accept: () => this.deleteMaterial(rowData.id),
            reject: () => this.toast.show({
                severity: 'info', summary: 'Cancel delete',
                detail: 'You have cancel delete', life: 1000
            })
        });
    }

    actionBodyTemplate(rowData, form) {
        let items = [
            {
                label: 'Delete',
                icon: 'pi pi-trash',
                command: (e) => {
                    this.confirmDelete(rowData)
                }
            }
        ];

        return (
            <React.Fragment>
                <div className="card">
                    <SplitButton label="Edit"
                        onClick={() => form.action(rowData.id, this.props.match.params.id, false)}
                        model={items}></SplitButton>
                </div>
            </React.Fragment>
        );
    }

    ingredientBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>{rowData.ingredient.name}</span>
            </React.Fragment>
        );
    }

    nameBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>{rowData.name}</span>
            </React.Fragment>
        );
    }

    updatedAtBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>{moment(rowData.updatedAt).format('HH:mm-A-ddd-DD/MMM/YYYY')}</span>
            </React.Fragment>
        );
    }

    maxQuantityBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>{rowData.maximumQuantity}</span>
            </React.Fragment>
        );
    }

    minQuantityBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>{rowData.minimumQuantity}</span>
            </React.Fragment>
        );
    }

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
                ...this.state.filter,
                ingredientId: null,
                name: "",
                code: "",
                updateAt: "",
                createAt: ""
            }
        }, () => {
            this.setState({ loading: true });
            this.getPageMaterials()
            this.toast.show({ severity: 'info', summary: 'Clear', detail: 'Clear filter', life: 1000 });
        })
    }

    applyFilter = () => {
        this.setState({ loading: true });
        this.getPageMaterials()
        this.toast.show({ severity: 'info', summary: 'Search', detail: 'Search data content', life: 1000 });
    }

    onPage = e => {
        this.setState(
            {
                loading: true,
                page: e.page
            },
            () => {
                this.getPageMaterials();
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
                this.getPageMaterials();
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
            this.getPageMaterials()
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
                this.getPageMaterials()
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
            <div className="table-header">
                <span className="p-input-icon-left">
                    <i className="pi pi-plus" />
                    <Button
                        className="blue-btn"
                        style={{ marginRight: '0.5rem' }}
                        icon="pi pi-plus"
                        iconPos="left"
                        label="New material"
                        onClick={() => this.form.action(null, this.props.match.params.id, true)}
                    />
                    <SplitButton className="table-control-length p-button-constrast" label="Refresh" icon="pi pi-refresh"
                        onClick={this.onRefresh} model={tableLengthOptions}>
                    </SplitButton>
                </span>
                <span className="p-input-icon-left" style={{ fontSize: "17px" }}>
                    <Link to={`../${this.state.supplierGroupId}`}> Back to Supplier</Link>
                </span>
            </div>
        )

        return (
            <div className="datatable-doc-demo">
                <Toast ref={(el) => this.toast = el} />
                <SupplierMaterialForm ref={el => this.form = el}
                    refreshData={() => this.getPageMaterials()}
                    id={this.props.match.params.id}
                />
                <Fieldset legend="Supplier Material" toggleable collapsed={this.state.panelCollapsed}>
                    <div className="p-grid p-fluid">
                        <div className="p-col-12 p-md-6">
                            <div className="p-grid">
                                <div className="p-col-12">
                                    <div className="p-inputgroup">
                                        <Dropdown value={this.state.filter.ingredientId}
                                            placeholder="Ingredient"
                                            itemTemplate={item => item.label}
                                            options={this.state.ingredientList}
                                            onChange={(e) => {
                                                this.setFilter({ ...this.state.filter, ingredientId: e.target.value })
                                            }}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="p-col-12 p-md-6">
                            <div className="p-grid">
                                <div className="p-col-12">
                                    <div className="p-inputgroup">
                                        <InputText
                                            placeholder="Name"
                                            value={this.state.filter.name}
                                            onChange={(e) => this.setFilter({ ...this.state.filter, name: e.target.value })}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="p-col-12 p-md-6">
                            <div className="p-grid">
                                <div className="p-col-12">
                                    <div className="p-inputgroup">
                                        <Calendar
                                            dateFormat="yy-mm-dd"
                                            placeholder="Update From"
                                            id="basic" value={this.state.filter.updateAt}
                                            onChange={(e) => this.setFilter({ ...this.state.filter, updateAt: convertDateToEnCADate(e.target.value) })} />
                                    </div>
                                </div>
                            </div>
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
                < DataTable ref={(el) => this.dt = el}
                    lazy={true}
                    first={this.state.page * this.state.rows}
                    value={this.state.supplierMaterial}
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

                    emptyMessage="No supplier material found"
                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                >
                    <Column field="ingredientId" header="Ingredient" body={this.ingredientBodyTemplate} sortable />
                    <Column field="name" header="Name" body={this.nameBodyTemplate} sortable />
                    <Column field="description" header="Description" body={this.descriptionBodyTemplate} sortable />
                    <Column field="minimumQuantity" header="Min Quant" body={this.minQuantityBodyTemplate} sortable />
                    <Column field="maximumQuantity" header="Max Quant" body={this.maxQuantityBodyTemplate} sortable />
                    <Column field="updatedAt" header="Updated From" body={this.updatedAtBodyTemplate} sortable />
                    <Column header="Action" body={(rowData) => this.actionBodyTemplate(rowData, this.form)} />
                </DataTable>
            </div >
        );
    }
}
