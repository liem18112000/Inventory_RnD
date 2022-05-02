import BaseForm from "./BaseForm";
import {IngredientService} from "../../service/IngredientService";
import {DEFAULT_SIDE_FORM_INPUTS} from "./config";

const ExampleForm = (props) => {

    const {
        refreshData,
        isMock = false,
        ...formConfig
    } = props;

    const formInputs = DEFAULT_SIDE_FORM_INPUTS;

    let service = new IngredientService();
    service.getById = id => service.getByID(id, isMock);
    service.save = data => service.saveIngredient(data, isMock);
    service.update = data => service.updateIngredient(data, isMock);

    return (
        <BaseForm
            formInputs={formInputs}
            service={service}
            title={"ingredient category"}
            refreshData={refreshData}
            {...formConfig}
        />
    )
}

export default ExampleForm;