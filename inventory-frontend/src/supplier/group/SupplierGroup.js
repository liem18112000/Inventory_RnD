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
import { handleGetPage } from "../../core/handlers/ApiLoadContentHandler";
import { Toast } from 'primereact/toast';
import { PagingDataModelMapper } from "../../core/models/mapper/ModelMapper";
import { SupplierGroupForm } from './SupplierGroupForm';
import { SupplierService } from '../../service/SupplierService';
import { Calendar } from 'primereact/calendar';
import { convertDateToEnCADate } from '../../core/utility/ComponentUtility';

export class SupplierGroup extends Component {

    /**
     * Constructor
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            content: [],
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
            },
            isMock: false,
            loading: false
        };
        this.supplierService = new SupplierService();
        this.mapper = new PagingDataModelMapper();
    }

    /**
     * This function is activated after component is created
     */
    componentDidMount() {
        this.setState({ loading: true });
        this.getPage()
    };

    /**
     * Call get page supplier group API
     */
    getPage = () => {
        const { filter, page, rows, sortField, sortOrder, isMock } = this.state;
        this.supplierService
            .getPageGroup(filter, page, rows, sortField, sortOrder, isMock)
            .then(data => handleGetPage(data, this.toast))
            .then(data => this.mapper.toModel(data))
            .then(data => this.setState({ ...this.state, ...data }));
    }

