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
import ExamRequestEdit from './exam-request/examRequestEdit'
import ExamRequestSearch from './exam-request/examRequestSearch'
import ExamRequestRegister from './exam-request/examRequestRegister'
import ExamEdit from './exam/examEdit'
import ExamSearch from './exam/examSearch'
import ExamRegister from './exam/examRegister'
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
                    <Route path='/patient/:patientId/exam-request/add' component={ExamRequestRegister} />
                    <Route path='/exam-request/search' component={ExamRequestSearch} />
                    <Route path='/exam-request/:examRequestId' component={ExamRequestEdit} />
                    <Route path='/patient/:patientId/exam/add' component={ExamRegister} />
                    <Route path='/exam/search' component={ExamSearch} />
                    <Route path='/exam/:examId' component={ExamEdit} />
                    <Redirect from='/patient/*/*' to='/' />
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