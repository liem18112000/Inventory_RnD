/**
 * Conformist data model for mapping and adapting page and filter
 * model to frontend.
 */
import {isJson} from "../../utility/ComponentUtility";

export default class PagingAndSortingDataModel {

    /**
     * Define a page data model
     * @returns {{total: number, content: *[], page: number, loading: boolean, rows: number}}
     */
    getDefaultDataModel() {
        return {
            content: [],
            loading: false,
            total: 0,
            page: 0,
            rows: 0
        }
    }

    /**
     * Convert custom data response from API to data model
     * @param inputData Response data from service
     * @returns {{total: number, content: *[], page: number, loading: boolean, rows: number}}
     */
    getDataModel(inputData) {

        const data = inputData;
        if(!data || !isJson(data)) {
            console.warn("Data is null or not a JSON", data);
            return this.getDefaultDataModel();
        }

        let dataModel = this.getDefaultDataModel();
        dataModel.content = this.extractedContent(data);
        dataModel.total = this.#extractedTotal(data);
        dataModel.page = this.extractedPage(data);
        dataModel.size = this.extractedSize(data);
        return dataModel;
    }

    /**
     * Extract total element from response data
     * @param data Response data from service
     * @returns {*|number|number}
     */
    #extractedTotal(data) {
        const dataTotalElements = data.totalElements;
        if(dataTotalElements && !isNaN(dataTotalElements) && dataTotalElements >= 0) {
            return dataTotalElements;
        }

        console.warn("Content total element is null or not a number or not non-negative", dataTotalElements);
        return 0;
    }

    /**
     * Extract content from response data
     * @param data Response data from service
     * @returns {*[]|*}
     */
    extractedContent(data) {
        const dataContent = data.content;
        if(dataContent && Array.isArray(dataContent)) {
            return dataContent;
        }

        console.warn("Content is null or not an array", dataContent);
        return [];
    }

    /**
     * Extract page number from response data
     * @param data Response data from service
     * @returns {*|number|number}
     */
    extractedPage(data) {
        const contentPageable = data.pageable;
        if(contentPageable) {
            const pageNumber = contentPageable.pageNumber;
            if(pageNumber && !isNaN(pageNumber) && pageNumber >= 0) {
                return pageNumber
            } else {
                console.warn("Content page number is null or not a number or not non-negative", pageNumber);
            }
        } else {
            console.warn("Content pageable is null", contentPageable)
        }

        return 0;
    }

    /**
     * Extract total size from response data
     * @param data Response data from service
     * @returns {*|number|number}
     */
    extractedSize(data) {
        const contentPageable = data.pageable;
        if(contentPageable) {
            const pageSize = contentPageable.pageNumber;
            if(pageSize && !isNaN(pageSize) && pageSize >= 0) {
                return pageSize
            } else {
                console.warn("Content page size is null or not a number or not non-negative", pageSize);
            }
        } else {
            console.warn("Content pageable is null", contentPageable)
        }

        return 0;
    }
}