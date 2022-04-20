import React, { Component } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { SplitButton } from 'primereact/splitbutton';
import { Fieldset } from 'primereact/fieldset';
import { RecipeService } from '../../service/RecipeService'

import '../../assets/styles/TableDemo.css';
import moment from 'moment';
import { Button } from "primereact/button";
import 'primeflex/primeflex.css';
import { RecipeGroupForm } from './RecipeGroupForm';
import { Toast } from 'primereact/toast';
import { handleGetPage } from "../../core/handlers/ApiLoadContentHandler";
import { confirmDialog } from 'primereact/confirmdialog';
import { PagingDataModelMapper } from "../../core/models/mapper/ModelMapper";
import { Calendar } from 'primereact/calendar';
import { convertDateToEnCADate } from '../../core/utility/ComponentUtility';

export class RecipeGroup extends Component {

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
                description: "",
                updatedAt: "",
            },
            isMock: false,
            loading: false
        };
        this.history = props.history
        this.recipeService = new RecipeService();
        this.mapper = new PagingDataModelMapper();
    }

    componentDidMount() {
        this.setState({ loading: true });
        this.getPageGroups()
    };

    getPageGroups = () => {
        const { filter, page, rows, sortField, sortOrder, isMock } = this.state;
        this.recipeService
            .getPageGroup(filter, page, rows, sortField, sortOrder, isMock)
            .then(data => handleGetPage(data, this.toast))
            .then(data => this.mapper.toModel(data))
            .then(data => this.setState({ ...this.state, ...data }));
    }

    descriptionBodyTemplate(rowData) {
        return (
            <React.Fragment>
                <span>{rowData.description}</span>
            </React.Fragment>
        );
    }

    deleteRecipe(recipeId) {
        if (!recipeId) {
            this.toast.show({
                severity: 'warning', summary: 'Delete failed',
                detail: 'Recipe id is not set', life: 3000
            })
        } else {
            this.recipeService.deleteRecipe(recipeId, this.state.isMock)
                .then(res => {
                    if (res) {
                        this.toast.show({
                            severity: 'success', summary: 'Delete success',
                            detail: 'Recipe group has been deleted', life: 1000
                        })
                        this.getPageGroups()
                    } else {
                        this.toast.show({
                            severity: 'error', summary: 'Delete failed',
                            detail: 'Recipe group may has recipe child or recipe detail.', life: 5000
                        })
                    }
                })
        }
    }

    confirmDelete(rowData) {
        confirmDialog({
            message: 'Do you want to delete this group?',
            header: 'Delete Confirmation',
            icon: 'pi pi-info-circle',
            acceptClassName: 'p-button-danger',
            accept: () => this.deleteRecipe(rowData.id, this.state.isMock),
            reject: () => this.toast.show({
                severity: 'info', summary: 'Cancel delete',
                detail: 'You have cancel delete', life: 1000
            })
        });
    }

    actionBodyTemplate(rowData, form) {
        let items = [
            {
                label: 'Edit',
                icon: 'pi pi-pencil',
                command: (e) => { form.action(rowData.id, this.state.isMock) }
            },
            {
                label: 'Delete',
                icon: 'pi pi-trash',
                command: (e) => this.confirmDelete(rowData)
            }
        ];
        return (
            <React.Fragment>
                <div className="card">
                    <SplitButton label="View"
                        onClick={() => this.props.history.push({
                            pathname: `recipe/${rowData.id}`,
                            state: { groupName: rowData.name }
                        })} model={items}
                    >
                    </SplitButton>
                </div>
            </React.Fragment>
        );
    }

    codeBodyTemplate(rowData) {
        return (
            <React.Fragment>
                {rowData.code}
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
                <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>{moment(rowData.updateAt).format('HH:mm-A-ddd-DD/MMM/YYYY')}</span>
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
                name: "",
                code: "",
                description: "",
                updatedAt: "",
            }
        }, () => {
            this.setState({ loading: true });
            this.getPageGroups();
            this.toast.show({ severity: 'info', summary: 'Clear', detail: 'Clear filter', life: 3000 });
        })
    }

    applyFilter = () => {
        this.setState({ loading: true });
        this.getPageGroups();
        this.toast.show({ severity: 'info', summary: 'Search', detail: 'Search data content', life: 3000 });
    }

    onPage = e => {
        this.setState(
            {
                loading: true,
                page: e.page
            },
            () => {
                this.getPageGroups();
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
                this.getPageGroups();
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
            this.getPageGroups();
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
                this.applyFilter();
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
                <RecipeGroupForm ref={el => this.form = el}
                    refreshData={() => this.getPageGroups()}
                />
                <Fieldset legend="Recipe Groups" toggleable>
                    <div className="p-grid p-fluid">
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
                                <div className="p-col-12">
                                    <div className="p-inputgroup">
                                        <Calendar
                                            dateFormat="yy-mm-dd"
                                            placeholder="Update From"
                                            id="basic" value={this.state.filter.updatedAt}
                                            onChange={(e) => this.setFilter({ ...this.state.filter, updatedAt: convertDateToEnCADate(e.target.value) })} />
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

                    emptyMessage="No recipe categories found"
                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                // paginatorPosition="top,bottom"
                >
                    <Column field="code" header="Code" body={this.codeBodyTemplate} sortable />
                    <Column field="name" header="Name" body={this.nameBodyTemplate} sortable />
                    <Column field="updateAt" header="Updated From" body={this.updatedAtBodyTemplate} sortable />
                    <Column field="description" header="Description" body={this.descriptionBodyTemplate} sortable />
                    <Column header="Action" body={(rowData) => this.actionBodyTemplate(rowData, this.form)} />
                </DataTable>
            </div >
        );
    }
}