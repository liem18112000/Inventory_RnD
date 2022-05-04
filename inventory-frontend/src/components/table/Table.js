import React, {useEffect, useState} from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputText } from 'primereact/inputtext';
import { SplitButton } from 'primereact/splitbutton';
import { Button } from "primereact/button";
import { Fieldset } from "primereact/fieldset";
import {
    DEFAULT_COLUMNS,
    DEFAULT_FILTER_FIELDSET_CONFIG,
    DEFAULT_PAGINATOR,
    DEFAULT_SIZE,
    DEFAULT_SORT,
    DEFAULT_TABLE_CONFIG,
    DEFAULT_TABLE_LENGTH_OPTIONS
} from "./config";
import { getFilterModel } from "./TableUtil";
import {Link} from "react-router-dom";

const Table = (props) => {
    const {
        columns = DEFAULT_COLUMNS,
        fetchData,

        mockData,
        onBeforeSort,
        onAfterSort,
        onBeforeChangePage,
        onAfterChangePage,
        onBeforeChangeSize,
        onAfterChangeSize,
        onBeforeRefresh,
        onAfterRefresh,
        onBeforeResetFilter,
        onAfterResetFilter,
        onBeforeResetSort,
        onAfterResetSort,

        filterLegend,
        headerSection,
        headerSectionPosition,

        getNavigateBackLink,
        navigateBackLabel,

        obtainFilter,
        obtainPagination,

        refreshTimestamp = new Date(),
        setRefreshTimestamp
    } = props;

    const [data, setData] = useState([]);

    const [paginator, setPaginator] = useState(DEFAULT_PAGINATOR);

    const [filter, setFilter] = useState(getFilterModel(columns));

    const [loading, setLoading] = useState(true);

    /**
     * Fetch data for the first time and when pagination or filter change
     */
    useEffect(() => {
        onFetchData()
        onObtainState()
    }, [JSON.stringify(paginator), JSON.stringify(filter), refreshTimestamp])

    /**
     * On obtain state
     */
    const onObtainState = () => {
        if (obtainFilter) {
            obtainFilter(filter)
        }
        if (obtainPagination) {
            obtainPagination(paginator)
        }
    }

    /**
     * On fetch data
     */
    const onFetchData = () => {
        setLoading(true)
        if (Array.isArray(mockData) && mockData.length > 0) {
            console.log("Mock data");
            setData(mockData);
            setLoading(false);
        } else {
            if (!fetchData) {
                console.error("fetchData is null");
                setLoading(false);
            } else {
                fetchData(filter, paginator).then(res => {
                    const {content, page, rows, total} = res
                    setData(content);
                    setPaginator({
                        ...paginator,
                        page: page,
                        total: total,
                        rows: rows
                    })
                }).then(() => setLoading(false));
            }
        }
    }

    /**
     * On reset filter
     */
    const onResetFilter = () => {
        if (onBeforeResetFilter) {
            onBeforeResetFilter(filter);
        }

        setFilter(getFilterModel(columns));

        if (onAfterResetFilter) {
            onAfterResetFilter(filter)
        }
    }

    /**
     * On change page
     * @param e event
     */
    const onChangePage = e => {
        const { page } = e
        if (onBeforeChangePage) {
            onBeforeChangePage(paginator.page);
        }
        setPaginator({
            ...paginator,
            page: page
        })
        if (onAfterChangePage) {
            onAfterChangePage(page)
        }
    };

    /**
     * On sort data
     * @param e event
     */
    const onSort = e => {
        const {sortField, sortOrder} = e;
        if (onBeforeSort) {
            onBeforeSort(sortField, sortOrder);
        }
        setPaginator({
            ...paginator,
            sortField: sortField,
            sortOrder: sortOrder
        });
        if (onAfterSort) {
            onAfterSort(sortField, sortOrder);
        }
    };

    /**
     * On reset sort
     */
    const onResetSort = () => {
        if (onBeforeResetSort) {
            onBeforeResetSort(paginator.sortField, paginator.sortOrder);
        }
        setPaginator({
            ...paginator,
            ...DEFAULT_SORT
        });
        if (onAfterResetSort) {
            onAfterResetSort(DEFAULT_SORT.sortField, DEFAULT_SORT.sortOrder);
        }
    }

    /**
     * On refresh data
     */
    const onRefresh = () => {
        if (onBeforeRefresh) {
            onBeforeRefresh(filter, paginator);
        }

        onResetFilter();
        onResetSort();
        setRefreshTimestamp(new Date());

        if (onAfterRefresh) {
            onAfterRefresh(filter, paginator)
        }
    }

    /**
     * On change table size
     * @param size table size
     */
    const onChangeSize = size => {
        if (onBeforeChangeSize) {
            onBeforeChangeSize(size);
        }
        setPaginator({
            ...paginator,
            rows: size > 0 ? size : DEFAULT_SIZE
        });
        if (onAfterChangeSize) {
            onAfterChangeSize(size);
        }
    };

    const renderFilterInputs = (filterInputs) => {
        if (!filterInputs) {
            return;
        }
        return filterInputs
            .map((col, index) => {
                const {
                    field,
                    filterConfig
                } = col;

                const {
                    InputComponent = InputText,
                    formatInput = (input) => input ? input : "",
                    inputProps = {},
                    getValueFromEvent = (event) => event.target.value
                } = filterConfig;

                const onFilterValueChange = (event) => {
                    let tempFilter = {...filter};
                    tempFilter[field] = formatInput(getValueFromEvent(event));
                    setFilter({...tempFilter})
                }

                return (
                    <div className="p-col-12 p-md-6 p-lg-6">
                        <InputComponent key={`filterInput-${index}`}
                                        value={filter[field]}
                                        onChange={(e) => onFilterValueChange(e)}
                                        {...inputProps}/>
                    </div>
                )
            })
    }

    const renderFilter = (legend, columns) => {
        const filterInputs = columns
            .filter(col => col.filterConfig && col.filterConfig.enabled);

        return (
            <Fieldset
                legend={legend ? legend : "Filter"}
                toggleable
                {...DEFAULT_FILTER_FIELDSET_CONFIG}>
                <div className="p-grid p-fluid">
                    {renderFilterInputs(filterInputs)}
                </div>
                <div className="p-d-flex p-jc-center">
                    <div className="p-mr-2">
                        <Button
                            key={"clear-filter"}
                            className="p-button-warning"
                            icon="pi pi-filter-slash"
                            iconPos="left"
                            label="Clear filter"
                            onClick={onResetFilter}
                        />
                    </div>

                    <div className="p-mr-2">
                        <Button
                            key={"clear-sort"}
                            icon="pi pi-sort-alt"
                            iconPos="left"
                            label="Clear sort"
                            onClick={onResetSort}
                        />
                    </div>
                </div>
            </Fieldset>
        )
    }

    const renderHeader = (additionalHeader, position = "before") => {
        const tableLengthOptions = DEFAULT_TABLE_LENGTH_OPTIONS
            .map((option, index) => ({
                key: `option-${index}`,
                label: `${option} items / pages`,
                command: () => onChangeSize(option)
            }))
        return (
            <React.Fragment>
                <div className="table-header" >
                    <span className="p-input-icon-left">
                        {position === "before" && additionalHeader}
                        <SplitButton className="table-control-length p-button-contrast"
                                     label={window.innerWidth > 960 ? "Refresh" : ""}
                                     icon="pi pi-refresh"
                                     onClick={onRefresh}
                                     model={tableLengthOptions}>
                        </SplitButton>
                        {position === "after" && additionalHeader}
                    </span>
                    {navigateBackLabel && getNavigateBackLink && <span
                        className="p-input-icon-left"
                        style={{ fontSize: "17px" }}>
                        <Link to={getNavigateBackLink(props)}>
                            {navigateBackLabel}
                        </Link>
                    </span>}
                </div>
            </React.Fragment>
        )
    }

    const renderColumns = (columns) => {
        if (columns) {
            return columns.map((col, index) => {
                const {
                    field,
                    header = field.length > 0 ? field.charAt(0).toUpperCase() + field.slice(1) : "Header",
                    sortField = field,
                    filterField = field,
                    columnConfig = {}
                } = col;
                return (
                    <Column key={`column-${index}`}
                            field={field}
                            sortField={sortField}
                            filterField={filterField}
                            header={header}
                            style={{ flexGrow: 1, flexBasis: '200px' }}
                            {...columnConfig} />
                )
            })
        }
    }

    return (
        <div className="datatable-doc-demo">
            {renderFilter(filterLegend, columns)}
            <DataTable lazy={true}
                       first={paginator.page * paginator.rows}
                       value={data}
                       loading={loading}
                       header={renderHeader(headerSection, headerSectionPosition)}
                       className="p-datatable-customers"
                       onPage={onChangePage}
                       onSort={onSort}
                       rows={paginator.rows}
                       totalRecords={paginator.total}
                       sortField={paginator.sortField}
                       sortOrder={paginator.sortOrder}
                       {...DEFAULT_TABLE_CONFIG}>
                {renderColumns(columns)}
            </DataTable>
        </div>
    );
}

export default Table;