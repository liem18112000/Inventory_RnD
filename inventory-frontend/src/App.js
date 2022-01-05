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
import { Login } from './login/Login';
import React, {useRef, useState} from "react";
import {Toast} from "primereact/toast";

export const history = createBrowserHistory();

function App() {

  const toast = useRef(null);

  const [token, setToken] = useState({
    "actorName": null,
    "actorRole": null,
    "isAuthenticate": false
  })

  const handleAuthenticate = (token) => {
    setToken(token)
    if(token.isAuthenticate) {
      toast.current.show({ severity: 'success', summary: 'Login success', detail: 'Login success', life: 1000 });
    }else {
      toast.current.show({ severity: 'error', summary: 'Login failed', detail: 'Login failed', life: 1000 });
    }
  }

  return token.isAuthenticate ? (
    <Router history={history}>
      <Switch>
        <AdminTemplate path="/" exact Component={Dashboard}/>
        <AdminTemplate path="/ingredient-inventory" exact Component={IngredientInventory}/>
        <AdminTemplate path="/ingredient" exact Component={IngredientCategory} />
        <AdminTemplate path="/ingredient/:id" exact Component={IngredientType} />
        <AdminTemplate path="/ingredient/history/:id" exact Component={IngredientHistory} />
        <AdminTemplate path="/ingredient/type/:id" exact Component={IngredientItem} />
        <AdminTemplate path="/recipe" exact Component={RecipeGroup} />
        <AdminTemplate path="/recipes" exact Component={Recipes} />
        <AdminTemplate path="/recipe/:id" exact Component={RecipeChild} />
        <AdminTemplate path="/recipe/child/:id" exact Component={RecipeDetail} />
        <AdminTemplate path="/supplier" exact Component={Dashboard} />
        <AdminTemplate path="/notification" exact Component={Dashboard} />
      </Switch>
    </Router>
  ) : (
    <Router history={history}>
      <Toast ref={toast} />
      <Switch>
        <Login onAuthenticate={handleAuthenticate} path="/"/>
      </Switch>
    </Router>
  )
}

export default App;
