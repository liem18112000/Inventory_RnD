import BaseTable from "./BaseTable";
import { IngredientService } from "../../service/IngredientService";
import { getDateColumnConfig } from "./TableUtil";
import ExampleForm from "../form/ExampleForm";
import { confirmDialog } from "primereact/confirmdialog";
import { useRef } from "react";
import { Toast } from "primereact/toast";
import { DEFAULT_TOAST_INTERVAL } from "./config";
import { sleep } from "../../core/utility/ComponentUtility";
import { BreadCrumb } from 'primereact/breadcrumb';

const ExampleTable = (props) => {

    const {
        isMock = false
    } = props

    const toast = useRef(null);
    let service = new IngredientService();
    service.getData = (filter, page, rows, sortField, sortOrder) =>
        service.getPageCategory(filter, page, rows, sortField, sortOrder, isMock);
    service.delete = id => service.deleteIngredient(id, isMock);

    const items = [
        { label: 'Ingredient Category' }
    ];

    const home = { icon: 'pi pi-home', url: '/ingredient-inventory' }

    const confirmDelete = (id, refresh) => {
        if (!id) {
            toast.current?.show({
                severity: 'warning',
                summary: 'Delete failed',
                detail: 'Ingredient id is not set',
                life: 3000
            })
        } else {
            service.delete(id)
                .then(res => {
                    if (res) {
                        toast.current?.show({
                            severity: 'success',
                            summary: 'Delete success',
                            detail: 'Ingredient category has been deleted',
                            life: DEFAULT_TOAST_INTERVAL
                        })
                        sleep(DEFAULT_TOAST_INTERVAL / 2)
                            .then(refresh)
                    } else {
                        toast.current?.show({
                            severity: 'error',
                            summary: 'Delete failed',
                            detail: 'Ingredient category may has ingredient details, recipe detail or material referenced ',
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

    const getAdditionalActionItems = (rowData, refresh) => ([{
        key: "delete-option",
        label: 'Delete',
        icon: 'pi pi-trash',
        command: (e) => onDelete(rowData.id, refresh)
    }])

    /**
     * Confirm dialog for delete function
     * @param {*} id
     * @param refresh
     */
    const onDelete = (id, refresh) => {
        if (id) {
            confirmDialog({
                message: 'Do you want to delete this category?',
                header: 'Delete Confirmation',
                icon: 'pi pi-info-circle',
                acceptClassName: 'p-button-danger',
                accept: () => confirmDelete(id, refresh),
                reject: cancelDelete
            });
        }
    }

    return (
        <>
            <Toast ref={toast} />
            <BreadCrumb model={items} home={home} />
            <BaseTable
                service={service}
                name={"ingredient category"}
                additionalColumns={[getDateColumnConfig("createAt", "Create from"),]}
                Form={ExampleForm}
                getNavigateViewLink={(rowData) => `test/${rowData.id}`}
                getAdditionalActionItems={getAdditionalActionItems}
                isMock={isMock}
                {...props}
            />
        </>
    )
}

export default ExampleTable;