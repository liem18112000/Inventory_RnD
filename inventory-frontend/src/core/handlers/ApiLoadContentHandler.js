import {isJson} from "../utility/ComponentUtility";

/**
 * Handle get page response.
 * If data is not valid return an empty response
 * @param data
 * @param toast
 * @param visibleLevel
 * @returns {{pageable: {pageNumber: number, pageSize: number}, loading: boolean, content: *[], totalElements: number}|*}
 */
export const handleGetPage = (data, toast = null, visibleLevel = "warn") => {
    if(data && isJson(data)) {
        return data
    } else {

        if(toast){
            toast.show({
                severity: visibleLevel,
                summary: 'Load Fail',
                detail:'Service get data failed',
                life: 1000
            });
        }

        return {
            content: [],
            loading: false,
            totalElements: 0,
            pageable: {
                pageNumber: 0,
                pageSize: 1
            }
        }
    }
}