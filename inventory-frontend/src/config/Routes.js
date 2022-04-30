import {IngredientCategory} from "../ingredient/category/IngredientCategory";
import {IngredientInventory} from "../pages/IngredientInventory";
import {IngredientType} from "../ingredient/type/IngredientType";
import {IngredientHistory} from "../ingredient/type/IngredientHistory";
import IngredientItem from "../ingredient/item/IngredientItem";
import {SuggestTaxon} from "../ingredient/suggest-taxon/SuggestTaxon";
import {RecipeGroup} from "../recipe/group/RecipeGroup";
import {Recipes} from "../recipe/recipes/Recipes";
import {RecipeChild} from "../recipe/child/RecipeChild";
import {RecipeDetail} from "../recipe/detail/RecipeDetail";
import {SupplierGroup} from "../supplier/group/SupplierGroup";
import {SupplierChild} from "../supplier/child/SupplierChild";
import {SupplierMaterial} from "../supplier/material/SupplierMaterial";
import {ImportDetail} from "../supplier/import-detail/ImportDetail";
import {Notification} from "../notification/notification/Notification";
import {Event} from "../notification/event/Event";
import {SupplierImport} from "../supplier/supplier-import/SupplierImport";
import AdminTemplate from "../templates/AdminTemplate";
import TableExample from "../components/table/TableExample";

const routes = [
  {
    path: "/",
    component: IngredientInventory,
  },
  {
    path: "/ingredient-inventory",
    component: IngredientInventory,
  },
  {
    path: "/ingredient",
    component: IngredientCategory,
  },
  {
    path: "/ingredient/:id",
    component: IngredientType,
  },
  {
    path: "/ingredient/history/:id",
    component: IngredientHistory,
  },
  {
    path: "/ingredient/type/:id",
    component: IngredientItem,
  },
  {
    path: "/taxon",
    component: SuggestTaxon,
  },
  {
    path: "/recipe",
    component: RecipeGroup,
  },
  {
    path: "/recipes",
    component: Recipes,
  },
  {
    path: "/recipe/:id",
    component: RecipeChild,
  },
  {
    path: "/recipe/child/:id",
    component: RecipeDetail,
  },
  {
    path: "/supplier",
    component: SupplierGroup,
  },
  {
    path: "/supplier/:id",
    component: SupplierChild,
  },
  {
    path: "/supplier/material/:id",
    component: SupplierMaterial,
  },
  {
    path: "/supplier/import/:id",
    component: SupplierImport,
  },
  {
    path: "/notification/event",
    component: Event,
  },
  {
    path: "/notification/event/:id",
    component: Notification,
  },
  {
    path: "/supplier/import/detail/:id",
    component: ImportDetail,
  },
  {
    path: "/test",
    component: TableExample,
  }
]

const renderRoute = () => {
  return routes.map(route => {
    const {path, component} = route
    return <AdminTemplate path={path} exact Component={component} />
  })
}

export {
    routes,
    renderRoute
}