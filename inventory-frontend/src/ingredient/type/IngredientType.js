import React, {useEffect, useRef, useState} from 'react';
import '../../assets/styles/TableDemo.css'
import { Toast } from 'primereact/toast';
import { IngredientTypeConfig } from './IngredientTypeConfig';
import BaseTable from "../../components/table/BaseTable";
import {
    getDateColumnConfig,
    getDeleteActionItem,
    getDropdownColumnConfig,
    getNumericColumnConfig
} from "../../components/table/TableUtil";
import {
    COMPONENT_TITLE,
    DELETE_FAILED_MESSAGE,
    DELETE_SUCCESS_MESSAGE,
    getNavigateBackLink, getNavigateViewState,
    getService,
    NAVIGATE_BACK_LABEL
} from "./config";
import IngredientTypeForm from "./IngredientTypeForm";
import {getNavigateViewLink} from "./config";
import {
    BREADCRUMB_HOME_MODEL,
    getBreadcrumbIngredientTypeModel
} from "../../components/common/breadcrumModel";
import {BreadCrumb} from "primereact/breadcrumb";

const IngredientType = (props) => {
    const {
        match,
        isMock = false
    } = props;

    const parentId = match.params.id

    const toast = useRef(null);

    const configForm = useRef(null);

    const service = getService(parentId, isMock);

    const [unitType, setUnitType] = useState([]);

    const [unit, setUnit] = useState([]);

    const [filter, setFilter] = useState({unitType: ""});

    const [breadcrumbModel, setBreadcrumbModel] = useState(
        getBreadcrumbIngredientTypeModel());

    const getAdditionalActionItems = (rowData, refresh) => [
        {
            key: "history-option",
            label: 'History',
            icon: 'pi pi-external-link',
            command: (e) => {
                props.history.push({
                    pathname: `./history/${rowData.id}`,
                    state: {
                        cateId: parentId,
                        unitType: rowData.unitType,
                        unit: rowData.unit
                    }
                })
            }
        },
        {
            key: "config-option",
            label: 'Config',
            icon: 'pi pi-cog',
            command: (e) => { configForm.current?.action(rowData.id) }
        },
        getDeleteActionItem(service, rowData, refresh, toast, DELETE_SUCCESS_MESSAGE, DELETE_FAILED_MESSAGE)
    ];

    useEffect(() => {
        service
            .getUnitTypes(false)
            .then(setUnitType);
    }, [])

    useEffect(() => {
        if (props?.location?.state?.cateId) {
            service
                .getById(props?.location?.state?.cateId)
                .then(data => {
                    const { id, name } = data;
                    if (name && id) {
                        setBreadcrumbModel(
                            getBreadcrumbIngredientTypeModel(name, id)
                        )
                    }
                });
        }
    }, [props?.location?.state?.cateId])

    useEffect(() => {
        if (filter && filter.unitType && filter.unitType !== "") {
            service
                .getUnit(filter.unitType, false)
                .then(setUnit)
        } else {
            setUnit([])
        }
    }, [filter.unitType])

    return (
        <>
            <Toast ref={toast} />
            <IngredientTypeConfig
                ref={configForm}
                refreshData={() => {}}
            />
            <BreadCrumb
                model={breadcrumbModel}
                home={BREADCRUMB_HOME_MODEL} />
            <BaseTable
                service={service}
                name={COMPONENT_TITLE}
                additionalColumns={[
                    getDropdownColumnConfig("unitType", unitType),
                    getDropdownColumnConfig("unit", unit),
                    getNumericColumnConfig("quantity", false, false),
                    getDateColumnConfig("createAt", "Create from")
                ]}
                Form={IngredientTypeForm}
                formProps={{
                    additionalData: {
                        parentId: parentId
                    }
                }}
                navigateBackLabel={NAVIGATE_BACK_LABEL}
                getNavigateBackLink={getNavigateBackLink}
                getNavigateViewState={getNavigateViewState}
                getNavigateViewLink={getNavigateViewLink}
                obtainFilter={setFilter}
                getAdditionalActionItems={getAdditionalActionItems}
                {...props}
            />
        </>

    )
}

export default IngredientType;