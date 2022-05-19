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
import { Link } from "react-router-dom";
import { NotificationForm } from './NotificationForm';
import {handleExceptionWithSentryAndSendFeedback} from "../../core/utility/integrations/SentryExceptionResolver";
import { BREADCRUMB_HOME_MODEL, getBreadcrumbNotificationModel } from '../../components/common/breadcrumModel';
import { BreadCrumb } from 'primereact/breadcrumb';

export class Notification extends Component {

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
                description: "",
                type: "",
                status: "",
                event: {
                    id: props.match.params.id
                }
            },
            notificationTypes: [],
            notificationStatuses: [],
            isMock: false,
            loading: false,
            panelCollapsed: true,

            breadcrumbModel: getBreadcrumbNotificationModel()
        };
        this.notificationService = new NotificationService();
        this.mapper = new PagingDataModelMapper();
    }

    /**
     * This function is activated after component is created
     */
    componentDidMount() {
        this.setState({ loading: true });
        this.getPage();
        this.getNotificationTypes();
        this.getNotificationStatuses();
        if (this.props?.location?.state?.groupId) {
            this.notificationService
                .getEventByID(this.props?.location?.state?.groupId, this.state.isMock)
                .then(data => {
                    const { id, name } = data;
                    if (name && id) {
                        this.notificationService
                            .getEventByID(this.props.match.params.id, this.state.isMock)
                            .then(res => {
                                this.setState({
                                    ...this.state,
                                    breadcrumbModel: getBreadcrumbNotificationModel(
                                        name,
                                        res.name,
                                        id,
                                        res.id
                                    )
                                })
                            })
                    }
                });
        }
    };

    getNotificationTypes() {
        this.notificationService.getNotificationTypes(this.state.isMock).then(types => {
            this.setState({
                ...this.state,
                notificationTypes: types.content
            })
        }).catch(e => handleExceptionWithSentryAndSendFeedback(e, "Get notification type failed."));
    }

    getNotificationStatuses() {
        this.notificationService.getNotificationStatuses(this.state.isMock).then(statuses => {
            this.setState({
                ...this.state,
                notificationStatuses: statuses.content
            })
        }).catch(e => handleExceptionWithSentryAndSendFeedback(e, "Get notification status failed."))
    }

    /**
     * Call get page supplier group API
     */
    getPage() {
        const { filter, page, rows, sortField, sortOrder, isMock } = this.state;
        this.notificationService
            .getPageNotification(filter, page, rows, sortField, sortOrder, isMock)
            .then(data => handleGetPage(data, this.toast))
            .then(data => this.mapper.toModel(data))
            .then(data => this.setState({ ...this.state, ...data }));
    }

    /**
     * Description body
     * @param rowData event data row
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
     * @param rowData data row
     * @returns {JSX.Element}
     */
    actionBodyTemplate(rowData, form) {
        return (
            <React.Fragment>
                <div className="card">
                    <Button label="View"
                        onClick={(e) => { form.action(rowData.id) }}
                    ></Button>
                </div>
            </React.Fragment >
        );
    }

    /**
     * Notification name body template
     * @param rowData data row
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
     * Notification type body template
     * @param rowData data row
     * @returns {JSX.Element}
     */
    notificationTypeBodyTemplate(rowData) {
        return (
            <React.Fragment>
                {rowData.type}
            </React.Fragment>
        );
    }

    /**
     * Notification status body template
     * @param rowData data row
     * @returns {JSX.Element}
     */
    notificationStatusBodyTemplate(rowData) {
        return (
            <React.Fragment>
                {rowData.status}
            </React.Fragment>
        );
    }

    /**
     * Notify at timestamp body template
     * @param rowData event data row
     * @returns {JSX.Element}
     */
    occurAtBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>
                    {rowData.notifyAt ? moment(rowData.notifyAt).format('HH:mm-A-ddd-DD/MMM/YYYY') : "Not Available"}
                </span>
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
                description: "",
                type: "",
                status: "",
                event: {
                    id: this.state.filter.event.id
                }
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
                    <SplitButton className="table-control-length p-button-constrast" label="Refresh" icon="pi pi-refresh"
                        onClick={this.onRefresh} model={tableLengthOptions}>
                    </SplitButton>
                </span>
                {/* <span className="p-input-icon-left" style={{ fontSize: "17px" }}>
                    <Link to='/notification/event'>Back to Notification Event</Link>
                </span> */}
            </div>
        )

        return (
            <div className="datatable-doc-demo">
                <Toast ref={(el) => this.toast = el} />
                <NotificationForm ref={el => this.form = el}
                    refreshData={() => this.getPage()}
                />
                <BreadCrumb
                    model={this.state.breadcrumbModel}
                    home={BREADCRUMB_HOME_MODEL} />
                <Fieldset legend="Notification" toggleable collapsed={this.state.panelCollapsed}>
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
                                            placeholder="Description"
                                            value={this.state.filter.description}
                                            onChange={(e) => this.setFilter({ ...this.state.filter, description: e.target.value })}
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
                                            placeholder={"Notification Type"}
                                            itemTemplates={item => item.label}
                                            options={this.state.notificationTypes}
                                            value={this.state.filter.type}
                                            onChange={(e) => this.setFilter({ ...this.state.filter, type: e.target.value })}
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
                                            placeholder={"Notification Status"}
                                            itemTemplates={item => item.label}
                                            options={this.state.notificationStatuses}
                                            value={this.state.filter.status}
                                            onChange={(e) => this.setFilter({ ...this.state.filter, status: e.target.value })}
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

                    emptyMessage="No event found"
                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                >
                    <Column sortField="name" filterField="name" header="Name" body={this.nameBodyTemplate} sortable />
                    <Column field="description" header="Description" body={this.descriptionBodyTemplate} sortable />
                    <Column sortField="notifyAt" filterField="notifyAt" header="Notify At" body={this.occurAtBodyTemplate} sortable />
                    <Column sortField="type" filterField="type" header="Type" body={this.notificationTypeBodyTemplate} sortable />
                    <Column sortField="status" filterField="status" header="Status" body={this.notificationStatusBodyTemplate} sortable />
                    <Column header="Action" body={(rowData) => this.actionBodyTemplate(rowData, this.form)} />
                </DataTable>
            </div >
        );
    }
}