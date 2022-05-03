import React, { useRef } from 'react';
import '../../assets/styles/TableDemo.css';
import 'primeflex/primeflex.css';
import { Toast } from 'primereact/toast';
import BaseTable from "../../components/table/BaseTable";
import {
    ADDITIONAL_COLUMNS,
    COMPONENT_TITLE,
    DELETE_FAILED_MESSAGE,
    DELETE_SUCCESS_MESSAGE,
    getNavigateViewLink,
    getNavigateViewState,
    getService
} from "./config";
import {getDeleteActionItem} from "../../components/table/TableUtil";
import IngredientCategoryForm from "./IngredientCategoryForm";

const IngredientCategory = (props) => {

    const {
        isMock = false,
        ...tableConfig
    } = props

    const toast = useRef(null);

    const service = getService(isMock);

    const getAdditionalActionItems = (rowData, refresh) => [
        getDeleteActionItem(service, rowData, refresh, toast, DELETE_SUCCESS_MESSAGE, DELETE_FAILED_MESSAGE)
    ];

    return (
        <>
            <Toast ref={toast} />
            <BaseTable
                service={service}
                name={COMPONENT_TITLE}
                additionalColumns={ADDITIONAL_COLUMNS}
                Form={IngredientCategoryForm}
                getNavigateViewLink={getNavigateViewLink}
                getNavigateViewState={getNavigateViewState}
                getAdditionalActionItems={getAdditionalActionItems}
                isMock={isMock}
                {...tableConfig}
            />
        </>
    )
}

export default IngredientCategory;