    /**
     * Description body
     * @param rowData           supplier group data row
     * @returns {JSX.Element}
     */
    descriptionBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span>{rowData.description}</span>
            </React.Fragment>
        );
    }

    /**
     * Action body template
     * @param rowData           supplier group data row
     * @param form              supplier group form
     * @returns {JSX.Element}
     */
    actionBodyTemplate(rowData, form) {
        let items = [
            {
                label: 'Edit',
                icon: 'pi pi-pencil',
                command: (e) => { form.action(rowData.id, this.state.isMock) }
            },
        ];

        return (
            <React.Fragment>
                <div className="card">
                    <SplitButton label="View" onClick={() => this.props.history.push({
                        pathname:`supplier/${rowData.id}`
                    })} model={items}>
                    </SplitButton>
                </div>
            </React.Fragment>
        );
    }

    /**
     * Code body template
     * @param rowData           supplier group data row
     * @returns {JSX.Element}
     */
    codeBodyTemplate(rowData) {
        return (
            <React.Fragment>
                {rowData.code}
            </React.Fragment>
        );
    }

    /**
     * Category name body template
     * @param rowData           supplier group data row
     * @returns {JSX.Element}
     */
    nameBodyTemplate(rowData) {
        return (
            <React.Fragment>
                {rowData.name}
            </React.Fragment>
        );
    }

    /**
     * Create at timestamp body template
     * @param rowData           supplier group data row
     * @returns {JSX.Element}
     */
    createAtBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>{moment(rowData.createdAt).format('HH:mm-A-ddd-DD/MMM/YYYY')}</span>
            </React.Fragment>
        );
    }

    updateAtBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle' }}>{moment(rowData.updatedAt).format('HH:mm A ddd DD/MMM/YYYY')}</span>
            </React.Fragment>
        );
    }

    /**
     * Set state for filter
     * @param filter    supplier group filter fields
     */
    setFilter = (filter) => {
        this.setState({
            ...this.state,
            filter: filter
        })
    }

    /**
     * Rest stat of supplier group filter
     */
    resetFilter = () => {
        this.setState({
            ...this.state,
            filter: {
                name: "",
                code: "",
                updateAt: "",
                createAt: ""
            }
        }, () => {
            this.setState({ loading: true });
            this.getPage();
            this.toast.show({ severity: 'info', summary: 'Clear', detail: 'Clear filter', life: 1000 });
        })
    }

    /**
     * Apply supplier group filter fields
     */
    applyFilter = () => {
        this.setState({ loading: true });
        this.getPage();
        this.toast.show({ severity: 'info', summary: 'Search', detail: 'Search data content', life: 1000 });
    }

    /**
     * Reset page length
     * @param e
     */
    onPage = e => {
        this.setState(
            {
                loading: true,
                page: e.page
            },
            () => {
                this.getPage();
            }
        );
    };

    /**
     * Change page on datatable
     * @param e
     */
    onSort = e => {
        this.setState(
            {
                loading: true,
                sortField: e.sortField,
                sortOrder: e.sortOrder
            },
            () => {
                this.setState({ loading: true });
                this.getPage();
                this.toast.show({
                    severity: 'info',
                    summary: 'Sort',
                    detail: 'Sort ' + e.sortField + " " + (e.sortOrder === 1 ? "ascending" : "descending"),
                    life: 1000
                });
            }
        );
    };

    /**
     * Refresh data table
     */
    onRefresh = () => {
        this.setState({
            ...this.state,
            loading: true
        }, () => {
            this.getPage()
            this.toast.show({ severity: 'info', summary: 'Refresh', detail: 'Refresh datatable', life: 1000 });
        })
    }

    /**
     * Reset page length
     * @param l page length
     */
    onChangePageLength = l => {
        this.setState(
            {
                rows: l,
                page: 0
            },
            () => {
                this.setState({ loading: true });
                this.getPage();
                this.toast.show({ severity: 'info', summary: 'Reset page size', detail: 'Page size is set to ' + l, life: 1000 });
            }
        );
    };

    /**
     * Brief datatable description
     * @param page          Page
     * @param rows          Size
     * @param total         Total element
     * @returns {string}    Datatable description
     */
    getTablePageReport = (page, rows, total) => {
        if (total > 0) {
            let first = (page * rows) + 1;
            let last = page * rows + rows;
            if (last > total) last = total;
            return `Showing ${first} to ${last} of ${total} entries`;
        }
        return ''
    };

    /**
     * Render component
     * @returns {JSX.Element}
     */
    render() {
        const tableLengthOptions = [
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

        const header = (
            <div className="table-header">
                <span className="p-input-icon-left">
                    <i className="pi pi-plus" />
                    <Button
                        className="blue-btn-1"
                        style={{ marginRight: '0.5rem' }}
                        icon="pi pi-plus"
                        iconPos="left"
                        label="New group"
                        onClick={() => this.form.action(null, true)}
                    />
                    <SplitButton className="table-control-length p-button-constrast" label="Refresh" icon="pi pi-refresh"
                        onClick={this.onRefresh} model={tableLengthOptions}>
                    </SplitButton>
                </span>
                <div style={{}}>
                </div>
            </div>
        )

        return (
            <div className="datatable-doc-demo">
                <Toast ref={(el) => this.toast = el} />
                <SupplierGroupForm ref={el => this.form = el}
                    refreshData={() => this.getPage()}
                />
                <Fieldset legend="Supplier Group" toggleable>
                    <div className="p-grid p-fluid">
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
                                        <InputText
                                            placeholder="Code"
                                            value={this.state.filter.code}
                                            onChange={(e) => this.setFilter({ ...this.state.filter, code: e.target.value })}
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
                                            placeholder="Create From"
                                            id="basic" value={this.state.filter.createAt}
                                            onChange={(e) => this.setFilter({ ...this.state.filter, createAt: convertDateToEnCADate(e.target.value) })} />
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
                    value={this.state.content}
                    loading={this.state.loading}
                    header={header}
                    className="p-datatable-customers"
                    dataKey="id"
                    rowHover scrollable scrollHeight="calc(85vh - 200px)"

                    // ---Paginator--- 
                    paginator={true}
                    onPage={this.onPage}
                    onSort={this.onSort}
                    rows={this.state.rows}
                    totalRecords={this.state.total}
                    // ---Paginator--- 
                    sortField={this.state.sortField}
                    sortOrder={this.state.sortOrder}

                    emptyMessage="No supplier group found"
                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                >
                    <Column sortField="name" filterField="name" header="Name" body={this.nameBodyTemplate} sortable />
                    <Column field="code" header="Code" body={this.codeBodyTemplate} sortable />
                    <Column field="description" header="Description" body={this.descriptionBodyTemplate} sortable />
                    <Column sortField="createdAt" filterField="createdAt" header="Create From" body={this.createAtBodyTemplate} sortable />
                    <Column sortField="updatedAt" filterField="updatedAt" header="Update From" body={this.updateAtBodyTemplate} sortable />
                    <Column header="Action" body={(rowData) => this.actionBodyTemplate(rowData, this.form)} />
                </DataTable>
            </div >
        );
    }
}