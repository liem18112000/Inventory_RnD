// Ingredient base URL
import axios from 'axios'
import { baseIngredientAPI } from '../constant'
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import {isNumber} from "../core/utility/ComponentUtility";
const BaseURL = baseIngredientAPI()


const mockSample =
    {
        "id": 36,
        "clientId": 1,
        "name": "TestNoUnit",
        "description": "event activated on creating a ingredient-event successfully occurred",
        "updateAt": "2021-12-25T01:30:13.414868500Z",
        "accessAt": "2021-12-25T01:30:44.994685Z",
        "actorName": "inventory-keeper",
        "actorRole": "inventory-keeper",
        "trackTimestamp": "2021-12-25T08:30:13.414868500",
        "ingredient": {
            "id": 128,
            "clientId": 1,
            "name": "TestNoUnit",
            "code": "TestNoUnit@345",
            "description": "TestNoUnit",
            "unit": "generic",
            "unitType": "generic",
            "createAt": "2021-12-25T01:30:13.312332500Z",
            "updateAt": "2021-12-25T01:30:13.312332500Z"
        },
        "event": {
            "name": "ingredient_add",
            "message": "event activated on creating a ingredient",
            "status": {
                "name": "success",
                "message": "event successfully occurred"
            }
        },
        "extraInformation": {
            "id": 128,
            "clientId": 1,
            "name": "TestNoUnit",
            "code": "TestNoUnit@345",
            "description": "TestNoUnit",
            "unit": "generic",
            "unitType": "generic",
            "maximumQuantity": 10000.0,
            "minimumQuantity": 1.0,
            "actorName": "inventory-keeper",
            "actorRole": "inventory-keeper"
        }
    };

const mockListSample =
    [
        {
            "id": 55,
            "clientId": 1,
            "name": "Test Category on UI",
            "description": "event activated on modifying a ingredient-event successfully occurred",
            "updateAt": "2021-12-25T03:58:28.167122200Z",
            "accessAt": "2021-12-25T04:13:43.719948900Z",
            "actorName": "inventory-keeper",
            "actorRole": "inventory-keeper",
            "trackTimestamp": "2021-12-25T10:58:28.167122200",
            "ingredient": {
                "id": 134,
                "clientId": 1,
                "name": "Test Category on UI",
                "code": "UiTestCategory",
                "description": "UiTestCategory",
                "unit": "square_centimeter",
                "unitType": "area",
                "createAt": "2021-12-25T02:07:45.430174700Z",
                "updateAt": "2021-12-25T03:58:43.715374200Z"
            },
            "event": {
                "name": "ingredient_modify",
                "message": "event activated on modifying a ingredient",
                "status": {
                    "name": "success",
                    "message": "event successfully occurred"
                }
            },
            "extraInformation": {
                "id": 134,
                "clientId": 1,
                "name": "Test Category on UI",
                "code": "UiTestCategoryEdited",
                "description": "UiTestCategory",
                "unit": "square_centimeter",
                "unitType": "area",
                "maximumQuantity": 10000.0,
                "minimumQuantity": 1.0,
                "actorName": "inventory-keeper",
                "actorRole": "inventory-keeper"
            }
        },
        {
            "id": 56,
            "clientId": 1,
            "name": "Test Category on UI",
            "description": "event activated on modifying a ingredient-event successfully occurred",
            "updateAt": "2021-12-25T03:58:43.725373Z",
            "accessAt": "2021-12-25T04:13:43.720946500Z",
            "actorName": "inventory-keeper",
            "actorRole": "inventory-keeper",
            "trackTimestamp": "2021-12-25T10:58:43.725373",
            "ingredient": {
                "id": 134,
                "clientId": 1,
                "name": "Test Category on UI",
                "code": "UiTestCategory",
                "description": "UiTestCategory",
                "unit": "square_centimeter",
                "unitType": "area",
                "createAt": "2021-12-25T02:07:45.430174700Z",
                "updateAt": "2021-12-25T03:58:43.715374200Z"
            },
            "event": {
                "name": "ingredient_modify",
                "message": "event activated on modifying a ingredient",
                "status": {
                    "name": "success",
                    "message": "event successfully occurred"
                }
            },
            "extraInformation": {
                "id": 134,
                "clientId": 1,
                "name": "Test Category on UI",
                "code": "UiTestCategory",
                "description": "UiTestCategory",
                "unit": "square_centimeter",
                "unitType": "area",
                "maximumQuantity": 10000.0,
                "minimumQuantity": 1.0,
                "actorName": "inventory-keeper",
                "actorRole": "inventory-keeper"
            }
        },
        {
            "id": 57,
            "clientId": 1,
            "name": "Test Category on UI",
            "description": "event activated on modifying a ingredient-event failed to occurred",
            "updateAt": "2021-12-25T03:59:14.330281100Z",
            "accessAt": "2021-12-25T04:13:43.721945300Z",
            "actorName": "inventory-keeper",
            "actorRole": "inventory-keeper",
            "trackTimestamp": "2021-12-25T10:59:14.330281100",
            "ingredient": {
                "id": 134,
                "clientId": 1,
                "name": "Test Category on UI",
                "code": "UiTestCategory",
                "description": "UiTestCategory",
                "unit": "square_centimeter",
                "unitType": "area",
                "createAt": "2021-12-25T02:07:45.430174700Z",
                "updateAt": "2021-12-25T03:58:43.715374200Z"
            },
            "event": {
                "name": "ingredient_modify",
                "message": "event activated on modifying a ingredient",
                "status": {
                    "name": "failed",
                    "message": "event failed to occurred"
                }
            },
            "extraInformation": {
                "id": 134,
                "clientId": 1,
                "name": "Test Category on UI",
                "code": "TestNoUnit@345",
                "description": "UiTestCategory",
                "unit": "square_centimeter",
                "unitType": "area",
                "maximumQuantity": 10000.0,
                "minimumQuantity": 1.0,
                "actorName": "inventory-keeper",
                "actorRole": "inventory-keeper"
            }
        }
    ];

