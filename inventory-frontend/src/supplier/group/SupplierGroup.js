import React, { useState, useEffect, useRef } from 'react';
import { classNames } from 'primereact/utils';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import '../../assets/styles/TableDemo.css';
import { SplitButton } from 'primereact/splitbutton';
import moment from 'moment';
import { Toast } from 'primereact/toast';
import { InputText } from 'primereact/inputtext';
import { Fieldset } from 'primereact/fieldset';
import { IngredientService } from '../../service/IngredientService';
import { PagingDataModelMapper } from '../../core/models/mapper/ModelMapper';
import { handleGetPage } from '../../core/handlers/ApiLoadContentHandler';

export const SupplierGroup = () => {
    const [content, setContent] = useState([]);
    const [page, setPage] = useState(0);
    const [rows, setRows] = useState(10);
    const [total, setTotal] = useState(0);
    const [sortField, setSortField] = useState(null);
    const [sortOrder, setSortOrder] = useState(0);
    const [filter, setFilter] = useState({
        name: "",
        code: "",
        description: "",
        createAt: "",
    });
    const [isMock, setIsMock] = useState(false);
    const [loading, setLoading] = useState(false);

    const dt = useRef(null);
    const toast = useRef(null);

    // const ingredientService = new IngredientService();
    const mapper = new PagingDataModelMapper();

    useEffect(() => {
        // setLoading(true);
        // getPageCategories()
    const ingredientService = new IngredientService();
        ingredientService
            .getPageCategory(filter, page, rows, sortField, sortOrder, isMock)
            .then(data => handleGetPage(data))
            .then(data => mapper.toModel(data))
            .then(data => {
                console.log(data);
                setContent(data.content)
                setLoading(data.loading)
                setTotal(data.total)
                setPage(data.page)
                setRows(data.rows)
            })
        }); 

    /**
     * Call get page ingredient category API
     */
    const getPageCategories = () => {
        ingredientService
            .getPageCategory(filter, page, rows, sortField, sortOrder, isMock)
            .then(data => handleGetPage(data))
            .then(data => mapper.toModel(data))
            // .then(data => this.setState({ ...this.state, ...data}));
            .then(data => {
                console.log(data);
                setContent(data.content)
                setLoading(data.loading)
                setTotal(data.total)
                setPage(data.page)
                setRows(data.rows)
            })
    }

    const nameBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                <span className="p-column-title">Name</span>
                {rowData.name}
            </React.Fragment>
        );
    }

    const codeBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                <span className="p-column-title">Code</span>
                {rowData.code}
            </React.Fragment>
        );
    }

    const descriptionBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                <span className="p-column-title">Description</span>
                <span>{rowData.description}</span>
            </React.Fragment>
        );
    }

    const createAtBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                <span className="p-column-title">Create At</span>
                <span style={{ verticalAlign: 'middle' }}>{moment(rowData.createAt).format('HH:mm A ddd DD/MMM/YYYY')}</span>
            </React.Fragment>
        );
    }

    const updateAtBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                <span className="p-column-title">Update At</span>
                <span style={{ verticalAlign: 'middle' }}>{moment(rowData.updateAt).format('HH:mm A ddd DD/MMM/YYYY')}</span>
            </React.Fragment>
        );
    }

    const actionBodyTemplate = () => {
        return (
            <Button type="button" icon="pi pi-cog" className="p-button-secondary"></Button>
        );
    }

    const renderHeader = () => {
        return (
            <div className="table-header">
                <span className="p-input-icon-left">
                    <i className="pi pi-plus" />
                    <Button
                        className="blue-btn"
                        style={{ marginRight: '0.5rem' }}
                        icon="pi pi-plus"
                        iconPos="left"
                        label="New category"
                    // onClick={() => this.form.action(null, true)}
                    />
                    <SplitButton className="table-control-length p-button-constrast" label="Refresh" icon="pi pi-refresh"
                    // onClick={this.onRefresh}
                    // model={tableLengthOptions}
                    >
                    </SplitButton>
                </span>
            </div>
        );
    }

    const header = renderHeader();

    /**
     * Rest stat of ingredient category filter
     */
    const resetFilter = () => {
        // this.setState({
        //     ...this.state,
        //     filter: {
        //         name: "",
        //         code: "",
        //         description: "",
        //         createAt: "",
        //     }
        // }, () => {
        //     this.setState({ loading: true });
        //     this.getPageCategories();
        //     this.toast.show({ severity: 'info', summary: 'Clear', detail: 'Clear filter', life: 1000 });
        // })
    }

    /**
     * Apply ingredient category filter fields
     */
    const applyFilter = () => {
        setLoading(true);
        // getPageCategories();
        toast.current.show({ severity: 'info', summary: 'Search', detail: 'Search data content', life: 1000 });
    }

    return (
        <div className="datatable-doc-demo">
            <Toast ref={toast} />
            <div className="card">
                <Fieldset legend="Supplier Group" toggleable>
                    <div className="p-grid p-fluid">
                        <div className="p-col-12 p-md-6">
                            <div className="p-grid">
                                <div className="p-col-12">
                                    <div className="p-inputgroup">
                                        <InputText
                                            placeholder="Name"
                                            value={filter.code}
                                            onChange={(e) => setFilter({ ...filter, code: e.target.value })}
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
                                            value={filter.description}
                                            onChange={(e) => setFilter({ ...filter, description: e.target.value })}
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
                                onClick={() => applyFilter()} />
                        </div>
                        <div>
                            <Button
                                className="p-button-warning"
                                icon="pi pi-trash"
                                iconPos="left"
                                label="Clear"
                                onClick={() => resetFilter()}
                            />
                        </div>
                    </div>
                </Fieldset>
                <DataTable ref={dt}
                    lazy={true}
                    // first={page * rows}
                    // value={content}
                    // loading={loading}
                    header={header}
                    className="p-datatable-customers"
                    dataKey="id"
                    rowHover

                    // ---Paginator--- 
                    // paginator={true}
                    // onPage={this.onPage}
                    // onSort={this.onSort}
                    // rows={this.state.rows}
                    // totalRecords={this.state.total}
                    // ---Paginator--- 
                    // sortField={this.state.sortField}
                    // sortOrder={this.state.sortOrder}

                    emptyMessage="No ingredient categories found"
                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                >
                    <Column field="name" header="Name" body={nameBodyTemplate} sortable />
                    <Column field="code" header="Code" body={codeBodyTemplate} sortable />
                    <Column field="description" header="Description" body={descriptionBodyTemplate} sortable />
                    <Column field="createAt" header="Create At" body={createAtBodyTemplate} sortable />
                    <Column field="updateAt" header="Update At" body={updateAtBodyTemplate} sortable />
                    <Column header="Action" body={(rowData) => actionBodyTemplate(rowData)} />
                </DataTable>
            </div>
        </div>
    );
}