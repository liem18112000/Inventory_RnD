import BaseTable from "./BaseTable";
import {IngredientService} from "../../service/IngredientService";
import {
    getDateColumnConfig,
    getDropdownColumnConfig,
    getNumericColumnConfig
} from "./TableUtil";
import {useEffect, useState} from "react";
import ExampleChildForm from "../form/ExampleChildForm";

const ExampleChildTable = (props) => {
    const {
        match,
        isMock = false
    } = props;
    let service = new IngredientService();
    const parentId = match.params.id
    service.getData = (filter, page, rows, sortField, sortOrder) =>
            service.getPageType(parentId, filter, page, rows, sortField, sortOrder, isMock);
    const [unitType, setUnitType] = useState([]);
    const [unit, setUnit] = useState([]);
    const [filter, setFilter] = useState({unitType: ""});

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
        }
    }, [filter.unitType])

    return (
        <BaseTable
            service={service}
            name={"ingredient detail"}
            additionalColumns={[
                getDropdownColumnConfig("unitType", unitType),
                getDropdownColumnConfig("unit", unit),
                getNumericColumnConfig("quantity", false),
                getDateColumnConfig("createAt", "Create from"),
            ]}
            Form={ExampleChildForm}
            formProps={{parentId: parentId}}
            navigateBackLabel={"Back to ingredient category"}
            getNavigateBackLink={(props) => "../test"}
            obtainFilter={setFilter}
            {...props}
        />
    )
}

export default ExampleChildTable;