const mockPageSample =
    {
        "content": [
            {
                "id": 44,
                "clientId": 1,
                "name": "Thailand Sting",
                "description": "event activated on adding a single item-event successfully occurred",
                "updateAt": "2021-12-25T02:21:54.338531300Z",
                "accessAt": "2021-12-25T04:15:21.852085900Z",
                "actorName": "liem-admin",
                "actorRole": "client-admin",
                "trackTimestamp": "2021-12-25T09:21:54.338531300",
                "ingredient": {
                    "id": 107,
                    "clientId": 1,
                    "name": "Sting",
                    "code": "STING",
                    "description": "no.1",
                    "unit": "bottle",
                    "unitType": "whole",
                    "createAt": "2021-11-03T07:02:04.799679Z",
                    "updateAt": "2021-11-11T16:03:04.230802500Z"
                },
                "event": {
                    "name": "ingredient_item_add",
                    "message": "event activated on adding a single item",
                    "status": {
                        "name": "success",
                        "message": "event successfully occurred"
                    }
                },
                "extraInformation": {
                    "clientId": 1,
                    "ingredientId": 107,
                    "importId": 1,
                    "name": "Thailand Sting",
                    "code": "THSTINGSS88",
                    "description": "Thailand Sting",
                    "unit": "bottle",
                    "unitType": "whole",
                    "expiredAt": "2021-12-28",
                    "actorName": "liem-admin",
                    "actorRole": "client-admin"
                }
            },
            {
                "id": 45,
                "clientId": 1,
                "name": "Thailand Sting batch",
                "description": "event activated on adding a batch of items-event successfully occurred",
                "updateAt": "2021-12-25T02:22:08.417209900Z",
                "accessAt": "2021-12-25T04:15:21.853082600Z",
                "actorName": "liem-admin",
                "actorRole": "client-admin",
                "trackTimestamp": "2021-12-25T09:22:08.417209900",
                "ingredient": {
                    "id": 107,
                    "clientId": 1,
                    "name": "Sting",
                    "code": "STING",
                    "description": "no.1",
                    "unit": "bottle",
                    "unitType": "whole",
                    "createAt": "2021-11-03T07:02:04.799679Z",
                    "updateAt": "2021-11-11T16:03:04.230802500Z"
                },
                "event": {
                    "name": "ingredient_item_batch_add",
                    "message": "event activated on adding a batch of items",
                    "status": {
                        "name": "success",
                        "message": "event successfully occurred"
                    }
                },
                "extraInformation": {
                    "clientId": 1,
                    "ingredientId": 107,
                    "importId": 1,
                    "name": "Thailand Sting batch",
                    "code": "THSTINGSS_BATCH",
                    "description": "Thailand Sting batch",
                    "unit": "bottle",
                    "unitType": "whole",
                    "expiredAt": "2025-12-21",
                    "actorName": "liem-admin",
                    "actorRole": "client-admin",
                    "quantity": 3,
                    "codes": [
                        "THSTINGSS_BATCH_ffc38d18-eecf-493b-aeb6-920b3c3935cd",
                        "THSTINGSS_BATCH_688e357a-e2f4-4940-88ca-13e19492128a",
                        "THSTINGSS_BATCH_96565620-39f6-4920-94fa-2232a816857c"
                    ]
                }
            },
            {
                "id": 47,
                "clientId": 1,
                "name": "Thailand Sting batch",
                "description": "event activated on removing a single item-event successfully occurred",
                "updateAt": "2021-12-25T02:22:41.942846800Z",
                "accessAt": "2021-12-25T04:15:21.854081300Z",
                "actorName": "inventory-keeper",
                "actorRole": "inventory-keeper",
                "trackTimestamp": "2021-12-25T09:22:41.942846800",
                "ingredient": {
                    "id": 107,
                    "clientId": 1,
                    "name": "Sting",
                    "code": "STING",
                    "description": "no.1",
                    "unit": "bottle",
                    "unitType": "whole",
                    "createAt": "2021-11-03T07:02:04.799679Z",
                    "updateAt": "2021-11-11T16:03:04.230802500Z"
                },
                "event": {
                    "name": "ingredient_item_remove",
                    "message": "event activated on removing a single item",
                    "status": {
                        "name": "success",
                        "message": "event successfully occurred"
                    }
                },
                "extraInformation": {
                    "id": 152,
                    "clientId": 1,
                    "ingredientId": 107,
                    "importId": 1,
                    "name": "Thailand Sting batch",
                    "code": "THSTINGSS_BATCH_96565620-39f6-4920-94fa-2232a816857c",
                    "description": "Thailand Sting batch",
                    "unit": "bottle",
                    "unitType": "whole",
                    "expiredAt": "2025-12-21",
                    "updateAt": "2021-12-25T02:22:08.397450700Z",
                    "actorName": "inventory-keeper",
                    "actorRole": "inventory-keeper"
                }
            }
        ],
        "pageable": {
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "pageSize": 3,
            "pageNumber": 0,
            "unpaged": false,
            "paged": true
        },
        "last": true,
        "totalPages": 1,
        "totalElements": 3,
        "number": 0,
        "size": 3,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "first": true,
        "numberOfElements": 3,
        "empty": false
    };

