import {
    capitalizeTheFirstLetter
} from "../../components/table/TableUtil";
import {IngredientService} from "../../service/IngredientService";

const COMPONENT_TITLE = "ingredient detail";

const DELETE_SUCCESS_MESSAGE = `${capitalizeTheFirstLetter(COMPONENT_TITLE)} has been deleted`;

const DELETE_FAILED_MESSAGE = `${capitalizeTheFirstLetter(COMPONENT_TITLE)} may has recipe detail or material referenced`;

const NAVIGATE_BACK_LABEL = "Back to ingredient category";

const getService = (parentId, isMock = false) => {
    let service = new IngredientService();
    service.getById = id => service.getByID(id, isMock);
    service.save = data => service.saveIngredient(data, isMock);
    service.update = data => service.updateIngredient(data, isMock);
    service.getData = (filter, page, rows, sortField, sortOrder) =>
        service.getPageType(parentId, filter, page, rows, sortField, sortOrder, isMock);
    service.delete = id => service.deleteIngredient(id, isMock);
    return service;
}

const getNavigateViewLink = rowData => `/ingredient/type/${rowData.id}`;

const getNavigateBackLink = props => "/ingredient";

const getNavigateViewState = (rowData, props) => ({
    cateId: props.location.state.cateId,
    unit: rowData.unit,
    unitType: rowData.unitType,
});

export {
    COMPONENT_TITLE,
    DELETE_SUCCESS_MESSAGE,
    DELETE_FAILED_MESSAGE,
    NAVIGATE_BACK_LABEL,
    getService,
    getNavigateViewLink,
    getNavigateBackLink,
    getNavigateViewState
}