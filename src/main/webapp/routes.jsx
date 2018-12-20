import React, { Component } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import { HashRouter, Route, Switch, Redirect } from 'react-router-dom';

import ContentHeader from './common/template/contentHeader'
import Content from './common/template/content'
import Dashboard from './dashboard/dashboard'
import PatientForm from './patient/patientForm'
import PatientSearch from './patient/patientSearch'
import PatientEdit from './patient/patientEdit'
import { init, submitPatient, remove, initRegisterPatient } from './patient/patientActions'

class Routes extends Component {
    render() {
        return (
            <HashRouter>
                <Switch>
                    <Route exact path='/' component={Dashboard} />
                    <Route path='/patient/add' render={
                        () => {
                            this.props.initRegisterPatient()
                            return (
                                <div><ContentHeader title='Pacientes' small='Cadastro' />
                                    <Content>
                                        <PatientForm submitLabel='Adicionar'
                                            submitClass='primary' onSubmit={this.props.submitPatient} />
                                    </Content>
                                </div>
                            )
                        }
                    } />
                    <Route path='/patient/search' render={() => {
                        return (
                            <div><ContentHeader title='Pacientes' small='Busca' />
                                <Content>
                                    <PatientSearch />
                                </Content>
                            </div>
                        )
                    }
                    } />
                    <Route path='/patient/:patientId' component={PatientEdit} />
                    <Redirect from='*' to='/' />
                </Switch>
            </HashRouter>
        )
    }
}

const mapDispatchToPros = dispatch =>
    bindActionCreators({
        init, submitPatient, remove, initRegisterPatient
    }, dispatch)
export default connect(null, mapDispatchToPros)(Routes)