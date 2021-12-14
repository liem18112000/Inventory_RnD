import 'primereact/resources/themes/saga-blue/theme.css'
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import AdminTemplate from './templates/AdminTemplate';
import { createBrowserHistory } from 'history';
import { Router, Switch } from 'react-router';

import { IngredientCategory } from './ingredient/category/IngredientCategory';
import { IngredientType } from './ingredient/type/IngredientType';
import Dashboard from './pages/Dashboard';
import IngredientItem from './ingredient/item/IngredientItem';
import { IngredientInventory } from './pages/IngredientInventory';
import { RecipeGroup } from './recipe/group/RecipeGroup';
import { RecipeChild } from './recipe/child/RecipeChild';
import { RecipeDetail } from './recipe/detail/RecipeDetail';
// import { IngredientItem } from './ingredient/item/IngredientItem';

export const history = createBrowserHistory();

function App() {
  return (
    <Router history={history}>
      <Switch>
        <AdminTemplate path="/" exact Component={Dashboard}/>
        <AdminTemplate path="/ingredient-inventory" exact Component={IngredientInventory}/>
        <AdminTemplate path="/ingredient" exact Component={IngredientCategory} />
        <AdminTemplate path="/ingredient/:id" exact Component={IngredientType} />
        <AdminTemplate path="/ingredient/type/:id" exact Component={IngredientItem} />
        <AdminTemplate path="/recipe" exact Component={RecipeGroup} />
        <AdminTemplate path="/recipe/:id" exact Component={RecipeChild} />
        <AdminTemplate path="/recipe/child/:id" exact Component={RecipeDetail} />
        <AdminTemplate path="/supplier" exact Component={Dashboard} />
        <AdminTemplate path="/notification" exact Component={Dashboard} />
      </Switch>
    </Router>
  )
}

export default App;
