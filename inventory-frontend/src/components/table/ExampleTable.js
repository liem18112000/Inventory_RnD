import BaseTable from "./BaseTable";
import {IngredientCategoryForm} from "../../ingredient/category/IngredientCategoryForm";
import {IngredientService} from "../../service/IngredientService";
import {getDateColumnConfig} from "./TableUtil";

const ExampleTable = () => {
    let service = new IngredientService();
    service.getData = service.getPageCategory;
    return (
        <BaseTable
            service={service}
            name={"ingredient category"}
            additionalColumns={[
                getDateColumnConfig("updateAt", "Update from"),
            ]}
            Form={IngredientCategoryForm}
        />
    )
}

export default ExampleTable;