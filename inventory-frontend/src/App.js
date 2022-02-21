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
import { Recipes } from './recipe/recipes/Recipes';
import { IngredientHistory } from './ingredient/type/IngredientHistory';
import { SupplierGroup } from './supplier/group/SupplierGroup';
// import { IngredientItem } from './ingredient/item/IngredientItem';
import { Login } from './login/Login';
import React, { useRef } from "react";
import { Toast } from "primereact/toast";
import { authenticateService, getAuthenticateToken } from "./core/security/AuthenticateService";
import { sleep } from "./core/utility/ComponentUtility";
import { SupplierChild } from './supplier/child/SupplierChild';
import { SupplierMaterial } from './supplier/material/SupplierMaterial';
import { SupplierImport } from './supplier/supplier-import/SupplierImport';
import { Event } from './notification/event/Event';
import { Notification } from './notification/notification/Notification';
import { ImportDetail } from './supplier/import-detail/ImportDetail';
import { SuggestTaxon } from './ingredient/suggest-taxon/SuggestTaxon';

export const history = createBrowserHistory();

function App() {

  const toast = useRef(null);
  const isAuthenticate = getAuthenticateToken()?.isAuthenticate;

  const handleAuthenticate = (username, password) => {
    const isLoginSuccess = authenticateService(username, password);
    if(isLoginSuccess) {
      toast.current.show({ severity: 'success', summary: 'Login success', detail: 'Login success', life: 1000 });
      sleep(500).then(() => window.location.reload())
    } else {
      toast.current.show({ severity: 'error', summary: 'Login failed', detail: 'Login failed', life: 1000 });
    }
  }

  return isAuthenticate ? (
    <Router history={history}>
      <Switch>
        <AdminTemplate path="/" exact Component={Dashboard}/>
        <AdminTemplate path="/ingredient-inventory" exact Component={IngredientInventory}/>
        <AdminTemplate path="/ingredient" exact Component={IngredientCategory} />
        <AdminTemplate path="/ingredient/:id" exact Component={IngredientType} />
        <AdminTemplate path="/ingredient/history/:id" exact Component={IngredientHistory} />
        <AdminTemplate path="/ingredient/type/:id" exact Component={IngredientItem} />
        <AdminTemplate path="/taxon" exact Component={SuggestTaxon} />
        <AdminTemplate path="/recipe" exact Component={RecipeGroup} />
        <AdminTemplate path="/recipes" exact Component={Recipes} />
        <AdminTemplate path="/recipe/:id" exact Component={RecipeChild} />
        <AdminTemplate path="/recipe/child/:id" exact Component={RecipeDetail} />
        <AdminTemplate path="/supplier" exact Component={SupplierGroup} />
        <AdminTemplate path="/supplier/:id" exact Component={SupplierChild} />
        <AdminTemplate path="/supplier/material/:id" exact Component={SupplierMaterial} />
        <AdminTemplate path="/supplier/import/:id" exact Component={SupplierImport} />
        <AdminTemplate path="/notification/event" exact Component={Event} />
        <AdminTemplate path="/notification/event/:id" exact Component={Notification} />
        <AdminTemplate path="/supplier/import/detail/:id" exact Component={ImportDetail} />
        <AdminTemplate path="/notification" exact Component={Dashboard} />
      </Switch>
    </Router>
  ) : (
    <>
      <Toast ref={toast} />
      <Login onAuthenticate={handleAuthenticate} path="/"/>
    </>
  )
}

export default App;
