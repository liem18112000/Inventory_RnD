import axios from 'axios'
import { baseAPI } from '../constant'
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";

var categoryJson = {
    "content": [
        {
            "id": 28,
            "clientId": 1,
            "name": "Mirinda",
            "code": "88331188",
            "description": "Mirinda",
            "unit": "bottle",
            "unitType": "whole",
            "quantity": 0,
            "createAt": "2021-10-07T14:16:52.357651500Z",
            "updateAt": "2021-10-07T14:16:52.357651500Z"
        },
        {
            "id": 24,
            "clientId": 1,
            "name": "Pepsi",
            "code": "99331133",
            "description": "Pepsi",
            "unit": "can",
            "unitType": "whole",
            "quantity": 4,
            "createAt": "2021-10-07T07:00:36.379957900Z",
            "updateAt": "2021-10-07T07:00:36.380955400Z"
        },
        {
            "id": 23,
            "clientId": 1,
            "name": "Coca cola",
            "code": "00331133",
            "description": "Coca cola",
            "unit": "can",
            "unitType": "whole",
            "quantity": 3,
            "createAt": "2021-10-07T02:18:36.061655500Z",
            "updateAt": "2021-10-07T02:18:36.061655500Z"
        },
        {
            "id": 3,
            "clientId": 1,
            "name": "Queen land Goat milk",
            "code": "QLGM",
            "description": "Queen land Goat milk",
            "unit": "bottle",
            "unitType": "whole",
            "quantity": 3,
            "createAt": "2021-10-03 22:27:07",
            "updateAt": "2021-10-05T15:22:04.989878200Z"
        },
        {
            "id": 2,
            "clientId": 1,
            "name": "New Zealand Cow Milk",
            "code": "NZCM",
            "description": "New Zealand Cow Milk",
            "unit": "bottle",
            "unitType": "whole",
            "quantity": 4,
            "createAt": "2021-10-03 22:27:07",
            "updateAt": "2021-10-05T15:25:04.225930500Z"
        }
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalElements": 5,
    "totalPages": 1,
    "number": 0,
    "size": 10,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "first": true,
    "numberOfElements": 5,
    "empty": false
}

var typeJson = {
    "content": [
        {
            "id": 28,
            "clientId": 1,
            "name": "Mirinda",
            "code": "88331188",
            "description": "Mirinda",
            "unit": "bottle",
            "unitType": "whole",
            "quantity": 0,
            "createAt": "2021-10-07T14:16:52.357651500Z",
            "updateAt": "2021-10-07T14:16:52.357651500Z"
        },
        {
            "id": 24,
            "clientId": 1,
            "name": "Pepsi",
            "code": "99331133",
            "description": "Pepsi",
            "unit": "can",
            "unitType": "whole",
            "quantity": 4,
            "createAt": "2021-10-07T07:00:36.379957900Z",
            "updateAt": "2021-10-07T07:00:36.380955400Z"
        },
        {
            "id": 23,
            "clientId": 1,
            "name": "Coca cola",
            "code": "00331133",
            "description": "Coca cola",
            "unit": "can",
            "unitType": "whole",
            "quantity": 3,
            "createAt": "2021-10-07T02:18:36.061655500Z",
            "updateAt": "2021-10-07T02:18:36.061655500Z"
        },
        {
            "id": 3,
            "clientId": 1,
            "name": "Queen land Goat milk",
            "code": "QLGM",
            "description": "Queen land Goat milk",
            "unit": "bottle",
            "unitType": "whole",
            "quantity": 3,
            "createAt": "2021-10-03 22:27:07",
            "updateAt": "2021-10-05T15:22:04.989878200Z"
        },
        {
            "id": 2,
            "clientId": 1,
            "name": "New Zealand Cow Milk",
            "code": "NZCM",
            "description": "New Zealand Cow Milk",
            "unit": "bottle",
            "unitType": "whole",
            "quantity": 4,
            "createAt": "2021-10-03 22:27:07",
            "updateAt": "2021-10-05T15:25:04.225930500Z"
        }
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalElements": 5,
    "totalPages": 1,
    "number": 0,
    "size": 10,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "first": true,
    "numberOfElements": 5,
    "empty": false
}

// Ingredient base URL
const BaseURL = baseAPI()

/**
 * Ingredient service
 */
export class IngredientService {

    /**
     * Get page of category ingredient by filter
     * @param filter        Filter on name, description, code and createAt
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{number: number, last: boolean, size: number, numberOfElements: number, totalPages: number, pageable: {paged: boolean, pageNumber: number, offset: number, pageSize: number, unpaged: boolean, sort: {unsorted: boolean, sorted: boolean, empty: boolean}}, sort: {unsorted: boolean, sorted: boolean, empty: boolean}, content: [{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}], first: boolean, totalElements: number, empty: boolean}>}
     */
    getPageCategory(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve(categoryJson);
        }

        const order     = sortOrder === 1 ? 'asc' : 'desc';
        const sort      = sortField ? `${sortField}, ${order}` : `id, ${order}`;
        const headers   = getHeaderByGatewayStatus({})

        // fetch ingredient category data from api 
        return axios.post(`${BaseURL}/category/page`, {
            "name":         !filter ? "" : filter.name,
            "description":  !filter ? "" : filter.description,
            "code":         !filter ? "" : filter.code,
            "createAt":     !filter ? "" : filter.createAt,

            "page": page ? page : 0,
            "size": rows ? rows : 10,
            "sort": sort
        }, {
            headers: headers
        }).then(res => {
            return res.data
        }).catch(error => console.log(error));
    }

    /**
     * Sync items in inventory
     * @returns {Promise<AxiosResponse<any> | void>}
     */
    syncInventory() {
        const headers   = getHeaderByGatewayStatus({});
        return axios.post(`${BaseURL}/inventory/sync`, {}, {
            headers: headers
        }).then(res => {
            return res.data
        }).catch(error => console.log(error));
    }

    /**
     * Get page of Ingredient type with filter
     * @param parentId      Ingredient Parent ID (Ingredient Category ID)
     * @param filter        Filter by name, description, code, unit, unit type, created at and update at
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{number: number, last: boolean, size: number, numberOfElements: number, totalPages: number, pageable: {paged: boolean, pageNumber: number, offset: number, pageSize: number, unpaged: boolean, sort: {unsorted: boolean, sorted: boolean, empty: boolean}}, sort: {unsorted: boolean, sorted: boolean, empty: boolean}, content: [{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}], first: boolean, totalElements: number, empty: boolean}>}
     */
    getPageType(parentId, filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve(typeJson);
        }

        const order = sortOrder === 1 ? 'asc' : 'desc';
        const sort = sortField ? `${sortField}, ${order}` : `id, ${order}`;
        const headers   = getHeaderByGatewayStatus({})

        // fetch ingredient type data from api 
        return axios.post(`${BaseURL}/type/page`, {
            "name":         !filter ? "" : filter.name,
            "description":  !filter ? "" : filter.description,
            "code":         !filter ? "" : filter.code,
            "createAt":     !filter ? "" : filter.createAt,
            "unitType":     !filter ? "" : filter.unitType,
            "unit":         !filter ? "" : filter.unit,

            "parentId":     parentId,

            "page": page ? page : 0,
            "size": rows ? rows : 10,
            "sort": sort
        }, {
            headers: headers
        }).then(res => {
            return res.data
        }).catch(error => console.log(error));
    }

    /**
     * Get page of item by ingredient detail id (ingredient type id) with filter
     * @param ingredientId      Ingredient Detail ID (Ingredient Type id)
     * @param filter            Filter by name, description, code, unit, unit type and created at
     * @param page              Page
     * @param rows              Size per page
     * @param sortField         Sorting field (default field is id)
     * @param sortOrder         Sorting order (default order is desc)
     * @param isMock            Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{number: number, last: boolean, size: number, numberOfElements: number, totalPages: number, pageable: {paged: boolean, pageNumber: number, offset: number, pageSize: number, unpaged: boolean, sort: {unsorted: boolean, sorted: boolean, empty: boolean}}, sort: {unsorted: boolean, sorted: boolean, empty: boolean}, content: [{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}], first: boolean, totalElements: number, empty: boolean}>}
     */
    getPageItem(ingredientId, filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve(typeJson);
        }

        const order     = sortOrder === 1 ? 'asc' : 'desc';
        const sort      = sortField ? `${sortField}, ${order}` : `id, ${order}`;
        const headers   = getHeaderByGatewayStatus({});

        // fetch ingredient type data from api 
        return axios.post(`${BaseURL}/item/page`, {
            "name": !filter ? "" : filter.name,
            "description": !filter ? "" : filter.description,
            "code": !filter ? "" : filter.code,
            "createAt": !filter ? "" : filter.createAt,
            "unitType": !filter ? "" : filter.unitType,
            "unit": !filter ? "" : filter.unit,

            "ingredientId": ingredientId,

            "page": page ? page : 0,
            "size": rows ? rows : 10,
            "sort": sort
        }, {
            headers: headers
        }).then(res => {
            return res.data
        }).catch(error => console.log(error));
    }

    /**
     * Get page of inventory statistics from inventory for each ingredient detail (ingredient type)
     * @param filter            Filter by name, description, code, unit, unit type and updated at
     * @param page              Page
     * @param rows              Size per page
     * @param sortField         Sorting field (default field is id)
     * @param sortOrder         Sorting order (default order is desc)
     * @param isMock            Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{number: number, last: boolean, size: number, numberOfElements: number, totalPages: number, pageable: {paged: boolean, pageNumber: number, offset: number, pageSize: number, unpaged: boolean, sort: {unsorted: boolean, sorted: boolean, empty: boolean}}, sort: {unsorted: boolean, sorted: boolean, empty: boolean}, content: [{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}, {unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}], first: boolean, totalElements: number, empty: boolean}>}
     */
    getPageInventory(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve(typeJson);
        }

        const order     = sortOrder === 1 ? 'asc' : 'desc';
        const sort      = sortField ? `${sortField}, ${order}` : `id, ${order}`;
        const headers   = getHeaderByGatewayStatus({})

        // fetch ingredient type data from api 
        return axios.post(`${BaseURL}/inventory/page`, {
            "name":         !filter ? "" : filter.name,
            "description":  !filter ? "" : filter.description,
            "updateAt":     !filter ? "" : filter.updateAt,
            "unitType":     !filter ? "" : filter.unitType,
            "unit":         !filter ? "" : filter.unit,

            "ingredientId": !filter ? null : filter.ingredientId,

            "page": page ? page : 0,
            "size": rows ? rows : 10,
            "sort": sort
        }, {
            headers: headers
        }).then(res => {
            return res.data
        }).catch(error => console.log(error));
    }

    /**
     * Get list of label-value of ingredient detail (ingredient type)
     * Label will be ingredient detail name and value is its ID
     * @param isMock Activate mock if true otherwise use real api call
     */
    getInventoryIngredientDetail(isMock = true) {
        if (isMock) {
            return Promise.resolve(typeJson.content[0]);
        }

        return axios.get(`${BaseURL}/type/simple`, {
            headers: getHeaderByGatewayStatus({})
        })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get ingredient (both category and detail) by id
     * @param id        Ingredient id
     * @param isMock    Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}>}
     */
    getByID(id, isMock = true) {
        if (isMock) {
            return Promise.resolve(categoryJson.content[0]);
        }

        const headers = getHeaderByGatewayStatus({})

        return axios
            .get(`${BaseURL}/${id}`, {
                headers: headers
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get item by id
     * @param id        Item id
     * @param isMock    Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}>}
     */
    getItemByID(id, isMock = true) {
        if (isMock) {
            return Promise.resolve(categoryJson.content[0]);
        }

        const headers = getHeaderByGatewayStatus({})

        return axios
            .get(`${BaseURL}/item/${id}`, {
                headers: headers
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get all unit type as list
     * @param isMock    Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<string[]>}
     */
    getUnitTypes(isMock = true) {
        if (isMock) {
            return Promise.resolve([
                "length",
                "volume",
                "area",
                "weight",
                "whole"
            ]);
        }

        return axios.get(`${BaseURL}/unit-type`).then(res => res.data).catch(error => console.log(error))
    }

    /**
     * Get unit of a specific unit type
     * @param unitType      Measurement unit type
     * @param isMock        Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<string[]>}
     */
    getUnit(unitType, isMock = true) {
        if (isMock) {
            return Promise.resolve([
                "bottle",
                "can",
                "pack",
                "piece",
                "box"
            ]);
        }

        return axios.get(`${BaseURL}/unit?unit=${unitType}`).then(res => res.data).catch(error => console.log(error))
    }

    /**
     * Save ingredient (both category and type)
     * @param ingredient    Ingredient need to be saved
     * @param isMock        Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}>}
     */
    saveIngredient(ingredient, isMock = true) {
        if (isMock) {
            return Promise.resolve(categoryJson.content[0]);
        }

        return axios
            .post(`${BaseURL}`, ingredient, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Save item
     * @param item      Item need to be saved
     * @param isMock    Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}>}
     */
    saveItem(item, isMock = true) {
        if (isMock) {
            return Promise.resolve(categoryJson.content[0]);
        }

        return axios
            .post(`${BaseURL}/item`, item, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Update ingredient (both category and type)
     * @param ingredient    Ingredient need to be updated
     * @param isMock        Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}>}
     */
    updateIngredient(ingredient, isMock = true) {
        if (isMock) {
            return Promise.resolve(categoryJson.content[0]);
        }

        return axios
            .put(`${BaseURL}`, ingredient, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Update item
     * @param item      Item need to be updated
     * @param isMock    Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{unitType: string, unit: string, clientId: number, code: string, quantity: number, name: string, description: string, updateAt: string, id: number, createAt: string}>}
     */
    updateItem(item, isMock = true) {
        if (isMock) {
            return Promise.resolve(categoryJson.content[0]);
        }

        return axios
            .put(`${BaseURL}/item/${item.id}`, item, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Delete ingredient by id
     * @param id        Ingredient ID
     * @param isMock    Activate mock if true otherwise use real api call
     * @returns {Promise<unknown>|Promise<AxiosResponse<any>>}
     */
    deleteIngredient(id, isMock = true) {
        if (isMock) {
            return Promise.resolve(null);
        }

        return axios
            .delete(`${BaseURL}/${id}`, {
                headers: getHeaderByGatewayStatus({})
            })
    }
}