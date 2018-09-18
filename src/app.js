import React from 'react';
import ReactDOM from 'react-dom';
import $ from 'jquery';
import toastr from 'toastr';

import 'bootstrap/dist/css/bootstrap.css';
import 'toastr/build/toastr.css';
import './pages/style.css';

import { BrowserRouter, Switch, Route } from 'react-router-dom';

import CarPage from './pages/CarPage';
import CarEditPage from './pages/CarEditPage';
import LoginPage from './pages/LoginPage';

ReactDOM.render((
  <BrowserRouter>
    <Switch>
      <Route exact path='/' component={CarPage}/>
      <Route path='/cars/:id' component={CarEditPage}/>
      <Route path='/login' component={LoginPage}/>
    </Switch>
  </BrowserRouter>),
  document.getElementById('root')
);
