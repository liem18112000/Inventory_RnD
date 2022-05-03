import React, {useEffect, useRef, useState} from 'react';
import '../../assets/styles/TableDemo.css'
import { Toast } from 'primereact/toast';
import { IngredientTypeConfig } from './IngredientTypeConfig';
import BaseTable from "../../components/table/BaseTable";
import {
    getDateColumnConfig, getDeleteActionItem,
    getDropdownColumnConfig,
    getNumericColumnConfig
} from "../../components/table/TableUtil";
import {DELETE_FAILED_MESSAGE, DELETE_SUCCESS_MESSAGE} from "./config";
import {COMPONENT_TITLE, getNavigateBackLink, getService, NAVIGATE_BACK_LABEL} from "./config";
import IngredientTypeForm from "./IngredientTypeForm";

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

    const getAdditionalActionItems = (rowData, refresh) => [
        getDeleteActionItem(service, rowData, refresh, toast, DELETE_SUCCESS_MESSAGE, DELETE_FAILED_MESSAGE)
    ];

    useEffect(() => {
        service
            .getUnitTypes(false)
            .then(setUnitType);
    }, [])

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
                obtainFilter={setFilter}
                getAdditionalActionItems={getAdditionalActionItems}
                {...props}
            />
        </>

    )
}

export default IngredientType;