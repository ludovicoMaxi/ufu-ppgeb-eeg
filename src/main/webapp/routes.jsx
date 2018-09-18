import React, { Component } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import { HashRouter, Route, Switch, Redirect } from 'react-router-dom';

import ContentHeader from './common/template/contentHeader'
import Content from './common/template/content'
import Dashboard from './dashboard/dashboard'
import PacienteForm from './paciente/pacienteForm'
import PacienteEdit from './paciente/pacienteEdit'
import { init, submitPaciente, remove } from './paciente/pacienteActions'

class Routes extends Component {
    render() {
        return (
            <HashRouter>
                <Switch>
                    <Route exact path='/' component={Dashboard} />
                    <Route path='/paciente/add' render={
                        () => {
                            this.props.init()
                            return (
                                <div><ContentHeader title='Pacientes' small='Cadastro' />
                                    <Content>
                                        <PacienteForm submitLabel='Adicionar'
                                            submitClass='primary' onSubmit={this.props.submitPaciente} />
                                    </Content>
                                </div>
                            )
                        }
                    } />
                    <Route path='/paciente/:pacienteId' component={PacienteEdit} />
                    <Redirect from='*' to='/' />
                </Switch>
            </HashRouter>
        )
    }
}

const mapDispatchToPros = dispatch =>
    bindActionCreators({
        init, submitPaciente, remove
    }, dispatch)
export default connect(null, mapDispatchToPros)(Routes)