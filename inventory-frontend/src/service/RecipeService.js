import axios from 'axios'
import { baseRecipeAPI, TENANT_ID } from '../constant'
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import {FilterRequestMapper} from "../core/models/mapper/ModelMapper";

const groupJson = {
    "content": [
        {
            "id": 7,
            "tenantId": 1,
            "name": "test-recipe-01",
            "description": "description",
            "updateAt": "2021-11-10T04:19:41.896827Z",
            "createAt": "2021-11-10T04:19:41.896827Z",
            "activated": true,
            "accessAt": "2021-11-25T10:01:03.106528800Z",
            "code": "test-recipe-01"
        },
        {
            "id": 5,
            "tenantId": 1,
            "name": "test-recipe",
            "description": "description",
            "updateAt": "2021-10-27T15:50:57.727011200Z",
            "createAt": "2021-10-27T15:50:57.727011200Z",
            "activated": true,
            "accessAt": "2021-11-25T10:01:03.120530700Z",
            "children": [
                {
                    "id": 6,
                    "tenantId": 1,
                    "name": "test-recipe-child",
                    "description": "description",
                    "updateAt": "2021-10-27T15:52:03.199970100Z",
                    "createAt": "2021-10-27T15:52:03.199970100Z",
                    "activated": true,
                    "accessAt": "2021-11-25T10:01:03.120530700Z",
                    "code": "123123987"
                },
                {
                    "id": 8,
                    "tenantId": 1,
                    "name": "test-recipe-child-02",
                    "description": "description",
                    "updateAt": "2021-11-10T04:19:59.445171900Z",
                    "createAt": "2021-11-10T04:19:59.445171900Z",
                    "activated": true,
                    "accessAt": "2021-11-25T10:01:03.120530700Z",
                    "code": "test-recipe-child-02"
                },
                {
                    "id": 9,
                    "tenantId": 1,
                    "name": "test-recipe-child-03",
                    "description": "description",
                    "updateAt": "2021-11-10T04:20:11.323849900Z",
                    "createAt": "2021-11-10T04:20:11.323849900Z",
                    "activated": true,
                    "accessAt": "2021-11-25T10:01:03.120530700Z",
                    "code": "test-recipe-child-03"
                }
            ],
            "code": "123123123"
        },
        {
            "id": 3,
            "tenantId": 1,
            "name": "Yogurt",
            "description": "yorgurt",
            "updateAt": "2021-10-22 07:41:44",
            "createAt": "2021-10-22 07:41:44",
            "activated": true,
            "accessAt": "2021-11-25T10:01:03.122528900Z",
            "children": [
                {
                    "id": 4,
                    "tenantId": 1,
                    "name": "Strawberri yogurt",
                    "description": "Strawberri yogurt",
                    "updateAt": "2021-10-22 07:41:44",
                    "createAt": "2021-10-22 07:41:44",
                    "activated": true,
                    "accessAt": "2021-11-25T10:01:03.122528900Z",
                    "code": "531531"
                }
            ],
            "code": "135135"
        },
        {
            "id": 1,
            "tenantId": 1,
            "name": "Cheese",
            "description": "cheese",
            "updateAt": "2021-10-22 07:41:44",
            "createAt": "2021-10-22 07:41:44",
            "activated": true,
            "accessAt": "2021-11-25T10:01:03.124536800Z",
            "children": [
                {
                    "id": 2,
                    "tenantId": 1,
                    "name": "Mozelea cheese",
                    "description": "Mozzella cheese",
                    "updateAt": "2021-10-22 07:41:44",
                    "createAt": "2021-10-22 07:41:44",
                    "activated": true,
                    "accessAt": "2021-11-25T10:01:03.124536800Z",
                    "code": "321321"
                }
            ],
            "code": "123123"
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
    "totalPages": 1,
    "totalElements": 4,
    "number": 0,
    "size": 10,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "first": true,
    "numberOfElements": 4,
    "empty": false
}

const childJson = {
    "content": [
        {
            "id": 9,
            "tenantId": 1,
            "name": "test-recipe-child-03",
            "description": "description",
            "updateAt": "2021-11-10T04:20:11.323849900Z",
            "createAt": "2021-11-10T04:20:11.323849900Z",
            "activated": true,
            "accessAt": "2021-11-25T09:52:11.420211200Z",
            "code": "test-recipe-child-03"
        },
        {
            "id": 8,
            "tenantId": 1,
            "name": "test-recipe-child-02",
            "description": "description",
            "updateAt": "2021-11-10T04:19:59.445171900Z",
            "createAt": "2021-11-10T04:19:59.445171900Z",
            "activated": true,
            "accessAt": "2021-11-25T09:52:11.420211200Z",
            "code": "test-recipe-child-02"
        },
        {
            "id": 6,
            "tenantId": 1,
            "name": "test-recipe-child",
            "description": "description",
            "updateAt": "2021-10-27T15:52:03.199970100Z",
            "createAt": "2021-10-27T15:52:03.199970100Z",
            "activated": true,
            "accessAt": "2021-11-25T09:52:11.420211200Z",
            "code": "123123987"
        },
        {
            "id": 4,
            "tenantId": 1,
            "name": "Strawberri yogurt",
            "description": "Strawberri yogurt",
            "updateAt": "2021-10-22 07:41:44",
            "createAt": "2021-10-22 07:41:44",
            "activated": true,
            "accessAt": "2021-11-25T09:52:11.420211200Z",
            "code": "531531"
        },
        {
            "id": 2,
            "tenantId": 1,
            "name": "Mozelea cheese",
            "description": "Mozzella cheese",
            "updateAt": "2021-10-22 07:41:44",
            "createAt": "2021-10-22 07:41:44",
            "activated": true,
            "accessAt": "2021-11-25T09:52:11.420211200Z",
            "code": "321321"
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
        "paged": true,
        "unpaged": false
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

const detailJson = {
    "content": [
        {
            "id": 1,
            "tenantId": 1,
            "name": "Recipe detail",
            "description": "Not available",
            "updateAt": "2021-11-09 23:40:53",
            "activated": true,
            "accessAt": "2021-11-26T07:52:51.295248700Z",
            "code": "CODE01",
            "ingredient": {
                "id": 2,
                "name": "New Zealand Cow Milk",
                "code": "NZCM",
                "description": "New Zealand Cow Milk",
                "unit": "bottle",
                "unitType": "whole"
            },
            "quantity": 4.0,
            "recipe": {
                "id": 2,
                "tenantId": 1,
                "name": "Mozelea cheese",
                "description": "Mozzella cheese",
                "updateAt": "2021-10-22 07:41:44",
                "createAt": "2021-10-22 07:41:44",
                "activated": true,
                "accessAt": "2021-11-26T07:52:51.055559Z",
                "code": "321321"
            }
        },
        {
            "id": 2,
            "tenantId": 1,
            "name": "Recipe detail",
            "description": "Not available",
            "updateAt": "2021-11-09 23:40:53",
            "activated": true,
            "accessAt": "2021-11-26T07:52:51.311443400Z",
            "code": "CODE02",
            "ingredient": {
                "id": 3,
                "name": "Queen land Goat milk",
                "code": "QLGM",
                "description": "Queen land Goat Milk",
                "unit": "bottle",
                "unitType": "whole"
            },
            "quantity": 2.0,
            "recipe": {
                "id": 2,
                "tenantId": 1,
                "name": "Mozelea cheese",
                "description": "Mozzella cheese",
                "updateAt": "2021-10-22 07:41:44",
                "createAt": "2021-10-22 07:41:44",
                "activated": true,
                "accessAt": "2021-11-26T07:52:51.295248700Z",
                "code": "321321"
            }
        },
        {
            "id": 3,
            "tenantId": 1,
            "name": "Recipe detail",
            "description": "Not available",
            "updateAt": "2021-11-09 23:40:53",
            "activated": true,
            "accessAt": "2021-11-26T07:52:51.326028100Z",
            "code": "CODE03",
            "ingredient": {
                "id": 2,
                "name": "New Zealand Cow Milk",
                "code": "NZCM",
                "description": "New Zealand Cow Milk",
                "unit": "bottle",
                "unitType": "whole"
            },
            "quantity": 2.0,
            "recipe": {
                "id": 6,
                "tenantId": 1,
                "name": "test-recipe-child",
                "description": "description",
                "updateAt": "2021-10-27T15:52:03.199970100Z",
                "createAt": "2021-10-27T15:52:03.199970100Z",
                "activated": true,
                "accessAt": "2021-11-26T07:52:51.313445400Z",
                "code": "123123987"
            }
        },
        {
            "id": 5,
            "tenantId": 1,
            "name": "Recipe detail",
            "description": "Recipe detail edited",
            "updateAt": "2021-11-20T05:17:37.665884300Z",
            "activated": true,
            "accessAt": "2021-11-26T07:52:51.336605700Z",
            "code": "CODE04",
            "ingredient": {
                "id": 3,
                "name": "Queen land Goat milk",
                "code": "QLGM",
                "description": "Queen land Goat Milk",
                "unit": "bottle",
                "unitType": "whole"
            },
            "quantity": 2.0,
            "recipe": {
                "id": 6,
                "tenantId": 1,
                "name": "test-recipe-child",
                "description": "description",
                "updateAt": "2021-10-27T15:52:03.199970100Z",
                "createAt": "2021-10-27T15:52:03.199970100Z",
                "activated": true,
                "accessAt": "2021-11-26T07:52:51.326028100Z",
                "code": "123123987"
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
        "pageSize": 10,
        "pageNumber": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 4,
    "number": 0,
    "size": 10,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 4,
    "first": true,
    "empty": false
}

// Get base recipe URL
const BaseURL = baseRecipeAPI()

/**
 * Recipe Service
 */
export class RecipeService {

    /**
     * Default constructor
     */
    constructor() {
        this.mapper = new FilterRequestMapper();
    }

    /**
     * Get page of recipe group by filter
     * @param filter        Filter on name, description, code and updateAt
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageGroup(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson);
        }

        const url       = `${BaseURL}/group/page`;
        const body      = this.mapper.toRequest(filter, page, rows, sortField, sortOrder);
        const config    = { headers: getHeaderByGatewayStatus() };

        // Fetch recipe group data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get page of recipe by filter and parent id
     * @param parentId      Recipe group id
     * @param filter        Filter on name, description, code and updateAt
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageChild(parentId, filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve(childJson);
        }

        const url       = `${BaseURL}/child/page`;
        const request   = { ...filter, parentId: parentId };
        const body      = this.mapper.toRequest(request, page, rows, sortField, sortOrder);
        const config    = { headers: getHeaderByGatewayStatus() };

        // fetch recipe child data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get page of recipe detail by filter and recipe id
     * @param recipeId      Recipe id
     * @param filter        Filter on name, description, code and updateAt
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageDetail(recipeId, filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve(detailJson);
        }

        const url       = `${BaseURL}/detail/page`;
        const request   = { ...filter, recipeId: recipeId };
        const body      = this.mapper.toRequest(request, page, rows, sortField, sortOrder);
        const config    = { headers: getHeaderByGatewayStatus() };

        // fetch recipe detail data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get recipe by id
     * @param id            Recipe id
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getByID(id, isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson.content[0]);
        }

        return axios
            .get(`${BaseURL}/${id}`, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

/**
     * Get recipe group by id
     * @param isMock        Activate mock if true otherwise use real api call
     */
     getRecipeGroupSimple(isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson.content[0]);
        }

        return axios
            .get(`${BaseURL}/group/simple`, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
     }

    /**
     * Get recipe detail by id
     * @param id            Recipe detail id
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getDetailByID(id, isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson.content[0]);
        }

        return axios
            .get(`${BaseURL}/detail/${id}`, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Update recipe (both group and child)
     * @param recipe        Recipe need to be updated
     * @param isMock        Activate mock if true otherwise use real api call
     */
    updateRecipe(recipe, isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson.content[0]);
        }

        return axios
            .put(`${BaseURL}`, recipe, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Save recipe (both group and child)
     * @param recipe        Recipe need to be saved
     * @param isMock        Activate mock if true otherwise use real api call
     */
    saveRecipe(recipe, isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson.content[0]);
        }

        return axios
            .post(`${BaseURL}`, recipe, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    saveDetail(detail, isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson.content[0]);
        }

        return axios
            .post(`${BaseURL}/detail`, detail, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    updateDetail(detail, isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson.content[0]);
        }

        return axios
            .put(`${BaseURL}/detail`, detail, {
                headers: getHeaderByGatewayStatus({})
            })
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Delete recipe by id
     * @param id            Recipe ID
     * @param isMock        Activate mock if true otherwise use real api call
     */
    deleteRecipe(id, isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson.content[0]);
        }

        return axios.delete(`${BaseURL}/${id}`, {
                headers: getHeaderByGatewayStatus({})
            }).then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Delete recipe detail by id
     * @param id            Recipe Detail ID
     * @param isMock        Activate mock if true otherwise use real api call
     */
    deleteRecipeDetail(id, isMock = true) {
        if (isMock) {
            return Promise.resolve(groupJson.content[0]);
        }

        return axios.delete(`${BaseURL}/detail/${id}`, {
            headers: getHeaderByGatewayStatus({})
        }).then(res => res.data)
            .catch(error => console.log(error));
    }

    getAllRecipeChild(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return Promise.resolve(childJson);
        }

        const url       = `${BaseURL}/child/page/all`;
        const body      = this.mapper.toRequest(filter, page, rows, sortField, sortOrder);
        const config    = { headers: getHeaderByGatewayStatus() };

        // Fetch all recipe child data from api
        return axios.post(url, body, config)
            .then(res => res.data)
            .catch(error => console.log(error));

    }
}
