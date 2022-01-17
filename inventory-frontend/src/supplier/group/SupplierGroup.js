import React, { useState, useEffect, useRef } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import '../../assets/styles/TableDemo.css';
import { SplitButton } from 'primereact/splitbutton';
import moment from 'moment';
import { Toast } from 'primereact/toast';
import { InputText } from 'primereact/inputtext';
import { Fieldset } from 'primereact/fieldset';
import { PagingDataModelMapper } from '../../core/models/mapper/ModelMapper';
import { handleGetPage } from '../../core/handlers/ApiLoadContentHandler';
import { SupplierService } from "../../service/SupplierService";

export const SupplierGroup = () => {

    const [content, setContent] = useState([]);
    const [page, setPage] = useState(0);
    const [rows, setRows] = useState(10);
    const [total, setTotal] = useState(0);
    const [sortField, setSortField] = useState("");
    const [sortOrder, setSortOrder] = useState(0);
    const [filter, setFilter] = useState({
        name: "",
        code: ""
    });
    const isMock = false;
    const [loading, setLoading] = useState(false);

    const datatable = useRef(null);
    const toast = useRef(null);
    const mapper = new PagingDataModelMapper();

    useEffect(() => {
        setLoading(true);
        getPage();
    }, []);

    /**
     * Call get page supplier group API
     */
    const getPage = () => {
        SupplierService
            .getPageGroup(filter, page, rows, sortField, sortOrder, isMock)
            .then(data => handleGetPage(data))
            .then(data => mapper.toModel(data))
            .then(data => {
                setContent(data.content);
                setLoading(data.loading);
                setTotal(data.total);
                setPage(data.page);
                setRows(data.rows);
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

    const actionBodyTemplate = (rowData, form) => {
        let items = [
            {
                label: 'Edit',
                icon: 'pi pi-pencil',
                command: (e) => { form.action(rowData.id, isMock) }
            },
        ];

        return (
            <React.Fragment>
                <span className="p-column-title">Action</span>
                <div className="card">
                    <SplitButton label="View" onClick={() => window.location.replace(
                        `supplier/${rowData.id}`
                    )} model={items}></SplitButton>
                </div>
            </React.Fragment>
        );
    }

    /**
     * Rest stat of supplier group filter
     */
    const resetFilter = () => {
        setLoading(true);
        setFilter({
            name: '',
            code: ''
        });
        getPage();
        toast.current.show({
            severity: 'info',
            summary: 'Clear',
            detail: 'Clear filter',
            life: 1000
        });
    }

    /**
     * Apply supplier group filter fields
     */
    const applyFilter = () => {
        setLoading(true);
        getPage();
        toast.current.show({
            severity: 'info',
            summary: 'Search',
            detail: 'Search data content',
            life: 1000
        });
    }

    /**
     * Reset page length
     * @param e
     */
    const onPage = e => {
        setLoading(true);
        setPage(e.page);
        getPage();
    };

    /**
     * Change page on datatable
     * @param e
     */
    const onSort = e => {
        setLoading(true);
        setSortField(e.sortField);
        setSortOrder(e.sortOrder);
        getPage();
        toast.current.show({
            severity: 'info',
            summary: 'Sort',
            detail: 'Sort ' + e.sortField + " " + (e.sortOrder === 1 ? "ascending" : "descending"),
            life: 1000
        });
    };

    /**
     * Refresh data table
     */
    const onRefresh = () => {
        setLoading(true);
        getPage()
        toast.current.show({
            severity: 'info',
            summary: 'Refresh',
            detail: 'Refresh datatable',
            life: 1000
        });
    }

    /**
     * Reset page length
     * @param l page length
     */
    const onChangePageLength = l => {
        setLoading(true);
        setPage(0);
        getPage();
        toast.current.show({
            severity: 'info',
            summary: 'Reset page size',
            detail: 'Page size is set to ' + l,
            life: 1000
        });
    };

    const renderHeader = () => {
        const tableLengthOptions = [
            {
                label: '5',
                command: () => {
                    setRows(5);
                    onChangePageLength(5);
                }
            },
            {
                label: '10',
                command: () => {
                    onChangePageLength(10);
                }
            },
            {
                label: '25',
                command: () => {
                    onChangePageLength(25);
                }
            },
            {
                label: '50',
                command: () => {
                    onChangePageLength(50);
                }
            },
            {
                label: '100',
                command: () => {
                    onChangePageLength(100);
                }
            }
        ];

        return (
            <div className="table-header">
                <span className="p-input-icon-left">
                    <i className="pi pi-plus" />
                    <Button
                        className="blue-btn"
                        style={{ marginRight: '0.5rem' }}
                        icon="pi pi-plus"
                        iconPos="left"
                        label="New group"
                    // onClick={() => this.form.action(null, true)}
                    />
                    <SplitButton
                        className="table-control-length p-button-constrast" label="Refresh" icon="pi pi-refresh"
                        onClick={onRefresh}
                        model={tableLengthOptions}>
                    </SplitButton>
                </span>
            </div>
        );
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
                                            value={filter.name}
                                            onChange={(e) => setFilter({ ...filter, name: e.target.value })}
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
                                            value={filter.code}
                                            onChange={(e) => setFilter({ ...filter, code: e.target.value })}
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
                <DataTable ref={datatable}
                    lazy={true}
                    first={page * rows}
                    value={content}
                    loading={loading}
                    header={renderHeader()}
                    className="p-datatable-customers"
                    dataKey="id"
                    rowHover

                    // ---Paginator--- 
                    paginator={true}
                    onPage={onPage}
                    onSort={onSort}
                    rows={rows}
                    totalRecords={total}
                    // ---Paginator--- 
                    sortField={sortField}
                    sortOrder={sortOrder}

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

