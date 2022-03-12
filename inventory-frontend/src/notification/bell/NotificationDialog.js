import React, { Component } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import 'primeflex/primeflex.css';
import { handleGetPage } from "../../core/handlers/ApiLoadContentHandler";
import { PagingDataModelMapper } from "../../core/models/mapper/ModelMapper";
import { NotificationService } from "../../service/NotificationService";
import { OverlayPanel } from 'primereact/overlaypanel';
import { Badge } from 'primereact/badge';

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

    /**
     * Category name body template
     * @param rowData event data row
     * @returns {JSX.Element}
     */
    nameBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span className="p-column-title">Name</span>
                {rowData.name}
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
                    <Badge value={this.state.total} ></Badge>
                </i>
                <OverlayPanel ref={(el) => this.op = el} showCloseIcon id="overlay_panel" style={{ width: '450px' }} className="overlaypanel-demo">
                    <DataTable
                        value={this.state.content}
                        selectionMode="single"
                        paginator rows={5}
                    >
                        <Column field="name" />
                    </DataTable>
                </OverlayPanel>
            </>
        );
    }
}
