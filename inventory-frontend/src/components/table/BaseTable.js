import {PagingDataModelMapper} from "../../core/models/mapper/ModelMapper";
import {handleGetPage} from "../../core/handlers/ApiLoadContentHandler";
import {Toast} from "primereact/toast";
import React, {useEffect, useRef} from "react";
import Table from "./Table";
import {DEFAULT_PAGINATOR, DEFAULT_SORT, DEFAULT_TOAST_INTERVAL, SORT_ASC} from "./config";
import {Button} from "primereact/button";
import {
    capitalizeTheFirstLetterOfEachWord,
    getActionColumnConfig,
    getDateColumnConfig,
    getDefaultColumnConfig, getFilterModel
} from "./TableUtil";

const BaseTable = (props) => {

    const {
        name,
        service,
        additionalColumns,
        additionalHeaders,
        headerSectionPosition = "before",
        Form,
        toastInterval = DEFAULT_TOAST_INTERVAL
    } = props

    const toast = useRef(null);
    const form = useRef(null);

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

    /**
     * Fetch data with filter and pagination
     * @param filter Data filter
     * @param pagination Data pagination
     * @returns {*}
     */
    const fetchData = (filter = getFilterModel(columns), pagination = DEFAULT_PAGINATOR) => {
        const { page, rows, sortField, sortOrder } = pagination;
        const mapper = new PagingDataModelMapper();
        return service.getData(filter, page, rows, sortField, sortOrder, false)
            .then(data => handleGetPage(data, toast.current))
            .then(data => mapper.toModel(data))
    }

    /**
     * Render headers
     * @param additionalHeaders headers
     * @returns {JSX.Element}
     */
    const renderHeader = (additionalHeaders) => {
        return (<>
            {additionalHeaders}
            <Button
                style={{ marginRight: '0.5rem' }}
                icon="pi pi-plus"
                iconPos="left"
                label={`New ${capitalizeTheFirstLetterOfEachWord(name)}`}
                onClick={() => form.current?.action(null, true)}
            />
        </>)
    }

    /**
     * After refresh handler
     */
    const onAfterRefresh = () => {
        toast.current.show({ severity: 'success', summary: 'Refresh', detail: 'Refresh table', life: toastInterval });
    }

    /**
     * After reset filter handler
     * @param filter
     */
    const onAfterResetFilter = (filter) => {
        toast.current.show({ severity: 'info', summary: 'Reset', detail: 'Reset filter', life: toastInterval });
    }

    /**
     * After reset sort
     * @param sortOrder
     * @param sortField
     */
    const onAfterResetSort = (sortOrder, sortField) => {
        toast.current.show({ severity: 'info', summary: 'Reset', detail: 'Reset sort', life: toastInterval });
    }

    /**
     * After sort handler
     * @param sortOrder
     * @param sortField
     */
    const onAfterSort = (sortField, sortOrder) => {
        toast.current.show({
            severity: 'info',
            summary: 'Sort',
            detail: `Sort ${sortField} ${sortOrder === SORT_ASC ? "ascending" : "descending"}`,
            life: toastInterval
        });
    }

    /**
     * After change page handler
     */
    const onAfterChangePage = (page) => {
        toast.current.show({
            severity: 'info',
            summary: 'Page',
            detail: `Page ${page + 1} fetched`,
            life: toastInterval
        });
    }

    /**
     * After change size handler
     */
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
                   onAfterResetSort={onAfterResetSort}
                   onAfterRefresh={onAfterRefresh}
                   onAfterSort={onAfterSort}
                   onAfterChangeSize={onAfterChangeSize}
                   onAfterChangePage={onAfterChangePage}
                   headerSection={renderHeader(additionalHeaders)}
                   headerSectionPosition={headerSectionPosition}
            />
        </React.Fragment>
    )
}

export default BaseTable;