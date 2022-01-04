import {isJson} from "../../utility/ComponentUtility";

/**
 * Data model mapper for mapping and adapting pagination model from response
 */
export class PagingDataModelMapper {

    /**
     * Default value for model content
     * @type {[]}
     */
    static DEFAULT_CONTENT = []

    /**
     * Default value for model page number
     * @type {number}
     */
    static DEFAULT_PAGE = 0

    /**
     * Default value for model page size
     * @type {number}
     */
    static DEFAULT_SIZE = 0

    /**
     * Default value for model total element number
     * @type {number}
     */
    static DEFAULT_TOTAL = 0

    /**
     * Default value for model loading status
     * @type {boolean}
     */
    static DEFAULT_LOADING = false

    /**
     * Define a page data model
     * @returns {{total: number, content: *[], page: number, loading: boolean, rows: number}}
     */
    getDefaultModel() {
        return {
            content:    PagingDataModelMapper.DEFAULT_CONTENT,
            loading:    PagingDataModelMapper.DEFAULT_LOADING,
            total:      PagingDataModelMapper.DEFAULT_TOTAL,
            page:       PagingDataModelMapper.DEFAULT_PAGE,
            rows:       PagingDataModelMapper.DEFAULT_SIZE
        }
    }

    /**
     * Convert custom data response from API to data model
     * @param response Response data from service
     * @returns {{total: number, content: *[], page: number, loading: boolean, rows: number}}
     */
    toModel(response) {

        const data = response;
        if(!data || !isJson(data)) {
            console.warn("Data is null or not a JSON", data);
            return this.getDefaultModel();
        }

        /**
         * Extract total element from response data
         * @param data Response data from service
         * @returns {*|number|number}
         */
        const extractedTotal = (data) => {
            const dataTotalElements = data.totalElements;
            if(dataTotalElements && !isNaN(dataTotalElements) && dataTotalElements >= 0) {
                return dataTotalElements;
            }

            console.warn("Content total element is null or not a number or not non-negative", dataTotalElements);
            return PagingDataModelMapper.DEFAULT_TOTAL;
        }

        /**
         * Extract content from response data
         * @param data Response data from service
         * @returns {*[]|*}
         */
        const extractedContent = (data) => {
            const dataContent = data.content;
            if(dataContent && Array.isArray(dataContent)) {
                return dataContent;
            }

            console.warn("Content is null or not an array", dataContent);
            return PagingDataModelMapper.DEFAULT_CONTENT;
        }

        /**
         * Extract page number from response data
         * @param data Response data from service
         * @returns {*|number|number}
         */
        const extractedPage = (data) => {
            const contentPageable = data.pageable;
            if(contentPageable) {
                const pageNumber = contentPageable.pageNumber;
                if(pageNumber && !isNaN(pageNumber)) {
                    return pageNumber
                } else {
                    console.warn("Content page is null or not a number", pageNumber);
                }
            } else {
                console.warn("Content pageable is null", contentPageable)
            }

            return PagingDataModelMapper.DEFAULT_PAGE;
        }

        /**
         * Extract total size from response data
         * @param data Response data from service
         * @returns {*|number|number}
         */
        const extractedRows = (data) => {
            const contentPageable = data.pageable;
            if(contentPageable) {
                const pageSize = contentPageable.pageSize;
                if(pageSize && !isNaN(pageSize)) {
                    return pageSize
                } else {
                    console.warn("Content rows is null or not a number", pageSize);
                }
            } else {
                console.warn("Content pageable is null", contentPageable)
            }

            return PagingDataModelMapper.DEFAULT_SIZE;
        }

        let dataModel       = this.getDefaultModel();
        dataModel.content   = extractedContent(data);
        dataModel.total     = extractedTotal(data);
        dataModel.page      = extractedPage(data);
        dataModel.rows      = extractedRows(data);
        return dataModel;
    }
}

/**
 * Request mapper for mapping filter model to request
 */
export class FilterRequestMapper {

    /**
     * Default value for request page number
     * @type {number}
     */
    static DEFAULT_PAGE = 0

    /**
     * Default value for request page size
     * @type {number}
     */
    static DEFAULT_SIZE = 10

    /**
     * Default value for model request sort
     * @type {string}
     */
    static DEFAULT_SORT_FIELD = "id";


    static DEFAULT_SORT_ORDER = 1;

    /**
     * Convert all filter element to filter request
     * @param filter
     * @param page
     * @param size
     * @param sortField
     * @param sortOrder
     * @returns {{size: number, page: number, sort: string}}
     */
    toRequest(
        filter,
        page = FilterRequestMapper.DEFAULT_PAGE,
        size = FilterRequestMapper.DEFAULT_SIZE,
        sortField = FilterRequestMapper.DEFAULT_SORT_FIELD,
        sortOrder = FilterRequestMapper.DEFAULT_SORT_ORDER
    ) {
        const extractedSort = (extractedField, extractedOrder) => {
            const order = extractedOrder === 1 ? 'asc' : 'desc';
            return extractedField.length > 0 ? `${extractedField},${order}` : `id,${order}`;
        }

        const filterFields  = filter && isJson(filter) ? filter : {};
        const filterPage    = !isNaN(page) && page >= 0 ? page : FilterRequestMapper.DEFAULT_PAGE;
        const filterSize    = !isNaN(size) && size > 0 ? size : FilterRequestMapper.DEFAULT_SIZE;
        const filterSort = extractedSort(sortField, sortOrder);

        return {
            ...filterFields,
            page: filterPage,
            size: filterSize,
            sort: filterSort
        }
    }

}