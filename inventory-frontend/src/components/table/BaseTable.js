import {PagingDataModelMapper} from "../../core/models/mapper/ModelMapper";
import {handleGetPage} from "../../core/handlers/ApiLoadContentHandler";
import {Toast} from "primereact/toast";
import React, {useRef} from "react";
import Table from "./Table";
import {SORT_ASC} from "./config";
import {Button} from "primereact/button";
import {
    capitalizeTheFirstLetterOfEachWord,
    getActionColumnConfig,
    getDateColumnConfig,
    getDefaultColumnConfig
} from "./TableUtil";

const BaseTable = (props) => {

    const {
        name,
        service,
        additionalColumns,
        additionalHeaders,
        Form
    } = props

    const columns = [
        getDefaultColumnConfig("code"),
        getDefaultColumnConfig("name"),
        ...additionalColumns,
        getDateColumnConfig("createAt", "Create from"),
        getDefaultColumnConfig("description"),
        getActionColumnConfig(
            (rowData) => props.history.push({
                pathname: `ingredient/${rowData.id}`
            }),
            (rowData) => [
                {
                    label: 'Edit',
                    icon: 'pi pi-pencil',
                    command: (e) => { form.current?.action(rowData.id, false) }
                }
            ]
        )
    ]

    const headers = (<>
        {additionalHeaders}
        <Button
            style={{ marginRight: '0.5rem' }}
            icon="pi pi-plus"
            iconPos="left"
            label={`New ${capitalizeTheFirstLetterOfEachWord(name)}`}
            onClick={() => form.current?.action(null, true)}
        />
    </>)

    const mapper = new PagingDataModelMapper();
    const toastInterval = 1000
    const toast = useRef(null);
    const form = useRef(null);

    const fetchData = (filter, pagination) => {
        const { page, rows, sortField, sortOrder } = pagination;
        return service.getData(filter, page, rows, sortField, sortOrder, false)
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
            <Form ref={form}
                  refreshData={fetchData}/>
            <Table columns={columns}
                   fetchData={fetchData}
                   filterLegend={capitalizeTheFirstLetterOfEachWord(name)}
                   onAfterResetFilter={onAfterResetFilter}
                   onAfterRefresh={onAfterRefresh}
                   onAfterSort={onAfterSort}
                   onAfterChangeSize={onAfterChangeSize}
                   onAfterChangePage={onAfterChangePage}
                   headerSection={headers}
                   headerSectionPosition="before"
            />
        </React.Fragment>
    )
}

export default BaseTable;