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

export {
    getBreadcrumbIngredientTypeModel,
    getBreadcrumbIngredientCategoryModel,
    getBreadcrumbIngredientItemModel,
    BREADCRUMB_HOME_MODEL,
}