const mockEvents =
    [
        {
            "value": 1,
            "label": "do nothing"
        },
        {
            "value": 2,
            "label": "ingredient item add"
        },
        {
            "value": 3,
            "label": "ingredient item batch add"
        },
        {
            "value": 4,
            "label": "ingredient item remove"
        },
        {
            "value": 5,
            "label": "ingredient item modify"
        },
        {
            "value": 6,
            "label": "ingredient add"
        },
        {
            "value": 7,
            "label": "ingredient remove"
        },
        {
            "value": 8,
            "label": "ingredient modify"
        },
        {
            "value": 9,
            "label": "ingredient config modify"
        }
    ];

const mockStatus =
    [
        {
            "value": "success",
            "label": "success"
        },
        {
            "value": "pending",
            "label": "pending"
        },
        {
            "value": "failed",
            "label": "failed"
        },
        {
            "value": "unknown",
            "label": "unknown"
        }
    ];

/**
 * Inventory track service
 */
export default class InventoryTrackService {

    /**
     * Get ingredient history by id
     * @param id
     * @param isMock
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{extraInformation: {unitType: string, unit: string, clientId: number, code: string, actorRole: string, name: string, description: string, actorName: string, id: number, minimumQuantity: number, maximumQuantity: number}, clientId: number, ingredient: {unitType: string, unit: string, clientId: number, code: string, name: string, description: string, updateAt: string, id: number, createAt: string}, actorRole: string, name: string, accessAt: string, description: string, updateAt: string, actorName: string, id: number, event: {name: string, message: string, status: {name: string, message: string}}, trackTimestamp: string}>}
     */
    getById(id, isMock = true) {

        if(isMock) {
            return Promise.resolve(mockSample);
        }

        if(isNaN(id)){
            throw Error("Inventory track id is null")
        }

        return axios
            .get(`${BaseURL}/history/${id}`,{
                headers: getHeaderByGatewayStatus({})})
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     *
     * @param ingredientId  Ingredient detail id
     * @param isMock
     * @returns {Promise<AxiosResponse<any> | void>|Promise<[{extraInformation: {unitType: string, unit: string, clientId: number, code: string, actorRole: string, name: string, description: string, actorName: string, id: number, minimumQuantity: number, maximumQuantity: number}, clientId: number, ingredient: {unitType: string, unit: string, clientId: number, code: string, name: string, description: string, updateAt: string, id: number, createAt: string}, actorRole: string, name: string, accessAt: string, description: string, updateAt: string, actorName: string, id: number, event: {name: string, message: string, status: {name: string, message: string}}, trackTimestamp: string}, {extraInformation: {unitType: string, unit: string, clientId: number, code: string, actorRole: string, name: string, description: string, actorName: string, id: number, minimumQuantity: number, maximumQuantity: number}, clientId: number, ingredient: {unitType: string, unit: string, clientId: number, code: string, name: string, description: string, updateAt: string, id: number, createAt: string}, actorRole: string, name: string, accessAt: string, description: string, updateAt: string, actorName: string, id: number, event: {name: string, message: string, status: {name: string, message: string}}, trackTimestamp: string}, {extraInformation: {unitType: string, unit: string, clientId: number, code: string, actorRole: string, name: string, description: string, actorName: string, id: number, minimumQuantity: number, maximumQuantity: number}, clientId: number, ingredient: {unitType: string, unit: string, clientId: number, code: string, name: string, description: string, updateAt: string, id: number, createAt: string}, actorRole: string, name: string, accessAt: string, description: string, updateAt: string, actorName: string, id: number, event: {name: string, message: string, status: {name: string, message: string}}, trackTimestamp: string}]>}
     */
    getAll(ingredientId, isMock = true) {

        if(isMock) {
            return Promise.resolve(mockListSample);
        }

        let config = {headers: getHeaderByGatewayStatus({})};
        if(isNumber(ingredientId)){
            config = {...config, params: {"ingredientId": ingredientId}}
        }

        return axios
            .get(`${BaseURL}/history/all`,config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get page of history track by ingredient detail id (ingredient type id) with filter
     * @param ingredientId      Ingredient type id
     * @param filter            Filter by name, description, code, unit, unit type and created at
     * @param page              Page
     * @param rows              Size per page
     * @param sortField         Sorting field (default field is id)
     * @param sortOrder         Sorting order (default order is desc)
     * @param isMock            Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{number: number, last: boolean, size: number, numberOfElements: number, totalPages: number, pageable: {paged: boolean, pageNumber: number, offset: number, pageSize: number, unpaged: boolean, sort: {unsorted: boolean, sorted: boolean, empty: boolean}}, sort: {unsorted: boolean, sorted: boolean, empty: boolean}, content: [{extraInformation: {unitType: string, ingredientId: number, expiredAt: string, unit: string, clientId: number, importId: number, code: string, actorRole: string, name: string, description: string, actorName: string}, clientId: number, ingredient: {unitType: string, unit: string, clientId: number, code: string, name: string, description: string, updateAt: string, id: number, createAt: string}, actorRole: string, name: string, accessAt: string, description: string, updateAt: string, actorName: string, id: number, event: {name: string, message: string, status: {name: string, message: string}}, trackTimestamp: string}, {extraInformation: {codes: string[], clientId: number, code: string, quantity: number, actorRole: string, description: string, actorName: string, unitType: string, ingredientId: number, expiredAt: string, unit: string, importId: number, name: string}, clientId: number, ingredient: {unitType: string, unit: string, clientId: number, code: string, name: string, description: string, updateAt: string, id: number, createAt: string}, actorRole: string, name: string, accessAt: string, description: string, updateAt: string, actorName: string, id: number, event: {name: string, message: string, status: {name: string, message: string}}, trackTimestamp: string}, {extraInformation: {clientId: number, code: string, actorRole: string, description: string, updateAt: string, actorName: string, unitType: string, ingredientId: number, expiredAt: string, unit: string, importId: number, name: string, id: number}, clientId: number, ingredient: {unitType: string, unit: string, clientId: number, code: string, name: string, description: string, updateAt: string, id: number, createAt: string}, actorRole: string, name: string, accessAt: string, description: string, updateAt: string, actorName: string, id: number, event: {name: string, message: string, status: {name: string, message: string}}, trackTimestamp: string}], first: boolean, totalElements: number, empty: boolean}>}
     */
    getPage(ingredientId, filter, page, rows, sortField, sortOrder, isMock = true) {

        if(isMock) {
            return Promise.resolve(mockPageSample);
        }

        const order = sortOrder === 1 ? 'asc' : 'desc';
        const sort = sortField ? `${sortField}, ${order}` : `id, ${order}`;

        return axios
            .post(`${BaseURL}/history/page`, {
                "ingredientId": ingredientId,
                "name": !filter ? "" : filter.name,
                "description": !filter ? "" : filter.description,
                "actorName": !filter ? "" : filter.actorName,
                "actorRole": !filter ? "" : filter.actorRole,
                "event": !filter ? "" : filter.event,
                "trackTimestamp": !filter ? "" : filter.trackTimestamp,
                "status": !filter ? "" : filter.status,

                "page": page ? page : 0,
                "size": rows ? rows : 10,
                "sort": sort
            }, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get list of label-value events
     * @param isMocking
     * @returns list of label-value events
     */
    getEvents(isMocking = true) {

        if(isMocking) {
            return Promise.resolve(mockEvents);
        }

        return axios
            .get(`${BaseURL}/event/all/simple`)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get list of label-value statuses
     * @param isMocking
     * @returns list of label-value statuses
     */
    getStatuses(isMocking = true) {

        if(isMocking) {
            return Promise.resolve(mockStatus)
        }

        return axios
            .get(`${BaseURL}/status/all/simple`)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

}