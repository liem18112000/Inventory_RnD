const BREADCRUMB_HOME_MODEL = { icon: 'pi pi-home', url: '/ingredient-inventory' }

const getBreadcrumbIngredientInventoryModel = () => [
    {
        label: 'Ingredient Inventory',
        url: '/ingredient-inventory'
    },
];

const getBreadcrumbIngredientCategoryModel = () => [
    {
        label: 'Ingredient Category',
        url: '/ingredient'
    },
];

const getBreadcrumbIngredientTypeModel = (categoryName, cateId) => [
    ...getBreadcrumbIngredientCategoryModel(categoryName),
    {
        label: categoryName ? categoryName : 'Ingredient Detail',
        url: cateId ? `/ingredient/${cateId}` : "#"
    },
]

const getBreadcrumbIngredientItemModel = (categoryName, typeName, cateId, typeId) => [
    ...getBreadcrumbIngredientTypeModel(categoryName, cateId),
    {
        label: typeName ? typeName : 'Ingredient Item',
        url: typeId ? `/ingredient/type/${typeId}` : "#"
    },
]

const getBreadcrumbIngredientHistoryModel = (categoryName, typeName, cateId, typeId) => [
    ...getBreadcrumbIngredientTypeModel(categoryName, cateId),
    {
        label: typeName ? typeName : 'Ingredient History',
        url: typeId ? `/ingredient/history/${typeId}` : "#"
    },
]

const getBreadcrumbRecipesModel = () => [
    {
        label: 'Recipes',
        url: '/recipes'
    },
];

const getBreadcrumbRecipeGroupModel = () => [
    {
        label: 'Recipe Group',
        url: '/recipe'
    },
];

const getBreadcrumbRecipeChildModel = (groupName, groupId) => [
    ...getBreadcrumbRecipeGroupModel(groupName),
    {
        label: groupName ? groupName : 'Recipe',
        url: groupId ? `/recipe/${groupId}` : "#"
    },
]

const getBreadcrumbRecipeDetailModel = (groupName, typeName, groupId, typeId) => [
    ...getBreadcrumbRecipeChildModel(groupName, groupId),
    {
        label: typeName ? typeName : 'Recipe Detail',
        url: typeId ? `/recipe/child/${typeId}` : "#"
    },
]

const getBreadcrumbSupplierGroupModel = () => [
    {
        label: 'Supplier Group',
        url: '/supplier'
    },
];

const getBreadcrumbSupplierChildModel = (supplierGroupName, supplierGroupId) => [
    ...getBreadcrumbSupplierGroupModel(supplierGroupName),
    {
        label: supplierGroupName ? supplierGroupName : 'Supplier',
        url: supplierGroupId ? `/supplier/${supplierGroupId}` : "#"
    },
]

const getBreadcrumbSupplierMaterialModel = (supplierGroupName, parentName, supplierGroupId, parentId) => [
    ...getBreadcrumbSupplierChildModel(supplierGroupName, supplierGroupId),
    {
        label: parentName ? parentName : 'Supplier Material',
        url: parentId ? `/supplier/material/${parentId}` : "#"
    },
]

const getBreadcrumbSupplierImportModel = (supplierGroupName, parentName, supplierGroupId, parentId) => [
    ...getBreadcrumbSupplierChildModel(supplierGroupName, supplierGroupId),
    {
        label: parentName ? parentName : 'Supplier Import',
        url: parentId ? `/supplier/import/${parentId}` : "#"
    },
]

const getBreadcrumbImportDetailModel = (supplierGroupName, parentName, supplierGroupId, parentId) => [
    ...getBreadcrumbSupplierImportModel(supplierGroupName, supplierGroupId),
    {
        label: parentName ? parentName : 'Import Detail',
        url: parentId ? `/supplier/import/detail/${parentId}` : "#"
    },
]

const getBreadcrumbEventModel = () => [
    {
        label: 'Notification Event',
        url: '/notification/event'
    },
];

const getBreadcrumbNotificationModel = (groupName, typeName, groupId, typeId) => [
    ...getBreadcrumbEventModel(groupName, groupId),
    {
        label: typeName ? typeName : 'Notification',
        url: typeId ? `/notification/event/${typeId}` : "#"
    },
]


export {
    getBreadcrumbIngredientInventoryModel,

    getBreadcrumbIngredientCategoryModel,
    getBreadcrumbIngredientTypeModel,
    getBreadcrumbIngredientItemModel,
    getBreadcrumbIngredientHistoryModel,

    getBreadcrumbRecipesModel,
    getBreadcrumbRecipeGroupModel,
    getBreadcrumbRecipeChildModel,
    getBreadcrumbRecipeDetailModel,

    getBreadcrumbSupplierGroupModel,
    getBreadcrumbSupplierChildModel,
    getBreadcrumbSupplierMaterialModel,
    getBreadcrumbSupplierImportModel,
    getBreadcrumbImportDetailModel,

    getBreadcrumbEventModel,
    getBreadcrumbNotificationModel,

    BREADCRUMB_HOME_MODEL,
}