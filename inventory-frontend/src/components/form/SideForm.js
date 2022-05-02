import {Sidebar} from "primereact/sidebar";
import {Button} from "primereact/button";
import React, {useEffect, useState} from "react";
import {DEFAULT_SIDE_FORM_CONFIG, DEFAULT_SIDE_FORM_INPUTS} from "./config";
import {getFormDataModel} from "./FormUtil";
import {capitalizeTheFirstLetter, capitalizeTheFirstLetterOfEachWord} from "../table/TableUtil";

const SideForm = (props) => {

    const {
        title = "Form",
        formInputs = DEFAULT_SIDE_FORM_INPUTS,
        formData = getFormDataModel(formInputs),
        visible = false,
        setVisible,

        onBeforeSubmit,
        onSubmit,
        onAfterSuccessSubmit,
        onAfterFailedSubmit,
        onSubmitException,

        onFailedValidate,

        submitLabel = "Submit",
        cancelLabel = "Cancel",

        onBeforeCloseForm,
        onAfterCloseForm,

        obtainData,
        obtainErrors,

        ...otherConfigs
    } = props;

    const [errors, setErrors] = useState([]);
    const [data, setData] = useState(formData);

    useEffect(() => {
        if (formData) {
            setData(formData);
            onObtainData(formData);
            onObtainErrors(errors)
        } else {
            setData(getFormDataModel(formInputs));
        }
    }, [formData])

    const onObtainData = (d) => {
        if (obtainData) {
            obtainData(d);
        }
    }

    const onObtainErrors = (e) => {
        if (obtainErrors) {
            obtainErrors(e);
        }
    }

    const onHide = () => {
        setVisible(false);
        setErrors([]);
        setData(getFormDataModel(formInputs));
    }

    const onCloseForm = () => {
        if (onBeforeCloseForm) {
            onBeforeCloseForm();
        }

        onHide();

        if (onAfterCloseForm) {
            onAfterCloseForm();
        }
    }

    const onValidateInputs = (formValue, inputs) => inputs
        .reduce((allErrors, input) => {
            const { field, validators } = input;
            const value = formValue[field];
            if (validators.length > 0) {
                const e = validators.reduce((acc, validator) => {
                    const err = validator.operator(value, field);
                    if (err) {
                        return [...acc, capitalizeTheFirstLetter(err)]
                    }
                    return acc;
                }, [])
                if (e.length > 0) {
                    let tempErrors = {...allErrors};
                    tempErrors[field] = e;
                    return tempErrors
                }
            }
            return allErrors;
        }, {});

    const onHandleSubmit = (formValue, inputs) => {
        const validatedErrors = onValidateInputs(formValue, inputs);
        const hasErrors = Object.keys(validatedErrors).length > 0;

        if (onBeforeSubmit) {
            onBeforeSubmit(formValue, validatedErrors);
        }

        if (hasErrors) {
            setErrors(validatedErrors);
            if (onFailedValidate) {
                onFailedValidate(formValue, validatedErrors)
            }
            return;
        }

        onSubmit(formValue)
            .then(success => {
                if (success) {
                    if (onAfterSuccessSubmit) {
                        onAfterSuccessSubmit();
                        onHide();
                    }
                } else {
                    if (onAfterFailedSubmit) {
                        onAfterFailedSubmit();
                    }
                }
            }).catch(onSubmitException);
    }



    const renderInputs = formInputs => {
        if (!formInputs) {
            return ;
        }

        return formInputs.map((input, index) => {
            const {
                field,
                InputComponent,
                inputProps,
                getValueFromEvent = (event) => event.target.value,
                validators,
                formatInput
            } = input;

            const onInputValueChange = (event) => {
                let tempData = {...data};
                tempData[field] = formatInput(getValueFromEvent(event));
                setData({...tempData})
            }

            const inputErrors = errors && errors[field] ? errors[field] : null;

            const renderLabel = (key, field) => {
                return  <label key={key}>
                    {capitalizeTheFirstLetterOfEachWord(field)}
                </label>
            }

            const renderErrors = (inputErrors) => {
                if (!inputErrors || inputErrors.length === 0) {
                    return;
                }

                return inputErrors.map((error, index) => (<small
                    key={`error-${index}`}
                    className="p-error p-d-block">
                    {error}
                </small>));
            }

            const renderInput = (key, value, config, hasValidator = false, hasErrors = false) => {
                console.log(config)
                if (hasValidator) {
                    return (
                        <span className="p-input-icon-right">
                            <i className="pi pi-info-circle"  />
                            <InputComponent
                                className={hasErrors ? "p-invalid" : ""}
                                key={key}
                                value={value}
                                onChange={onInputValueChange}
                                {...config}
                            />
                        </span>
                    )
                }
                return (
                    <InputComponent
                        key={key}
                        value={value}
                        onChange={onInputValueChange}
                        {...config}
                    />
                )
            }

            return (
                <div className="p-col-12">
                    {renderLabel(`label-${index}`, field)}
                    {renderInput(
                        `input-${index}`,
                        data ? data[field] : null,
                        inputProps,
                        validators.length > 0,
                        errors ? errors[field] : false)}
                    {renderErrors(inputErrors)}
                </div>
            )
        })
    }

    return (
        <Sidebar visible={visible}
                 onHide={onHide}
                 {...DEFAULT_SIDE_FORM_CONFIG}
                 {...otherConfigs}>
            <h2>{title}</h2>
            <div className="p-grid p-fluid">
                {renderInputs(formInputs)}
            </div>
            <div className="p-grid">
                <div className="p-col-12 p-r p-margin-top-30 p-line-top">
                    <Button label={submitLabel}
                            icon="pi pi-check"
                            onClick={() => onHandleSubmit(data, formInputs)} />
                    <Button label={cancelLabel}
                            icon="pi-md-close"
                            className="p-button-secondary p-ml-2"
                            onClick={onCloseForm} />
                </div>
            </div>
        </Sidebar>
    );
}

export default SideForm;