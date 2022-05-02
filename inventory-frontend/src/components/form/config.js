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

export {
    DEFAULT_SIDE_FORM_CONFIG,
    DEFAULT_SIDE_FORM_INPUTS
};