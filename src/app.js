import React from 'react';
import ReactDOM from 'react-dom';
import $ from 'jquery';
import toastr from 'toastr';

import 'bootstrap/dist/css/bootstrap.css';
import 'toastr/build/toastr.css';

import { BrowserRouter, Switch, Route } from 'react-router-dom';

import PacientePage from './pages/PacientePage';
import LoginPage from './pages/LoginPage';
import PedidoRegister from './pages/PedidoRegister';
import ExameRegister from './pages/ExameRegister';

ReactDOM.render((
  <BrowserRouter>
    <Switch>
      <Route exact path='/' component={PacientePage}/>
      <Route path='/login' component={LoginPage}/>
      <Route exact path='/pedido' component={PedidoRegister}/>
      <Route exact path='/exame' component={ExameRegister}/>
    </Switch>
  </BrowserRouter>),
  document.getElementById('root')
);
