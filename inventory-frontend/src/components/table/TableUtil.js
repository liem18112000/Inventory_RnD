import React from "react";
import {Calendar} from "primereact/calendar";
import {convertDateToEnCADate, sleep} from "../../core/utility/ComponentUtility";
import moment from "moment";
import {SplitButton} from "primereact/splitbutton";
import {Button} from "primereact/button";
import {InputNumber} from "primereact/inputnumber";
import {Dropdown} from "primereact/dropdown";
import {DEFAULT_TOAST_INTERVAL} from "./config";
import {confirmDialog} from "primereact/confirmdialog";

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

const getDefaultColumnConfig = (field, sort = true, filter = true) => {
    return {
        field: field,
        filterConfig: {
            enabled: filter,
            defaultValue: "",
            inputProps: {
                placeholder: field.charAt(0).toUpperCase() + field.slice(1)
            }
        },
        columnConfig: {
            sortable: sort,
            body: rowData => <React.Fragment>
                    <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>
                        {rowData[field]}
                    </span>
            </React.Fragment>
        }
    }
}

const getDateColumnConfig = (field, placeholder, dateFormat = "yy-mm-dd", sort = true, filter = true) => {
    return {
        field: field,
        filterConfig: {
            enabled: filter,
            defaultValue: "",
            InputComponent: Calendar,
            formatInput: input => convertDateToEnCADate(input),
            inputProps: {
                dateFormat: dateFormat,
                placeholder: placeholder
            }
        },
        columnConfig: {
            sortable: sort,
            body: rowData => <React.Fragment>
                    <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>
                        {moment(rowData.createAt).format('HH:mm-A-ddd-DD/MMM/YYYY')}
                    </span>
            </React.Fragment>
        }
    }
}

const getNumericColumnConfig = (field, sort = true, filter = true) => {
    return {
        field: field,
        filterConfig: {
            enabled: filter,
            defaultValue: null,
            InputComponent: InputNumber,
            inputProps: {
                placeholder: field.charAt(0).toUpperCase() + field.slice(1),
                min: 0,
                max: 9999,
                mode: "decimal",
                buttonLayout: "horizontal",
                showButtons: true
            },
            getValueFromEvent: event => event ? event.value : 0,
            formatInput: input => input ? parseInt(input, 10) : 0,
        },
        columnConfig: {
            sortable: sort,
            body: rowData => <React.Fragment>
                    <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>
                        {rowData[field]}
                    </span>
            </React.Fragment>
        }
    }
}

const getDropdownColumnConfig = (field, options = [], sort = true, filter = true, placeholder = null) => {
    return {
        field: field,
        filterConfig: {
            enabled: filter,
            defaultValue: "",
            InputComponent: Dropdown,
            inputProps: {
                disabled: options.length === 0,
                options: options,
                placeholder: !placeholder ? splitCamelCaseWord(field) : placeholder
            }
        },
        columnConfig: {
            sortable: sort,
            body: rowData => <React.Fragment>
                    <span style={{ verticalAlign: 'middle', marginRight: '.6em' }}>
                        {rowData[field]}
                    </span>
            </React.Fragment>
        }
    }
}

const getActionColumnConfig = (onClick, getItemModel, actionLabel = "View") => {
    return {
        field: "action",
        filterConfig: {
            enabled: false
        },
        columnConfig: {
            sortable: false,
            body: rowData => {
                if (!getItemModel) {
                    return (
                        <React.Fragment>
                            <div className="card">
                                <Button label={actionLabel}
                                        onClick={() => onClick(rowData)}>
                                </Button>
                            </div>
                        </React.Fragment>
                    );
                }
                return (
                    <React.Fragment>
                        <div className="card">
                            <SplitButton label={actionLabel}
                                         onClick={() => onClick(rowData)}
                                         model={getItemModel(rowData)}>
                            </SplitButton>
                        </div>
                    </React.Fragment>
                );
            }
        }
    }
}

const capitalizeTheFirstLetter = word => {
    if (!word || word.length === 0) {
        return "";
    }
    return word.charAt(0).toUpperCase() + word.substring(1);
}

const capitalizeTheFirstLetterOfEachWord = (words, separator = ' ') => {
    const separateWords = words.toLowerCase().split(separator);
    for (let i = 0; i < separateWords.length; i++) {
        separateWords[i] = capitalizeTheFirstLetter(separateWords[i])
    }
    return separateWords.join(separator);
}

const splitCamelCaseWord = (words) =>
    capitalizeTheFirstLetter(words).replace(/([a-z])([A-Z])/g, '$1 $2');

const getDeleteActionItem =
    (service, rowData, refresh, toast,
     successDetailMessage = "", failedDetailMessage = "") => {

    const onConfirmDelete = (id, refresh) => {
        if (!id) {
            toast.current?.show({
                severity: 'warning',
                summary: 'Delete failed',
                detail: 'Id is not set',
                life: 3000
            })
        } else {
            service.delete(id).then(res => {
                if (res) {
                    toast.current?.show({
                        severity: 'success',
                        summary: 'Delete success',
                        detail: successDetailMessage,
                        life: DEFAULT_TOAST_INTERVAL
                    })
                    sleep(DEFAULT_TOAST_INTERVAL / 2)
                        .then(refresh)
                } else {
                    toast.current?.show({
                        severity: 'error',
                        summary: 'Delete failed',
                        detail: failedDetailMessage,
                        life: 5000
                    })
                }
            })
        }
    }

    const onCancelDelete = () => {
        toast.current?.show({
            severity: 'info',
            summary: 'Cancel delete',
            detail: 'You have cancel delete',
            life: DEFAULT_TOAST_INTERVAL
        })
    }

    const onDelete = (id, refresh) => {
        if (id) {
            confirmDialog({
                message: 'Do you want to delete this item?',
                header: 'Delete Confirmation',
                icon: 'pi pi-info-circle',
                acceptClassName: 'p-button-danger',
                accept: () => onConfirmDelete(id, refresh),
                reject: onCancelDelete
            });
        }
    }

    return {
        key: "delete-option",
        label: 'Delete',
        icon: 'pi pi-trash',
        command: (e) => onDelete(rowData.id, refresh)
    };
}

export {
    getFilterModel,
    getDateColumnConfig,
    getDefaultColumnConfig,
    getActionColumnConfig,
    getNumericColumnConfig,
    getDropdownColumnConfig,
    capitalizeTheFirstLetter,
    capitalizeTheFirstLetterOfEachWord,
    splitCamelCaseWord,
    getDeleteActionItem
}