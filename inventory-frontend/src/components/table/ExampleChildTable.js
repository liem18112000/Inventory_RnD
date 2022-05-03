import BaseTable from "./BaseTable";
import { IngredientService } from "../../service/IngredientService";
import {
    getDateColumnConfig,
    getDropdownColumnConfig,
    getNumericColumnConfig
} from "./TableUtil";
import React, { useEffect, useRef, useState } from "react";
import ExampleChildForm from "../form/ExampleChildForm";
import { DEFAULT_TOAST_INTERVAL } from "./config";
import { sleep } from "../../core/utility/ComponentUtility";
import { confirmDialog } from "primereact/confirmdialog";
import { IngredientTypeConfig } from "../../ingredient/type/IngredientTypeConfig";
import { Toast } from "primereact/toast";
import { BreadCrumb } from 'primereact/breadcrumb';

const ExampleChildTable = (props) => {
    const {
        match,
        isMock = false
    } = props;

    const parentId = match.params.id

    const toast = useRef(null);

    const configForm = useRef(null);

    let service = new IngredientService();
    service.getData = (filter, page, rows, sortField, sortOrder) =>
        service.getPageType(parentId, filter, page, rows, sortField, sortOrder, isMock);
    service.delete = id => service.deleteIngredient(id, isMock);
    const [unitType, setUnitType] = useState([]);
    const [unit, setUnit] = useState([]);
    const [filter, setFilter] = useState({ unitType: "" });

    const items = [
        { label: 'Ingredient Category', url: '/test' },
        { label: 'Ingredient Detail' }
    ];

    const home = { icon: 'pi pi-home', url: '/ingredient-inventory' }

    const confirmDelete = (id, refresh) => {
        if (!id) {
            toast.current?.show({
                severity: 'warning',
                summary: 'Delete failed',
                detail: 'Id is not set',
                life: 3000
            })
        } else {
            service.delete(id)
                .then(res => {
                    if (res) {
                        toast.current?.show({
                            severity: 'success',
                            summary: 'Delete success',
                            detail: 'Ingredient type has been deleted',
                            life: DEFAULT_TOAST_INTERVAL
                        })
                        sleep(DEFAULT_TOAST_INTERVAL / 2)
                            .then(refresh)
                    } else {
                        toast.current?.show({
                            severity: 'error',
                            summary: 'Delete failed',
                            detail: 'Ingredient type may has ingredient details, recipe detail or material referenced ',
                            life: 5000
                        })
                    }
                })
        }
    }

    const cancelDelete = () => {
        toast.current?.show({
            severity: 'info',
            summary: 'Cancel delete',
            detail: 'You have cancel delete',
            life: DEFAULT_TOAST_INTERVAL
        })
    }

    const getAdditionalActionItems = (rowData, refresh) => ([
        {
            key: "history-option",
            label: 'History',
            icon: 'pi pi-external-link',
            command: (e) => {
                props.history.push({
                    pathname: `../ingredient/history/${rowData.id}`,
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
        {
            key: "delete-option",
            label: 'Delete',
            icon: 'pi pi-trash',
            command: (e) => onDelete(rowData.id, refresh)
        }
    ])

    /**
     * Confirm dialog for delete function
     * @param {*} id
     * @param refresh
     */
    const onDelete = (id, refresh) => {
        if (id) {
            confirmDialog({
                message: 'Do you want to delete this type?',
                header: 'Delete Confirmation',
                icon: 'pi pi-info-circle',
                acceptClassName: 'p-button-danger',
                accept: () => confirmDelete(id, refresh),
                reject: cancelDelete
            });
        }
    }

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
                refreshData={() => { }}
            />
            <BreadCrumb model={items} home={home} />
            <BaseTable
                service={service}
                name={"ingredient detail"}
                additionalColumns={[
                    getDropdownColumnConfig("unitType", unitType),
                    getDropdownColumnConfig("unit", unit),
                    getNumericColumnConfig("quantity", false, false),
                    getDateColumnConfig("createAt", "Create from"),
                ]}
                Form={ExampleChildForm}
                formProps={{ parentId: parentId }}
                navigateBackLabel={"Back to ingredient category"}
                getNavigateBackLink={(props) => "../test"}
                obtainFilter={setFilter}
                getAdditionalActionItems={getAdditionalActionItems}
                {...props}
            />
        </>

    )
}

export default ExampleChildTable;