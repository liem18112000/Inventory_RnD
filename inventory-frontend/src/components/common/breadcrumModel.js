const BREADCRUMB_HOME_MODEL = { icon: 'pi pi-home', url: '/ingredient-inventory' }

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

const getBreadcrumbSupplierChildModel = (groupName, groupId) => [
    ...getBreadcrumbSupplierGroupModel(groupName),
    {
        label: groupName ? groupName : 'Supplier',
        url: groupId ? `/supplier/${groupId}` : "#"
    },
]

const getBreadcrumbSupplierMaterialModel = (groupName, typeName, groupId, typeId) => [
    ...getBreadcrumbSupplierChildModel(groupName, groupId),
    {
        label: typeName ? typeName : 'Supplier Material',
        url: typeId ? `/supplier/material/${typeId}` : "#"
    },
]

const getBreadcrumbSupplierImportModel = (groupName, typeName, groupId, typeId) => [
    ...getBreadcrumbSupplierChildModel(groupName, groupId),
    {
        label: typeName ? typeName : 'Supplier Import',
        url: typeId ? `/supplier/import/${typeId}` : "#"
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
    getBreadcrumbIngredientTypeModel,
    getBreadcrumbIngredientCategoryModel,
    getBreadcrumbIngredientItemModel,

    getBreadcrumbRecipeGroupModel,
    getBreadcrumbRecipeChildModel,
    getBreadcrumbRecipeDetailModel,

    getBreadcrumbSupplierGroupModel,
    getBreadcrumbSupplierChildModel,
    getBreadcrumbSupplierMaterialModel,
    getBreadcrumbSupplierImportModel,
    // getBreadcrumbImportDetailModel,

    getBreadcrumbEventModel,
    getBreadcrumbNotificationModel,

    BREADCRUMB_HOME_MODEL,
}