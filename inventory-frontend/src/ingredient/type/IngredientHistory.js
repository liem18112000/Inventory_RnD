import React, { Component } from 'react';
import { classNames } from 'primereact/utils';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { SplitButton } from 'primereact/splitbutton';
import { Button } from "primereact/button";
import { Fieldset } from "primereact/fieldset";

import '../../assets/styles/TableDemo.css'
import { Dropdown } from 'primereact/dropdown';

import { Link } from 'react-router-dom'
import moment from 'moment';
import { handleGetPage } from "../../core/handlers/ApiLoadContentHandler";
import { Toast } from 'primereact/toast';
import InventoryTrackService from '../../service/InventoryTrackService';

export class IngredientHistory extends Component {

    constructor(props) {
        super(props);
        this.state = {
            ingredientHistory: [],
            // --paginator state--
            page: 0,
            rows: 10,
            total: 0,
            sortField: "",
            sortOrder: 0,
            // --paginator state--
            filter: {
                name: "",
                description: "",
                actorName: "",
                actorRole: "",
                event: "",
                trackTimestamp: "",
                status: "",
            },
            cateId: props.location.state.cateId,
            events: [],
            statuses: [],
            isMock: false,
            loading: false
        };
        this.inventoryTrackService = new InventoryTrackService();
    }

    componentDidMount() {
        this.setState({ loading: true });
        this.getPages();
        this.getEvents();
        this.getStatuses();
    };

    getPages = () => {
        this.inventoryTrackService
            .getPage(
                this.props.match.params.id,
                this.state.filter,
                this.state.page,
                this.state.rows,
                this.state.sortField,
                this.state.sortOrder,
                this.state.isMock,
            )
            .then(data => handleGetPage(data, this.toast))
            .then(data => this.setState(
                {
                    ingredientHistory: data.content,
                    loading: false,
                    total: data.totalElements,
                    page: data.pageable.pageNumber,
                    rows: data.pageable.pageSize
                }
            ))
            .then(() => { console.log(this.state.ingredientHistory); })
            ;
    };

    /**
     * Fetch all events as label-value list
     */
    getEvents = () => {
        this.inventoryTrackService.getEvents(this.state.isMock)
            .then(events => this.setState({
                ...this.state,
                events: events
            }))
    }

    /**
     * Fetch all status as label-value list
     */
    getStatuses = () => {
        this.inventoryTrackService.getStatuses(this.state.isMock)
            .then(statuses => this.setState({...this.state, statuses: statuses}))
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
                name: "",
                description: "",
                actorName: "",
                actorRole: "",
                event: "",
                trackTimestamp: "",
                status: "",
            }
        }, () => {
            this.setState({ loading: true });
            this.getPages();
            this.toast.show({ severity: 'info', summary: 'Clear', detail: 'Clear filter', life: 1000 });
        })
    }

    applyFilter = () => {
        this.setState({ loading: true });
        this.getPages();
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

    statusBodyTemplate(rowData) {
        return <span className={`customer-badge status-${rowData.event.status.name}`}>{rowData.event.status.name}</span>;
    }

    eventBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Event</span>
                <span className={classNames('customer-badge', 'status-' + rowData.event.status.name)}>{rowData.event.name.replace(/_/g, " ")}</span>
            </React.Fragment>
        );
    }

    nameBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Name</span>
                <span style={{ verticalAlign: 'middle' }}>{rowData.name}</span>
            </React.Fragment>
        );
    }

    trackTimestampBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Track Timestamp</span>
                <span style={{ verticalAlign: 'middle' }}>{moment(rowData.trackTimestamp).format('HH:mm A ddd DD/MMM/YYYY')}</span>
            </React.Fragment>
        );
    }

    actorNameBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Actor Name</span>
                <span>{rowData.actorName}</span>
            </React.Fragment>
        );
    }

    actorRoleBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Actor Role</span>
                <span>{rowData.actorRole}</span>
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
                this.getPages();
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
                this.getPages();
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
            this.getPages()
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
                this.getPages();
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
                <Fieldset legend="Inventory History" toggleable>
                    <div className="p-grid p-fluid">
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <InputText
                                placeholder="Actor Name"
                                value={this.state.filter.actorName}
                                onChange={(e) => this.setFilter({ ...this.state.filter, actorName: e.target.value })}
                            />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <Dropdown value={this.state.filter.event}
                                placeholder="Event"
                                options={this.state.events}
                                itemTemplate={item => item.label}
                                onChange={(e) => {
                                    this.setState({ filter: { ...this.state.filter, event: e.target.value } })
                                }} />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <InputText
                                placeholder="Actor Role"
                                value={this.state.filter.actorRole}
                                onChange={(e) => this.setFilter({ ...this.state.filter, actorRole: e.target.value })}
                            />
                        </div>
                        <div className="p-col-12 p-md-6 p-lg-6">
                            <Dropdown value={this.state.filter.status}
                                placeholder="Status"
                                options={this.state.statuses}
                                itemTemplate={item => item.label}
                                onChange={(e) => {
                                    this.setState({ filter: { ...this.state.filter, status: e.target.value } })
                                }} />
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
                    value={this.state.ingredientHistory}
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
                    <Column sortField="name" filterField="name" header="Name" body={this.nameBodyTemplate} sortable />
                    <Column field="description" header="Description" body={this.descriptionBodyTemplate} sortable />
                    <Column field="actorName" header="Actor Name" body={this.actorNameBodyTemplate} sortable />
                    <Column field="actorRole" header="Actor Role" body={this.actorRoleBodyTemplate} sortable />
                    <Column field="event" header="Event" body={this.eventBodyTemplate} sortable />
                    <Column field="eventStatus" header="Status" sortable filterMenuStyle={{ width: '14rem' }} style={{ minWidth: '10rem' }} body={this.statusBodyTemplate} />
                    <Column sortField="trackTimestamp" filterField="trackTimestamp" header="Track Timestamp" body={this.trackTimestampBodyTemplate} sortable />
                </DataTable>
            </div>
        );
    }
}