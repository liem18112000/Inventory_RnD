import React from 'react';
import BaseForm from "../../components/form/BaseForm";
import {COMPONENT_TITLE, FORM_INPUTS, getService} from "./config";

/**
 * Ingredient form for save or update ingredient form information
 */
const IngredientCategoryForm = (props) => {

    const {
        refreshData,
        isMock = false,
        ...formConfig
    } = props;

    const service = getService(isMock);

    return (
        <BaseForm
            formInputs={FORM_INPUTS}
            service={service}
            title={COMPONENT_TITLE}
            refreshData={refreshData}
            {...formConfig}
        />
    )
}

export default IngredientCategoryForm;
