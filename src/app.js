import React from 'react';
import ReactDOM from 'react-dom';
import $ from 'jquery';
import toastr from 'toastr';

import 'bootstrap/dist/css/bootstrap.css';
import 'toastr/build/toastr.css';

import { BrowserRouter, Switch, Route } from 'react-router-dom';

import MainPage from './pages/MainPage';
import LoginPage from './pages/LoginPage';

ReactDOM.render((
  <BrowserRouter>
    <Switch>
      <Route exact path='/' component={MainPage}/>
      <Route path='/login' component={LoginPage}/>
    </Switch>
  </BrowserRouter>),
  document.getElementById('root')
);
