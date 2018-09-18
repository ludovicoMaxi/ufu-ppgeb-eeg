import React, { Component } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import { HashRouter, Route, Switch, Redirect } from 'react-router-dom';

import ContentHeader from './common/template/contentHeader'
import Content from './common/template/content'
import Dashboard from './dashboard/dashboard'
import CustomerForm from './customer/customerForm'
import CustomerEdit from './customer/customerEdit'
import { init, submitCustomerView, remove } from './customer/customerActions'

class Routes extends Component {
    render() {
        return (
            <HashRouter>
                <Switch>
                    <Route exact path='/' component={Dashboard} />
                    <Route path='/customer/add' render={() => {
                        this.props.init()
                        return (
                            <div><ContentHeader title='Associados' small='Cadastro' />
                                <Content>
                                    <CustomerForm submitLabel='Adicionar'
                                        submitClass='primary' onSubmit={this.props.submitCustomerView} />
                                </Content>
                            </div>
                        )
                    }
                    } />
                    <Route path='/customer/:customerId' component={CustomerEdit} />
                    <Redirect from='*' to='/' />
                </Switch>
            </HashRouter>
        )
    }
}

const mapDispatchToPros = dispatch =>
    bindActionCreators({
        init, submitCustomerView, remove
    }, dispatch)
export default connect(null, mapDispatchToPros)(Routes)