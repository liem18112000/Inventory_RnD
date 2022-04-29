import React, { Component } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import 'primeflex/primeflex.css';
import { handleGetPage } from "../../core/handlers/ApiLoadContentHandler";
import { PagingDataModelMapper } from "../../core/models/mapper/ModelMapper";
import { NotificationService } from "../../service/NotificationService";
import { OverlayPanel } from 'primereact/overlaypanel';
import { Badge } from 'primereact/badge';
import '../../assets/styles/TableDemo.css'
import { Link } from 'react-router-dom';

export class NotificationDialog extends Component {

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
            rows: 5,
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
            displayResponsive: false,
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
    };

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
    }

    nameTemplate(rowData) {
        // console.log(content)
        return (
            <React.Fragment>
                <Link to={`event/${rowData.id}`}>
                    <span
                        style={{ whileSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', width: '200px' }}
                    >
                        {rowData.name}
                    </span>
                </Link>
            </React.Fragment>
        );
    }

    /**
     * Render component
     * @returns {JSX.Element}
     */
    render() {
        return (
            <>
                <i className="pi pi-bell p-m-4 p-text-secondary p-overlay-badge"
                    style={{ fontSize: '2rem', float: 'right' }}
                    onClick={(e) => this.op.toggle(e)}
                    aria-haspopup aria-controls="overlay_panel"
                >
                    <Badge value={this.state.total} />
                </i>
                <OverlayPanel ref={(el) => this.op = el} showCloseIcon id="overlay_panel" className="overlaypanel-demo">
                    <DataTable
                        lazy={true}
                        first={this.state.page * this.state.rows}
                        value={this.state.content}
                        className="p-datatable-customers"
                        dataKey="id"
                        selectionMode="single"

                        paginator={true}
                        rows={this.state.rows}
                        loading={this.state.loading}
                        onPage={this.onPage}
                        onSort={this.onSort}

                        emptyMessage="No event found"
                        totalRecords={this.state.total}
                        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} events"
                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                    >
                        <Column header="Event" field="name" body={(rowData) => this.nameTemplate(rowData)} />
                        <Column header="Type" field="eventType" />
                    </DataTable>
                </OverlayPanel>
            </>
        );
    }
}
