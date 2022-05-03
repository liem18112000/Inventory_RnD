import {capitalizeTheFirstLetter, getDateColumnConfig} from "../../components/table/TableUtil";
import { IngredientService } from "../../service/IngredientService";
import { DEFAULT_SIDE_FORM_INPUTS } from "../../components/form/config";

const ADDITIONAL_COLUMNS = [ getDateColumnConfig("createAt", "Create from") ];

const FORM_INPUTS = DEFAULT_SIDE_FORM_INPUTS;

const COMPONENT_TITLE = "ingredient category";

const DELETE_SUCCESS_MESSAGE = `${capitalizeTheFirstLetter(COMPONENT_TITLE)} has been deleted`;

const DELETE_FAILED_MESSAGE = `${capitalizeTheFirstLetter(COMPONENT_TITLE)} may has ingredient details, recipe detail or material referenced`;

const getService = (isMock = false) => {
    let service = new IngredientService();
    service.getById = id => service.getByID(id, isMock);
    service.save = data => service.saveIngredient(data, isMock);
    service.update = data => service.updateIngredient(data, isMock);
    service.getData = (filter, page, rows, sortField, sortOrder) =>
        service.getPageCategory(filter, page, rows, sortField, sortOrder, isMock);
    service.delete = id => service.deleteIngredient(id, isMock);
    return service;
}

const getNavigateViewLink = rowData => `/ingredient/${rowData.id}`;

export {
    FORM_INPUTS,
    ADDITIONAL_COLUMNS,
    COMPONENT_TITLE,
    DELETE_SUCCESS_MESSAGE,
    DELETE_FAILED_MESSAGE,
    getService,
    getNavigateViewLink
}