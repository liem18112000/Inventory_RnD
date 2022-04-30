/**
 * Get filter model from columns
 * @param columns Table columns
 * @returns {{}|*}
 */
const getFilterModel = (columns) => {
    if (!columns) {
        return {}
    }

    return columns.filter(col => col.filterConfig && col.filterConfig.enabled)
        .reduce((filterModel, col) => {
            const { field, filterConfig } = col;
            const { defaultValue = "" } = filterConfig;
            if (field && !Object.keys(filterModel).includes(field)) {
                let tempModel = {...filterModel};
                tempModel[field] = defaultValue;
                return {...tempModel}
            }
            return filterModel;
        }, {});
}

export {
    getFilterModel
}