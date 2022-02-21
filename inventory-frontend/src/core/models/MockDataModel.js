export const mockPageIngredientCategory = () => {
    const mock = {
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
    return Promise.resolve(mock);
}

export const mockPageIngredientType = () => {
    const mock = {
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
    return Promise.resolve(mock);
}

export const mockPageIngredientItem = () => {
    const mock  = {
        "content": [
            {
                "id": 17,
                "clientId": 1,
                "ingredient": {
                    "id": 23,
                    "clientId": 1,
                    "name": "Coca cola",
                    "code": "00331133",
                    "description": "Coca cola",
                    "unit": "can",
                    "unitType": "whole",
                    "createAt": "2021-10-07T02:18:36.061655500Z",
                    "updateAt": "2021-10-07T02:18:36.061655500Z"
                },
                "name": "British Coca cola",
                "code": "CCCL01",
                "description": "British Coca cola",
                "unit": "can",
                "unitType": "whole",
                "expiredAt": "2021-12-21"
            },
            {
                "id": 19,
                "clientId": 1,
                "ingredient": {
                    "id": 23,
                    "clientId": 1,
                    "name": "Coca cola",
                    "code": "00331133",
                    "description": "Coca cola",
                    "unit": "can",
                    "unitType": "whole",
                    "createAt": "2021-10-07T02:18:36.061655500Z",
                    "updateAt": "2021-10-07T02:18:36.061655500Z"
                },
                "importId": 2,
                "name": "British Coca cola",
                "code": "CCCL02",
                "description": "British Coca cola",
                "unit": "can",
                "unitType": "whole",
                "expiredAt": "2021-12-21"
            },
            {
                "id": 20,
                "clientId": 1,
                "ingredient": {
                    "id": 23,
                    "clientId": 1,
                    "name": "Coca cola",
                    "code": "00331133",
                    "description": "Coca cola",
                    "unit": "can",
                    "unitType": "whole",
                    "createAt": "2021-10-07T02:18:36.061655500Z",
                    "updateAt": "2021-10-07T02:18:36.061655500Z"
                },
                "importId": 2,
                "name": "Premium British Coca cola",
                "code": "CCCL03",
                "description": "British Coca cola",
                "unit": "can",
                "unitType": "whole",
                "expiredAt": "2022-12-21"
            },
            {
                "id": 47,
                "clientId": 1,
                "ingredient": {
                    "id": 23,
                    "clientId": 1,
                    "name": "Coca cola",
                    "code": "00331133",
                    "description": "Coca cola",
                    "unit": "can",
                    "unitType": "whole",
                    "createAt": "2021-10-07T02:18:36.061655500Z",
                    "updateAt": "2021-10-07T02:18:36.061655500Z"
                },
                "name": "British Coca cola",
                "code": "CCCL07",
                "description": "British Coca cola",
                "unit": "can",
                "unitType": "whole",
                "expiredAt": "2021-11-19"
            },
            {
                "id": 48,
                "clientId": 1,
                "ingredient": {
                    "id": 23,
                    "clientId": 1,
                    "name": "Coca cola",
                    "code": "00331133",
                    "description": "Coca cola",
                    "unit": "can",
                    "unitType": "whole",
                    "createAt": "2021-10-07T02:18:36.061655500Z",
                    "updateAt": "2021-10-07T02:18:36.061655500Z"
                },
                "name": "British Coca cola",
                "code": "CCCL05",
                "description": "British Coca cola",
                "unit": "can",
                "unitType": "whole",
                "expiredAt": "2021-11-19"
            }
        ],
        "pageable": {
            "sort": {
                "empty": false,
                "unsorted": false,
                "sorted": true
            },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 5,
            "paged": true,
            "unpaged": false
        },
        "totalElements": 9,
        "totalPages": 2,
        "last": false,
        "number": 0,
        "size": 5,
        "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
        },
        "first": true,
        "numberOfElements": 5,
        "empty": false
    }
    return Promise.resolve(mock);
}

export const mockIngredientLabelValue = () => {
    const mock = [
        {
            "value": 2,
            "label": "New Zealand Cow Milk"
        },
        {
            "value": 3,
            "label": "Queen land Goat milk"
        },
        {
            "value": 23,
            "label": "Coca cola"
        },
        {
            "value": 28,
            "label": "Mirinda"
        },
        {
            "value": 37,
            "label": "Spritee"
        },
        {
            "value": 47,
            "label": "coke"
        },
        {
            "value": 83,
            "label": "DoubleMint"
        },
        {
            "value": 95,
            "label": "Japan Pepsi"
        },
        {
            "value": 100,
            "label": "fanta"
        },
        {
            "value": 105,
            "label": "Cuba Banana"
        },
        {
            "value": 107,
            "label": "Sting"
        },
        {
            "value": 109,
            "label": "Xa xi original"
        },
        {
            "value": 110,
            "label": "Xa xi premium"
        },
        {
            "value": 112,
            "label": "WATER"
        },
        {
            "value": 113,
            "label": "Redbull"
        },
        {
            "value": 115,
            "label": "Pork Noodle"
        },
        {
            "value": 132,
            "label": "TestNoUnitType"
        },
        {
            "value": 135,
            "label": "UI Test Type 001"
        },
        {
            "value": 136,
            "label": "TestNoUnitType003"
        },
        {
            "value": 138,
            "label": "TestNewEvent001"
        },
        {
            "value": 143,
            "label": "Phở bò gói"
        },
        {
            "value": 144,
            "label": "Phờ gà gói"
        },
        {
            "value": 145,
            "label": "Phở nai gói"
        },
        {
            "value": 146,
            "label": "TestNewEvent002"
        }
    ];
    return Promise.resolve(mock);
}

export const mockIngredient = () => {
    const mock = {
        "id": 1,
        "clientId": 1,
        "name": "Milk",
        "code": "11223344",
        "description": "Milk",
        "unit": "bottle",
        "unitType": "whole",
        "createAt": "2021-10-03 22:27:07",
        "updateAt": "2021-10-03 22:27:07"
    };
    return Promise.resolve(mock);
}

export const mockIngredientItem = () => {
    const mock = {
        "id": 17,
        "clientId": 1,
        "ingredient": {
            "id": 23,
            "clientId": 1,
            "name": "Coca cola",
            "code": "00331133",
            "description": "Coca cola",
            "unit": "can",
            "unitType": "whole",
            "createAt": "2021-10-07T02:18:36.061655500Z",
            "updateAt": "2021-10-07T02:18:36.061655500Z"
        },
        "name": "British Coca cola",
        "code": "CCCL01",
        "description": "British Coca cola",
        "unit": "can",
        "unitType": "whole",
        "expiredAt": "2021-12-21"
    };
    return Promise.resolve(mock);
}

export const mockPageInventory = () => {
    const mock = {
        "content": [
            {
                "id": 14,
                "clientId": 1,
                "ingredient": {
                    "id": 2,
                    "clientId": 1,
                    "name": "New Zealand Cow Milk",
                    "code": "NZCM",
                    "description": "New Zealand Cow Milk",
                    "unit": "bottle",
                    "unitType": "whole",
                    "createAt": "2021-10-03 22:27:06",
                    "updateAt": "2021-12-23T08:52:27.675310100Z"
                },
                "name": "New Zealand Cow Milk NZCM",
                "description": "New Zealand Cow Milk",
                "quantity": 0.0,
                "reserved": 0.0,
                "available": 0.0,
                "unit": "bottle",
                "unitType": "whole"
            },
            {
                "id": 15,
                "clientId": 1,
                "ingredient": {
                    "id": 3,
                    "clientId": 1,
                    "name": "Queen land Goat milk",
                    "code": "QLGM",
                    "description": "Queen land Goat Milk",
                    "unit": "bottle",
                    "unitType": "whole",
                    "createAt": "2021-10-03 22:27:08",
                    "updateAt": "2021-10-05T15:22:04.989878200Z"
                },
                "name": "Queen land Goat milk QLGM",
                "description": "Queen land Goat milk",
                "quantity": 0.0,
                "reserved": 0.0,
                "available": 0.0,
                "unit": "bottle",
                "unitType": "whole"
            },
            {
                "id": 16,
                "clientId": 1,
                "ingredient": {
                    "id": 23,
                    "clientId": 1,
                    "name": "Coca cola",
                    "code": "00331133",
                    "description": "Coca cola",
                    "unit": "can",
                    "unitType": "whole",
                    "createAt": "2021-10-07T02:18:36.061655500Z",
                    "updateAt": "2021-10-07T02:18:36.061655500Z"
                },
                "name": "Coca cola 00331133",
                "description": "Coca cola",
                "quantity": 1.0,
                "reserved": 0.0,
                "available": 1.0,
                "unit": "can",
                "unitType": "whole"
            },
            {
                "id": 18,
                "clientId": 1,
                "ingredient": {
                    "id": 28,
                    "clientId": 1,
                    "name": "Mirinda",
                    "code": "88331188",
                    "description": "Mirinda",
                    "unit": "bottle",
                    "unitType": "whole",
                    "createAt": "2021-10-07T14:16:52.357651500Z",
                    "updateAt": "2021-10-07T14:16:52.357651500Z"
                },
                "name": "Mirinda 88331188",
                "description": "Mirinda 88331188",
                "quantity": 0.0,
                "reserved": 0.0,
                "available": 0.0,
                "unit": "bottle",
                "unitType": "whole"
            },
            {
                "id": 19,
                "clientId": 1,
                "ingredient": {
                    "id": 37,
                    "clientId": 1,
                    "name": "Spritee",
                    "code": "12312347",
                    "description": "Sprite",
                    "unit": "bottle",
                    "unitType": "whole",
                    "createAt": "2021-10-22T15:01:35.067161100Z",
                    "updateAt": "2021-11-12T15:10:14.408033500Z"
                },
                "name": "Sprite In Japan",
                "description": "Sprite In Japan",
                "quantity": 0.0,
                "reserved": 0.0,
                "available": 0.0,
                "unit": "bottle",
                "unitType": "whole"
            },
            {
                "id": 20,
                "clientId": 1,
                "ingredient": {
                    "id": 47,
                    "clientId": 1,
                    "name": "coke",
                    "code": "COKE01",
                    "description": "cock",
                    "unit": "pack",
                    "unitType": "whole",
                    "createAt": "2021-10-29T02:55:53.554212300Z",
                    "updateAt": "2021-11-09T14:55:08.244264800Z"
                },
                "name": "Crap Crap234",
                "description": "Sprite 12312347",
                "quantity": 0.0,
                "reserved": 0.0,
                "available": 0.0,
                "unit": "pack",
                "unitType": "whole"
            },
            {
                "id": 21,
                "clientId": 1,
                "ingredient": {
                    "id": 83,
                    "clientId": 1,
                    "name": "DoubleMint",
                    "code": "TestTransactionType987",
                    "description": "TestTransactionType987",
                    "unit": "pack",
                    "unitType": "whole",
                    "createAt": "2021-11-01T15:41:20.399939500Z",
                    "updateAt": "2021-11-01T15:41:20.399939500Z"
                },
                "name": "TestTransactionType987 TestTransactionType987",
                "description": "TestTransactionType987",
                "quantity": 0.0,
                "reserved": 0.0,
                "available": 0.0,
                "unit": "pack",
                "unitType": "whole"
            },
            {
                "id": 25,
                "clientId": 1,
                "ingredient": {
                    "id": 95,
                    "clientId": 1,
                    "name": "Japan Pepsi",
                    "code": "JP0012123",
                    "description": "Japan Pepsy",
                    "unit": "can",
                    "unitType": "whole",
                    "createAt": "2021-11-02T13:54:39.620574900Z",
                    "updateAt": "2021-11-03T06:59:49.281972300Z"
                },
                "name": "Japan Pepsi JP0012123",
                "description": "Japan Pepsi",
                "quantity": 8.0,
                "reserved": 0.0,
                "available": 8.0,
                "unit": "can",
                "unitType": "whole"
            },
            {
                "id": 26,
                "clientId": 1,
                "ingredient": {
                    "id": 100,
                    "clientId": 1,
                    "name": "fanta",
                    "code": "FANTA",
                    "description": "fanta",
                    "unit": "can",
                    "unitType": "whole",
                    "createAt": "2021-11-03T07:02:04.799679Z",
                    "updateAt": "2021-11-03T07:02:04.799679Z"
                },
                "name": "fanta FANTA",
                "description": "fanta",
                "quantity": 0.0,
                "reserved": 0.0,
                "available": 0.0,
                "unit": "can",
                "unitType": "whole"
            },
            {
                "id": 27,
                "clientId": 1,
                "ingredient": {
                    "id": 105,
                    "clientId": 1,
                    "name": "Cuba Banana",
                    "code": "23133121241241",
                    "description": "12e12e12",
                    "unit": "kilogram",
                    "unitType": "weight",
                    "createAt": "2021-11-03T07:02:04.799679Z",
                    "updateAt": "2021-11-03T07:02:04.799679Z"
                },
                "name": "Chúi 1 23133121241241",
                "description": "12e12e12",
                "quantity": 0.0,
                "reserved": 0.0,
                "available": 0.0,
                "unit": "kilogram",
                "unitType": "weight"
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
        "last": false,
        "totalPages": 3,
        "totalElements": 23,
        "number": 0,
        "size": 10,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "first": true,
        "numberOfElements": 10,
        "empty": false
    }
    return Promise.resolve(mock);
}

export const mockImportSimple = () => {
    const mockData = [
        {
            "value": 1,
            "label": "API_Child_02-IMPORTG1001"
        },
        {
            "value": 2,
            "label": "API_Child_02-IMPORTG1002"
        },
        {
            "value": 3,
            "label": "API_Child_02-IMPORTG1003"
        },
        {
            "value": 4,
            "label": "API_Child_03-import_Code_001"
        },
        {
            "value": 5,
            "label": "API_Child_03-import_Code_002_edited"
        },
        {
            "value": 6,
            "label": "API_Child_01-Import 01"
        },
        {
            "value": 7,
            "label": "API_Child_01-Import 02 edited"
        },
        {
            "value": 8,
            "label": "API_Child_01-Import 03"
        },
        {
            "value": 9,
            "label": "API_Child_01-Import 04"
        },
        {
            "value": 10,
            "label": "API_Child_01-Import 05"
        },
        {
            "value": 11,
            "label": "API_Child_01-Import 06"
        },
        {
            "value": 12,
            "label": "WinmartBranch01-Import001-20211201"
        },
        {
            "value": 13,
            "label": "WinmartBranch01-Import002-20220101"
        },
        {
            "value": 14,
            "label": "WinmartBranch01-Import003-20220201"
        }
    ]
    return Promise.resolve(mockData);
}

export const mockSuggestTaxon = () => {
    const mock = [
        {
            "recipe": {
                "id": 19,
                "name": "Binomo Recipe",
                "description": "Binomo_Recipe_01",
                "updateAt": "2021-12-19T03:02:53.136035400Z",
                "createAt": "2021-12-19T03:02:53.136035400Z",
                "accessAt": "2022-01-12T08:48:23.254674200Z",
                "active": true
            },
            "details": [
                {
                    "id": 8,
                    "name": "Binomo_Recipe_01_detail",
                    "description": "Binomo_Recipe_01_detail",
                    "updateAt": "2021-12-10T15:38:02.524862500Z",
                    "accessAt": "2022-01-12T08:48:23.275763300Z",
                    "code": "Binomo_Recipe_01_detail",
                    "ingredient": {
                        "id": 23,
                        "name": "Coca cola",
                        "code": "00331133",
                        "description": "Coca cola",
                        "unit": "can",
                        "unitType": "whole"
                    },
                    "quantity": 1.0,
                    "active": true
                }
            ],
            "taxonQuantity": 1,
            "expiredTime": "2022-01-12T14:45:21.085078100",
            "confirmed": false
        }
    ]

    return Promise.resolve(mock);
}

export const mockGetPageEvent = () => {
    const mockData = {
        "content": [
            {
                "id": 2,
                "name": "Event 02",
                "description": "{}",
                "occurAt": "2022-01-29 21:05:33",
                "eventType": "Ingredient item quantity is low",
                "active": true,
                "accessAt": "2022-02-03T09:08:48.502925500Z"
            },
            {
                "id": 1,
                "name": "Event 01",
                "description": "{}",
                "occurAt": "2022-01-29 21:05:33",
                "eventType": "Notification of mail sending",
                "active": true,
                "accessAt": "2022-02-03T09:08:48.503922Z"
            }
        ],
        "pageable": {
            "sort": {
                "empty": false,
                "unsorted": false,
                "sorted": true
            },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 2,
            "paged": true,
            "unpaged": false
        },
        "totalElements": 2,
        "totalPages": 1,
        "last": true,
        "number": 0,
        "size": 2,
        "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
        },
        "first": true,
        "numberOfElements": 2,
        "empty": false
    }

    return Promise.resolve(mockData);
}

export const mockGetPageNotification = () => {
    const mockData = {
        "content": [
            {
                "id": 21,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 1,
                    "name": "Event 01",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Notification of mail sending",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.152693800Z"
                },
                "message": {
                    "subject": "Test message",
                    "body": "JSON parse error",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "email",
                "notifyAt": "2022-01-31T03:13:46.971377900Z",
                "status": "complete",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.156220Z"
            },
            {
                "id": 22,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 2,
                    "name": "Event 02",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Ingredient item quantity is low",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.156220Z"
                },
                "message": {
                    "subject": "Test message",
                    "body": "JSON parse error",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "email",
                "notifyAt": "2022-01-31T03:13:46.723386100Z",
                "status": "complete",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.156220Z"
            },
            {
                "id": 23,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 1,
                    "name": "Event 01",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Notification of mail sending",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.156220Z"
                },
                "message": {
                    "subject": "Test message",
                    "body": "JSON parse error",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "sms",
                "status": "created",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.156220Z"
            },
            {
                "id": 24,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 2,
                    "name": "Event 02",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Ingredient item quantity is low",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.156220Z"
                },
                "message": {
                    "subject": "Test message",
                    "body": "JSON parse error",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "sms",
                "notifyAt": "",
                "status": "created",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.156220Z"
            },
            {
                "id": 25,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 1,
                    "name": "Event 01",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Notification of mail sending",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.156220Z"
                },
                "message": {
                    "subject": "Test message",
                    "body": "JSON parse error",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "email",
                "notifyAt": "2022-01-31T02:25:12.950518100Z",
                "status": "complete",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.156220Z"
            },
            {
                "id": 26,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 1,
                    "name": "Event 01",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Notification of mail sending",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.156220Z"
                },
                "message": {
                    "subject": "Test message",
                    "body": "JSON parse error",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "email",
                "notifyAt": "2022-01-31T03:13:01.581288400Z",
                "status": "complete",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.156220Z"
            },
            {
                "id": 27,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 2,
                    "name": "Event 02",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Ingredient item quantity is low",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.156220Z"
                },
                "message": {
                    "subject": "Test message",
                    "body": "JSON parse error",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "email",
                "notifyAt": "2022-01-31T03:50:37.903577Z",
                "status": "complete",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.156220Z"
            },
            {
                "id": 28,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 2,
                    "name": "Event 02",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Ingredient item quantity is low",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.156220Z"
                },
                "message": {
                    "subject": "Test message",
                    "body": "JSON parse error",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "email",
                "status": "created",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.156220Z"
            },
            {
                "id": 29,
                "name": "New notification edited",
                "description": "Notification description edited",
                "event": {
                    "id": 2,
                    "name": "Event 02",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Ingredient item quantity is low",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.156220Z"
                },
                "message": {
                    "subject": "Test message edited",
                    "body": "JSON parse error edited",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "email",
                "notifyAt": "2022-01-31T13:20:48.344639800Z",
                "status": "complete",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.157216600Z"
            },
            {
                "id": 30,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 2,
                    "name": "Event 02",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Ingredient item quantity is low",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.157216600Z"
                },
                "message": {
                    "subject": "Test message edited",
                    "body": "JSON parse error edited",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "email",
                "notifyAt": "2022-01-31T15:03:40.933744600Z",
                "status": "complete",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.157216600Z"
            },
            {
                "id": 31,
                "name": "New notification",
                "description": "Notification description",
                "event": {
                    "id": 2,
                    "name": "Event 02",
                    "description": "{}",
                    "occurAt": "2022-01-29 21:05:33",
                    "eventType": "Ingredient item quantity is low",
                    "active": true,
                    "accessAt": "2022-02-03T09:11:27.157216600Z"
                },
                "message": {
                    "subject": "Test message",
                    "body": "JSON parse error",
                    "from": "noreply@rim.com",
                    "to": "liem18112000@gmail.com"
                },
                "type": "email",
                "notifyAt": "2022-01-31T15:06:02.460707200Z",
                "status": "complete",
                "active": true,
                "accessAt": "2022-02-03T09:11:27.157216600Z"
            }
        ],
        "pageable": {
            "sort": {
                "empty": true,
                "unsorted": true,
                "sorted": false
            },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 20,
            "paged": true,
            "unpaged": false
        },
        "totalElements": 11,
        "totalPages": 1,
        "last": true,
        "number": 0,
        "size": 20,
        "sort": {
            "empty": true,
            "unsorted": true,
            "sorted": false
        },
        "first": true,
        "numberOfElements": 11,
        "empty": false
    }

    return Promise.resolve(mockData);
}

export const mockGetNotificationById = () => {
    const mockData = {
        "id": 8,
        "name": "New notification",
        "description": "Notification description",
        "event": {
            "id": 1,
            "name": "Event 01",
            "description": "{}",
            "occurAt": "2022-01-29 21:05:33",
            "eventType": "Notification of mail sending",
            "active": true,
            "accessAt": "2022-01-30T12:48:03.754968400Z"
        },
        "message": {
            "subject": "Test message",
            "body": "JSON parse error",
            "sendAt": "2022-01-30T12:47:23.652473300Z",
            "from": "noreply@rim.com",
            "to": "liem18112000@gmail.com"
        },
        "type": "email",
        "notifyAt": "2022-01-30T12:47:36.276748400Z",
        "status": "complete",
        "active": true,
        "accessAt": "2022-01-30T12:48:03.754968400Z"
    };

    return Promise.resolve(mockData);
}

export const mockGetEventById = () => {
    const mockData = {
        "id": 1,
        "name": "Event 01",
        "description": "{}",
        "occurAt": "2022-01-29 21:05:33",
        "eventType": "Notification of mail sending",
        "active": true,
        "accessAt": "2022-02-03T09:23:03.066421100Z"
    }

    return Promise.resolve(mockData);
}

export const mockGetEventTypes = () => {
    const mockData = [
        {
            "value": "Notification of mail sending",
            "label": "NOTIFICATION OF MAIL SENDING"
        },
        {
            "value": "Ingredient item quantity is low",
            "label": "INGREDIENT ITEM QUANTITY IS LOW"
        }
    ];

    return Promise.resolve(mockData);
}

export const mockGetNotificationTypes = () => {
    const mockData = [
        {
            "value": "email",
            "label": "EMAIL"
        },
        {
            "value": "sms",
            "label": "SMS"
        }
    ];

    return Promise.resolve(mockData);
}

export const mockGetNotificationStatuses = () => {
    const mockData = [
        {
            "value": "created",
            "label": "CREATED"
        },
        {
            "value": "failed",
            "label": "FAILED"
        },
        {
            "value": "sending",
            "label": "SENDING"
        },
        {
            "value": "complete",
            "label": "COMPLETE"
        }
    ];

    return Promise.resolve(mockData);
}