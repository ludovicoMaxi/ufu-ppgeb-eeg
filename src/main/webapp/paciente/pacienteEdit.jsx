import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { init, getPacienteById, submitPaciente, showUpdate } from './pacienteActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import PacienteForm from './pacienteForm'

class PacienteEdit extends Component {

    componentWillMount() {
        this.props.init();
        const { pacienteId } = this.props.match.params;
        this.props.getPacienteById(pacienteId);
    }

    render() {

        return (
            <div><ContentHeader title='Pacientes' small='Edição' />
                <Content>
                    <PacienteForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitPaciente}
                        showSystemInfo={true} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ init, getPacienteById, submitPaciente, showUpdate }, dispatch)
export default connect(null, mapDispatchToProps)(PacienteEdit)