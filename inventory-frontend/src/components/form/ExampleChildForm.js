import {IngredientService} from "../../service/IngredientService";
import {useEffect, useState} from "react";
import {getDefaultInputConfig, getDropdownInputConfig, getTextareaInputConfig} from "./FormUtil";
import BaseForm from "./BaseForm";

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
        }
    }, [data.unitType])

    const formInputs = [
        getDefaultInputConfig("name"),
        getDefaultInputConfig("code"),
        getDropdownInputConfig("unitType", unitType),
        getDropdownInputConfig("unit", unit),
        getTextareaInputConfig("description"),
    ]

    return (
        <BaseForm
            additionalData={additionalData}
            formInputs={formInputs}
            service={service}
            title={"ingredient type"}
            refreshData={refreshData}
            obtainData={setData}
            {...formConfig}
        />
    )
}

export default ExampleChildForm;