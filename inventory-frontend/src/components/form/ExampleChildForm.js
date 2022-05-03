import {IngredientService} from "../../service/IngredientService";
import {useEffect, useState} from "react";
import {getDefaultInputConfig, getDropdownInputConfig, getTextareaInputConfig} from "./FormUtil";
import BaseForm from "./BaseForm";
import {DEFAULT_SIDE_FORM_INPUTS, FORM_CREATE_MODE} from "./config";

const ExampleChildForm = (props) => {

    const {
        refreshData,
        isMock = false,
        additionalData,
        ...formConfig
    } = props;

    let service = new IngredientService();
    service.getById = id => service.getByID(id, isMock);
    service.save = data => service.saveIngredient(data, isMock);
    service.update = data => service.updateIngredient(data, isMock);

    const [unitType, setUnitType] = useState([]);
    const [unit, setUnit] = useState([]);
    const [data, setData] = useState({unitType: ""});
    const [formMode, setFormMode] = useState(FORM_CREATE_MODE);

    useEffect(() => {
        service
            .getUnitTypes(false)
            .then(setUnitType);
    }, [])

    useEffect(() => {
        if (data && data.unitType && data.unitType !== "") {
            service
                .getUnit(data.unitType, false)
                .then(setUnit)
        } else {
            setUnit([])
        }
    }, [data.unitType])

    const isCreateMode = formMode === FORM_CREATE_MODE;

    const formInputs = isCreateMode ? [
        getDefaultInputConfig("name"),
        getDefaultInputConfig("code"),
        getDropdownInputConfig("unitType", unitType),
        getDropdownInputConfig("unit", unit),
        getTextareaInputConfig("description"),
    ] : DEFAULT_SIDE_FORM_INPUTS;

    return (
        <BaseForm
            additionalData={additionalData}
            formInputs={formInputs}
            service={service}
            title={"ingredient type"}
            refreshData={refreshData}
            obtainData={setData}
            obtainMode={setFormMode}
            {...formConfig}
        />
    )
}

export default ExampleChildForm;