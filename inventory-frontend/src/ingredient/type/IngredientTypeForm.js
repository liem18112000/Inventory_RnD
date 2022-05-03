import React, {useEffect, useState} from 'react';
import {DEFAULT_SIDE_FORM_INPUTS, FORM_CREATE_MODE} from "../../components/form/config";
import {
    getDefaultInputConfig,
    getDropdownInputConfig,
    getTextareaInputConfig
} from "../../components/form/FormUtil";
import BaseForm from "../../components/form/BaseForm";
import { getService } from "./config";

/**
 * Ingredient form for save or update ingredient form information
 */
const IngredientTypeForm = (props) => {

    const {
        refreshData,
        isMock = false,
        additionalData,
        ...formConfig
    } = props;

    const {
        parentId
    } = additionalData

    const service = getService(parentId, isMock);

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

export default IngredientTypeForm;