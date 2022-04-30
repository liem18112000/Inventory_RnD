import {IngredientService} from "../../service/IngredientService";
import {PagingDataModelMapper} from "../../core/models/mapper/ModelMapper";
import {handleGetPage} from "../../core/handlers/ApiLoadContentHandler";
import {Toast} from "primereact/toast";
import React, {useRef} from "react";
import Table from "./Table";
import {SORT_ASC} from "./config";

const TableExample = (props) => {
    const columns = [
        {
            field: "code",
            filterConfig: {
                enabled: true,
                defaultValue: "",
                inputProps: {
                    placeholder: "Code"
                }
            },
            columnConfig: {
                sortable: true,
            }
        },
        {
            field: "name",
            filterConfig: {
                enabled: true,
                defaultValue: "",
                inputProps: {
                    placeholder: "Name"
                }
            },
            columnConfig: {
                sortable: true,
            }
        },
        {
            field: "createAt",
            sortable: true,
            filterConfig: {
                enabled: true,
                defaultValue: "",

                inputProps: {
                    placeholder: "Create From"
                }
            },
            columnConfig: {
                sortable: true,
            }
        },
        {
            field: "description",
            sortable: true,
            filterConfig: {
                enabled: true,
                defaultValue: "",
                inputProps: {
                    placeholder: "Description"
                }
            },
            columnConfig: {
                sortable: true,
            }
        }
    ]
    const service = new IngredientService();
    const mapper = new PagingDataModelMapper();
    const toast = useRef(null);
    const toastInterval = 1000

    const fetchData = (filter, pagination) => {
        const { page, rows, sortField, sortOrder } = pagination;
        return service
            .getPageCategory(filter, page, rows, sortField, sortOrder, false)
            .then(data => handleGetPage(data, toast.current))
            .then(data => mapper.toModel(data))
    }

    const onAfterRefresh = () => {
        toast.current.show({ severity: 'success', summary: 'Refresh', detail: 'Refresh table', life: toastInterval });
    }

    const onAfterResetFilter = () => {
        toast.current.show({ severity: 'info', summary: 'Reset', detail: 'Reset filter', life: toastInterval });
    }

    const onAfterSort = (sortField, sortOrder) => {
        toast.current.show({
            severity: 'info',
            summary: 'Sort',
            detail: `Sort ${sortField} ${sortOrder === SORT_ASC ? "ascending" : "descending"}`,
            life: toastInterval
        });
    }

    const onAfterChangePage = (page) => {
        toast.current.show({
            severity: 'info',
            summary: 'Page',
            detail: `Page ${page + 1} fetched`,
            life: toastInterval
        });
    }

    const onAfterChangeSize = (size) => {
        toast.current.show({
            severity: 'info',
            summary: 'Size',
            detail: `Page size ${size} items / page`,
            life: toastInterval
        });
    }

    return (
        <React.Fragment>
            <Toast ref={toast} />
            <Table columns={columns}
                   fetchData={fetchData}
                   filterLegend={"Ingredient Category"}
                   onAfterResetFilter={onAfterResetFilter}
                   onAfterRefresh={onAfterRefresh}
                   onAfterSort={onAfterSort}
                   onAfterChangeSize={onAfterChangeSize}
                   onAfterChangePage={onAfterChangePage}
            />
        </React.Fragment>
    )
}

export default TableExample;