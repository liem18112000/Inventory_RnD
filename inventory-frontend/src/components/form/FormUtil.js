import {InputText} from "primereact/inputtext";
import {InputTextarea} from "primereact/inputtextarea";
import {Dropdown} from "primereact/dropdown";

const REQUIRED = {
    name: "required",
    operator: (value, field = "field") => {
        try {
            if (!value) {
                return `${field} is null`;
            }
            if (value instanceof String) {
                if (value.trim().length === 0) {
                    return `${field} string is empty`;
                }
            }
            if (Array.isArray(value)) {
                if (value.length === 0) {
                    return `${field} array is empty`;
                }
            }
            return null;
        } catch (e) {
            console.log(e);
            return `Handle validate ${field} failed`;
        }
    }
};

const NUMERIC_RANGE = {
    name: "numeric range",
    operator: (value, field = "field", from = 0, to = null, numericParser = parseInt) => {
        const error = REQUIRED.operator(value, field);
        if (error) {
            return error;
        }
        try {
            let numericValue = value;
            if (value instanceof String) {
                numericValue = numericParser(value);
            }

            if (from) {
                let numericFrom = from;
                if (from instanceof String) {
                    numericFrom = numericParser(from);
                }
                if (numericValue < numericFrom) {
                    return `${field} is lower than ${from}`
                }
            }

            if (to) {
                let numericTo = to;
                if (to instanceof String) {
                    numericTo = numericParser(to);
                }
                if (numericValue > numericTo) {
                    return `${field} is larger than ${to}`
                }
            }

            return null;
        } catch (e) {
            console.log(e);
            return `Handle validate ${field} failed`;
        }
    }
}

const getFormDataModel = (inputs) => {
    if (!inputs) {
        return {}
    }

    return inputs
        .reduce((dataModel, input) => {
            const { field, defaultValue } = input;
            if (field && !Object.keys(dataModel).includes(field)) {
                let tempModel = {...dataModel};
                tempModel[field] = defaultValue;
                return {...tempModel}
            }
            return dataModel;
        }, {id: null});
}

const getDefaultInputConfig = (field, validators = [REQUIRED]) => ({
    field: field,
    defaultValue: "",
    InputComponent: InputText,
    validators: validators,
    getValueFromEvent: e => e.target.value,
    formatInput: input => input ? input : "",
    inputProps: {
        placeholder: `Enter ${field}`
    }
})

const getTextareaInputConfig = (field, validators = [], rows = 5) => ({
    field: field,
    defaultValue: "",
    InputComponent: InputTextarea,
    validators: validators,
    getValueFromEvent: e => e.target.value,
    formatInput: input => input ? input : "",
    inputProps: {
        placeholder: `Enter ${field}`,
        rows: rows
    }
})

const getDropdownInputConfig = (field, options = [], validators = [REQUIRED]) => ({
    field: field,
    defaultValue: "",
    InputComponent: Dropdown,
    validators: validators,
    getValueFromEvent: e => e.target.value,
    formatInput: input => input ? input : "",
    inputProps: {
        placeholder: `Enter ${field}`,
        options: options
    }
})

export {
    REQUIRED,
    NUMERIC_RANGE,
    getFormDataModel,
    getDefaultInputConfig,
    getTextareaInputConfig,
    getDropdownInputConfig,
}