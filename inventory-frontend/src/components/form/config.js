import {getDefaultInputConfig, getTextareaInputConfig} from "./FormUtil";

const DEFAULT_SIDE_FORM_CONFIG = {
    position: "right",
    style: {
        overflowY: "auto",
        width: "40em"
    },
    blockScroll: true,
    baseZIndex: 1000000
}

const DEFAULT_SIDE_FORM_INPUTS = [
    getDefaultInputConfig("name"),
    getDefaultInputConfig("code"),
    getTextareaInputConfig("description")
]

const FORM_EDIT_MODE = "edit";

const FORM_CREATE_MODE = "create";

export {
    FORM_EDIT_MODE,
    FORM_CREATE_MODE,
    DEFAULT_SIDE_FORM_CONFIG,
    DEFAULT_SIDE_FORM_INPUTS
};