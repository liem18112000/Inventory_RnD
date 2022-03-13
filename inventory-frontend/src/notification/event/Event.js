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
import { NotificationService } from "../../service/NotificationService";
import { Dropdown } from "primereact/dropdown";
import { EventForm } from './EventForm';
import { EventTable } from './EventTable';

export class Event extends Component {

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
                eventType: "",
                description: ""
            },
            eventTypes: [],
            isMock: false,
            loading: false,
            displayResponsive: false
        };
        this.notificationService = new NotificationService();
        this.mapper = new PagingDataModelMapper();
        this.onClick = this.onClick.bind(this);
        this.onHide = this.onHide.bind(this);
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
                <Button label="No" icon="pi pi-times" onClick={() => this.onHide(name)} className="p-button-text" />
                <Button label="Yes" icon="pi pi-check" onClick={() => this.onHide(name)} autoFocus />
            </div>
        );
    }
    /**
     * This function is activated after component is created
     */
    componentDidMount() {
        this.setState({ loading: true });
        this.getPage();
        this.getEventTypes();
    };

    getEventTypes() {
        this.notificationService.getEventTypes(this.state.isMock).then(types => {
            this.setState({
                ...this.state,
                eventTypes: types.content
            })
        });
    }

    /**
     * Call get page supplier group API
     */
    getPage() {
        const { filter, page, rows, sortField, sortOrder, isMock } = this.state;
        this.notificationService
            .getPageEvent(filter, page, rows, sortField, sortOrder, isMock)
            .then(data => handleGetPage(data, this.toast))
            .then(data => this.mapper.toModel(data))
            .then(data => this.setState({ ...this.state, ...data }));
    }

    // /**
    //      * Confirm dialog for delete function
    //      * @param {*} rowData 
    //      */
    // confirmDelete(rowData) {
    //     confirmDialog({
    //         message: 'Do you want to delete this category?',
    //         header: 'Delete Confirmation',
    //         icon: 'pi pi-info-circle',
    //         acceptClassName: 'p-button-danger',
    //         accept: () => this.ingredientService.deleteIngredient(rowData.id, this.state.isMock).then(this.getPageCategories),
    //         reject: () => this.toast.show({ severity: 'info', summary: 'Cancel delete', detail: 'You have cancel delete', life: 1000 })
    //     });
    // }

    /**
     * Action body template
     * @param rowData event data row
     * @returns {JSX.Element}
     */
    actionBodyTemplate(rowData, form, table) {
        let items = [
            {
                label: 'Edit',
                icon: 'pi pi-pencil',
                command: (e) => { form.action(rowData.id) }
            },
            {
                label: 'Description',
                icon: 'pi pi-table',
                command: (e) => { table.action(rowData.description, rowData.eventType) }
            },
        ];


        return (
            <React.Fragment>
                <div className="card">
                    <SplitButton label="View" onClick={() => this.props.history.push({
                        pathname: `event/${rowData.id}`
                    })} model={items}></SplitButton>
                </div>
            </React.Fragment>
        );

    }

    /**
     * Category name body template
     * @param rowData event data row
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
     * Category name body template
     * @param rowData event data row
     * @returns {JSX.Element}
     */
    eventTypeBodyTemplate(rowData) {
        return (
            <React.Fragment>
                {rowData.eventType}
            </React.Fragment>
        );
    }

    /**
     * Occur at timestamp body template
     * @param rowData event data row
     * @returns {JSX.Element}
     */
    occurAtBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>{moment(rowData.occurAt).format('HH:mm-A-ddd-DD/MMM/YYYY')}</span>
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
                eventType: "",
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
                <EventForm ref={el => this.form = el}
                    refreshData={() => this.getPage()}
                />
                <EventTable ref={el => this.table = el}
                    refreshData={() => this.getPage()}
                />
                <Fieldset legend="Notification Event" toggleable>
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
                                        <Dropdown
                                            placeholder={"Event Type"}
                                            itemTemplates={item => item.label}
                                            options={this.state.eventTypes}
                                            value={this.state.filter.eventType}
                                            onChange={(e) => this.setFilter({ ...this.state.filter, eventType: e.target.value })}
                                        />
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

                    emptyMessage="No event found"
                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                >
                    <Column sortField="name" filterField="name" header="Name" body={this.nameBodyTemplate} sortable />
                    <Column sortField="occurAt" filterField="createAt" header="Occur At" body={this.occurAtBodyTemplate} sortable />
                    <Column sortField="eventType" filterField="eventType" header="Event Type" body={this.eventTypeBodyTemplate} sortable />
                    <Column header="Action" body={(rowData) => this.actionBodyTemplate(rowData, this.form, this.table)} />
                </DataTable>
            </div >
        );
    }
}
