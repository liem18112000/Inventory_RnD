import {PagingDataModelMapper} from "../../core/models/mapper/ModelMapper";
import {handleGetPage} from "../../core/handlers/ApiLoadContentHandler";
import {Toast} from "primereact/toast";
import React, {useRef, useState} from "react";
import Table from "./Table";
import {DEFAULT_PAGINATOR, DEFAULT_TOAST_INTERVAL, SORT_ASC} from "./config";
import {Button} from "primereact/button";
import {
    capitalizeTheFirstLetterOfEachWord,
    getActionColumnConfig,
    getDefaultColumnConfig, getFilterModel
} from "./TableUtil";

const BaseTable = (props) => {

    const {
        name,
        service,
        additionalColumns,
        additionalHeaders,
        headerSectionPosition = "before",
        toastInterval = DEFAULT_TOAST_INTERVAL,

        Form,
        formProps = {},

        navigateViewLabel = "View",
        getNavigateViewLink,
        getAdditionalActionItems,

        getNavigateBackLink,
        navigateBackLabel = "Back",

        obtainFilter
    } = props

    const [formVisible, setFormVisible] = useState(false);

    const [formDataId, setFormDataId] = useState(null);

    const [refreshTimestamp, setRefreshTimestamp] = useState(new Date());

    const toast = useRef(null);

    const actionItemsModel = (rowData, getActionItems) => {
        const actionItems = getActionItems ? getActionItems(rowData,
            () => setRefreshTimestamp(new Date())) : [];
        if (actionItems) {
            return [
                {
                    key: "edit-option",
                    label: 'Edit',
                    icon: 'pi pi-pencil',
                    command: (e) => {
                        setFormDataId(rowData.id);
                        setFormVisible(true)
                    }
                },
                ...actionItems
            ]
        }

        return [{
            label: 'Edit',
            icon: 'pi pi-pencil',
            command: (e) => {
                setFormDataId(rowData.id);
                setFormVisible(true)
            }
        }]
    }

    const columns = [
        getDefaultColumnConfig("code"),
        getDefaultColumnConfig("name"),
        ...additionalColumns,
        getDefaultColumnConfig("description"),
        getActionColumnConfig(
            rowData => props.history.push({ pathname: getNavigateViewLink ? getNavigateViewLink(rowData) : "#" }),
            rowData => actionItemsModel(rowData, getAdditionalActionItems),
            navigateViewLabel
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
        return service.getData(filter, page, rows, sortField, sortOrder)
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
                label={window.innerWidth > 768 ? `New ${capitalizeTheFirstLetterOfEachWord(name)}` : ""}
                onClick={() => {
                    setFormDataId(null);
                    setFormVisible(true)
                }}
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

    /**
     * On form submit refresh
     */
    const onFormSubmitRefresh = () => {
        setRefreshTimestamp(new Date());
    }

    return (
        <React.Fragment>
            <Toast ref={toast} />
            <Form id={formDataId}
                visible={formVisible}
                setVisible={setFormVisible}
                refreshData={onFormSubmitRefresh}
                {...formProps} />
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
                getNavigateBackLink={getNavigateBackLink}
                navigateBackLabel={navigateBackLabel}
                refreshTimestamp={refreshTimestamp}
                setRefreshTimestamp={setRefreshTimestamp}
                obtainFilter={obtainFilter}
            />
        </React.Fragment>
    )
}

export default BaseTable;
