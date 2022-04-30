const SORT_ASC = 0;
const SORT_DESC = 1;
const DEFAULT_SIZE = 10;
const DEFAULT_PAGE = 0;
const DEFAULT_PAGINATOR = {
    page: DEFAULT_PAGE,
    rows: DEFAULT_SIZE,
    total: 0,
    sortField: "",
    sortOrder: SORT_ASC,
}
const DEFAULT_TABLE_LENGTH_OPTIONS = [
    5, 10, 25, 50, 100, 200, 1000
]

const DEFAULT_TABLE_CONFIG = {
    dataKey: "id",
    scrollHeight: "calc(85vh - 200px)",
    paginator: true,
    emptyMessage: "No data found",
    currentPageReportTemplate: "Showing {first} to {last} of {totalRecords} entries",
    paginatorTemplate: "FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
}

const DEFAULT_COLUMNS = [
    {
        field: "name",
        filterConfig: {
            enabled: true,
            defaultValue: ""
        },
        columnConfig: {
            sortable: true,
        }
    },
    {
        field: "description",
        filterConfig: {
            enabled: true,
            defaultValue: ""
        },
        columnConfig: {
            sortable: true,
        }
    }
]

const DEFAULT_FILTER_FIELDSET_CONFIG = {
    toggleable: true,
    collapsed: true
}

export {
    SORT_DESC,
    SORT_ASC,
    DEFAULT_SIZE,
    DEFAULT_PAGE,
    DEFAULT_PAGINATOR,
    DEFAULT_TABLE_LENGTH_OPTIONS,
    DEFAULT_TABLE_CONFIG,
    DEFAULT_COLUMNS,
    DEFAULT_FILTER_FIELDSET_CONFIG
}