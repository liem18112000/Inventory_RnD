import SideForm from "./SideForm";
import React, {useEffect, useRef, useState} from "react";
import {
    handleExceptionWithSentry
} from "../../core/utility/integrations/SentryExceptionResolver";
import {Toast} from "primereact/toast";
import {DEFAULT_TOAST_INTERVAL} from "../table/config";
import {getFormDataModel} from "./FormUtil";
import {FORM_CREATE_MODE, FORM_EDIT_MODE} from "./config";

const BaseForm = (props) => {

    const {
        title = "form",
        formInputs,
        additionalData = null,
        service,
        visible = false,
        setVisible,
        id = null,
        refreshData,
        obtainMode,
        ...formConfig
    } = props;

    const editFormTitle = `Edit ${title}`;
    const createFormTitle = `New ${title}`;
    const toast = useRef(null);
    const toastInterval = DEFAULT_TOAST_INTERVAL;

    const [formTitle, setFormTitle] = useState(createFormTitle);
    const [formData, setFormData] = useState(null);
    const [formMode, setFormMode] = useState(FORM_CREATE_MODE);

    useEffect(() => {
        if (visible) {
            if (id != null) {
                setFormTitle(editFormTitle);
                onUpdateInformation(id);
                setFormMode(FORM_EDIT_MODE);
            } else {
                setFormTitle(createFormTitle);
                onCreateInformation()
                setFormMode(FORM_CREATE_MODE);
            }
        }
    }, [visible])

    useEffect(() => {
        if (obtainMode) {
            obtainMode(formMode);
        }
    }, [formMode])

    const onAppendAdditionalData = (_data, _additionalData) => {
        if (_additionalData) {
            return {
                ..._additionalData,
                ..._data,
            }
        }
        return _data;
    }

    /**
     * Get updated and set to update information state
     * @param id id
     */
    const onUpdateInformation = (id) => {
        service.getById(id).then(data => {
            if (data) {
                const defaultData = getFormDataModel(formInputs);
                const finalData = Object.keys(defaultData)
                    .reduce((obj, key) => {
                        let tempObj = {...obj};
                        tempObj[key] = data[key];
                        return tempObj;
                    }, defaultData)
                setFormData(onAppendAdditionalData(finalData, additionalData));
            } else {
                console.log("Get data by id failed");
            }
        }).catch(e => handleExceptionWithSentry(e))
    }

    const onCreateInformation = () => {
        setFormData(onAppendAdditionalData(getFormDataModel(formInputs), additionalData));
    }

    const onSubmit = (formValue) => {
        if (formTitle === editFormTitle) {
            return service.update(formValue)
                .then(res => !!res);
        }
        return service.save(formValue)
            .then(res => !!res);
    }

    const onAfterSuccessSubmit = () => {
        toast.current?.show({
            severity: 'success',
            summary: 'Submit Success',
            detail: formTitle === editFormTitle ? 'Update Success' : "Create Success",
            life: toastInterval
        });

        if (refreshData) {
            setFormData(onAppendAdditionalData(getFormDataModel(formInputs), additionalData));
            refreshData()
        }
    }

    const onAfterFailedSubmit = () => {
        toast.current.show({
            severity: 'error',
            summary: 'Submit Failed',
            detail: `${formTitle} failed`,
            life: toastInterval
        });
    }

    const onFailedValidate = (values, failValidatedValue) => {
        toast.current?.show({
            severity: 'error', summary: 'Validate Failed',
            content: renderFailedValidate(failValidatedValue),
            life: toastInterval
        });
    }

    /**
     * Show and reader validation fail errors
     * @returns {JSX.Element}
     */
    const renderFailedValidate = (failedValidateErrors) => {
        const messages = Object.values(failedValidateErrors);
        return (
            <div className="p-flex p-flex-column" style={{ flex: '1' }}>
                <div className="p-text-center">
                    <h3>Fail Validation</h3>
                </div>
                <div className="p-grid p-fluid">
                    {messages.map((message, index) => (
                        <div className="p-col-12" key={`message-${index}`}>
                            {message}
                        </div>
                    ))}
                </div>
            </div>
        )
    }

    return (
        <>
            <Toast ref={toast} />
            <SideForm
                title={formTitle}
                formInputs={formInputs}
                visible={visible}
                setVisible={setVisible}
                formData={formData}
                onSubmit={onSubmit}
                onAfterSuccessSubmit={onAfterSuccessSubmit}
                onAfterFailedSubmit={onAfterFailedSubmit}
                onFailedValidate={onFailedValidate}
                {...formConfig}
            />
        </>
    )
}

export default BaseForm;